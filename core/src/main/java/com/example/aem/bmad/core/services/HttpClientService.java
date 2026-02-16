package com.example.aem.bmad.core.services;

import com.example.aem.bmad.core.models.HttpResponse;

import java.util.Map;

/**
 * Service for making HTTP requests to external services.
 * Provides a standardized way to interact with REST APIs.
 */
public interface HttpClientService {

    /**
     * Perform GET request
     *
     * @param url the target URL
     * @param headers request headers
     * @return HTTP response
     */
    HttpResponse get(String url, Map<String, String> headers);

    /**
     * Perform POST request
     *
     * @param url the target URL
     * @param body request body
     * @param headers request headers
     * @return HTTP response
     */
    HttpResponse post(String url, String body, Map<String, String> headers);

    /**
     * Perform PUT request
     *
     * @param url the target URL
     * @param body request body
     * @param headers request headers
     * @return HTTP response
     */
    HttpResponse put(String url, String body, Map<String, String> headers);

    /**
     * Perform DELETE request
     *
     * @param url the target URL
     * @param headers request headers
     * @return HTTP response
     */
    HttpResponse delete(String url, Map<String, String> headers);

    /**
     * Perform PATCH request
     *
     * @param url the target URL
     * @param body request body
     * @param headers request headers
     * @return HTTP response
     */
    HttpResponse patch(String url, String body, Map<String, String> headers);

    /**
     * Generic request method
     *
     * @param method HTTP method (GET, POST, PUT, DELETE, PATCH)
     * @param url the target URL
     * @param body request body (can be null for GET/DELETE)
     * @param headers request headers
     * @return HTTP response
     */
    HttpResponse request(String method, String url, String body, Map<String, String> headers);
}
