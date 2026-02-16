package com.example.aem.bmad.core.services.impl;

import com.example.aem.bmad.core.models.HttpResponse;
import com.example.aem.bmad.core.services.HttpClientService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of HttpClientService using Apache HttpClient.
 * Provides connection pooling and configurable timeouts.
 */
@Component(service = HttpClientService.class, immediate = true)
@Designate(ocd = HttpClientServiceImpl.Config.class)
public class HttpClientServiceImpl implements HttpClientService {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    @ObjectClassDefinition(
        name = "BMAD HTTP Client Configuration",
        description = "Configuration for the HTTP client used in external integrations"
    )
    public @interface Config {

        @AttributeDefinition(
            name = "Connection Timeout (ms)",
            description = "Timeout for establishing connection"
        )
        int connectTimeout() default 5000;

        @AttributeDefinition(
            name = "Socket Timeout (ms)",
            description = "Timeout for waiting on data"
        )
        int socketTimeout() default 30000;

        @AttributeDefinition(
            name = "Connection Request Timeout (ms)",
            description = "Timeout for obtaining connection from pool"
        )
        int connectionRequestTimeout() default 5000;

        @AttributeDefinition(
            name = "Max Total Connections",
            description = "Maximum total connections across all routes"
        )
        int maxTotalConnections() default 100;

        @AttributeDefinition(
            name = "Max Connections Per Route",
            description = "Maximum connections per host"
        )
        int maxConnectionsPerRoute() default 20;
    }

    private CloseableHttpClient httpClient;
    private PoolingHttpClientConnectionManager connectionManager;

    @Activate
    @Modified
    protected void activate(Config config) {
        // Close existing client if reconfiguring
        deactivate();

        // Connection pool manager
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.maxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(config.maxConnectionsPerRoute());

        // Request configuration
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(config.connectTimeout())
            .setSocketTimeout(config.socketTimeout())
            .setConnectionRequestTimeout(config.connectionRequestTimeout())
            .build();

        // Build HTTP client
        httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build();

        LOG.info("HTTP client configured: connectTimeout={}, socketTimeout={}, maxConnections={}",
            config.connectTimeout(), config.socketTimeout(), config.maxTotalConnections());
    }

    @Deactivate
    protected void deactivate() {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                LOG.warn("Error closing HTTP client", e);
            }
            httpClient = null;
        }
        if (connectionManager != null) {
            connectionManager.close();
            connectionManager = null;
        }
    }

    @Override
    public HttpResponse get(String url, Map<String, String> headers) {
        return request("GET", url, null, headers);
    }

    @Override
    public HttpResponse post(String url, String body, Map<String, String> headers) {
        return request("POST", url, body, headers);
    }

    @Override
    public HttpResponse put(String url, String body, Map<String, String> headers) {
        return request("PUT", url, body, headers);
    }

    @Override
    public HttpResponse delete(String url, Map<String, String> headers) {
        return request("DELETE", url, null, headers);
    }

    @Override
    public HttpResponse patch(String url, String body, Map<String, String> headers) {
        return request("PATCH", url, body, headers);
    }

    @Override
    public HttpResponse request(String method, String url, String body, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();

        HttpRequestBase request = createRequest(method, url, body);

        // Apply headers
        if (headers != null) {
            headers.forEach(request::setHeader);
        }

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = response.getEntity() != null
                ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                : null;

            Map<String, String> responseHeaders = new HashMap<>();
            for (var header : response.getAllHeaders()) {
                responseHeaders.put(header.getName(), header.getValue());
            }

            long duration = System.currentTimeMillis() - startTime;
            LOG.debug("{} {} completed in {}ms with status {}",
                method, url, duration, statusCode);

            return new HttpResponse(statusCode, responseBody, responseHeaders, duration);

        } catch (IOException e) {
            long duration = System.currentTimeMillis() - startTime;
            LOG.error("{} {} failed after {}ms: {}", method, url, duration, e.getMessage());
            throw new RuntimeException("HTTP request failed: " + e.getMessage(), e);
        }
    }

    private HttpRequestBase createRequest(String method, String url, String body) {
        HttpRequestBase request;

        switch (method.toUpperCase()) {
            case "GET":
                request = new HttpGet(url);
                break;
            case "POST":
                HttpPost post = new HttpPost(url);
                if (body != null) {
                    post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
                }
                request = post;
                break;
            case "PUT":
                HttpPut put = new HttpPut(url);
                if (body != null) {
                    put.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
                }
                request = put;
                break;
            case "DELETE":
                request = new HttpDelete(url);
                break;
            case "PATCH":
                HttpPatch patch = new HttpPatch(url);
                if (body != null) {
                    patch.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
                }
                request = patch;
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        return request;
    }
}
