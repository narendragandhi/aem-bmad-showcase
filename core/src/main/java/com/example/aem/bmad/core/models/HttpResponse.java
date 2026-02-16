package com.example.aem.bmad.core.models;

import java.util.Collections;
import java.util.Map;

/**
 * Represents an HTTP response from an external service.
 */
public class HttpResponse {

    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final long durationMs;

    public HttpResponse(int statusCode, String body, Map<String, String> headers, long durationMs) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers != null ? headers : Collections.emptyMap();
        this.durationMs = durationMs;
    }

    /**
     * Get the HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Get the response body
     */
    public String getBody() {
        return body;
    }

    /**
     * Get response headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Get the request duration in milliseconds
     */
    public long getDurationMs() {
        return durationMs;
    }

    /**
     * Check if the response indicates success (2xx status)
     */
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    /**
     * Check if the response indicates a client error (4xx status)
     */
    public boolean isClientError() {
        return statusCode >= 400 && statusCode < 500;
    }

    /**
     * Check if the response indicates a server error (5xx status)
     */
    public boolean isServerError() {
        return statusCode >= 500;
    }

    /**
     * Get a specific header value
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
            "statusCode=" + statusCode +
            ", durationMs=" + durationMs +
            ", bodyLength=" + (body != null ? body.length() : 0) +
            '}';
    }
}
