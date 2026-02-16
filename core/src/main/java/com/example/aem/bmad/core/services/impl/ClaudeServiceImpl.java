package com.example.aem.bmad.core.services.impl;

import com.example.aem.bmad.core.models.LLMRequest;
import com.example.aem.bmad.core.models.LLMResponse;
import com.example.aem.bmad.core.services.HttpClientService;
import com.example.aem.bmad.core.services.LLMService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Anthropic Claude implementation of LLMService.
 * Supports Claude 3.5 Sonnet, Claude 3 Opus, and other Claude models.
 */
@Component(
    service = LLMService.class,
    property = {
        "provider=claude"
    }
)
@Designate(ocd = ClaudeServiceImpl.Config.class)
public class ClaudeServiceImpl implements LLMService {

    private static final Logger LOG = LoggerFactory.getLogger(ClaudeServiceImpl.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String MESSAGES_API = "https://api.anthropic.com/v1/messages";
    private static final String API_VERSION = "2023-06-01";

    @ObjectClassDefinition(
        name = "BMAD Anthropic Claude Configuration",
        description = "Configuration for Anthropic Claude API integration"
    )
    public @interface Config {

        @AttributeDefinition(
            name = "API Key",
            description = "Anthropic API key (use Cloud Manager secrets in production)"
        )
        String apiKey();

        @AttributeDefinition(
            name = "Default Model",
            description = "Default Claude model for completions"
        )
        String defaultModel() default "claude-sonnet-4-20250514";

        @AttributeDefinition(
            name = "Default Temperature",
            description = "Default temperature setting (0.0 - 1.0)"
        )
        double defaultTemperature() default 0.7;

        @AttributeDefinition(
            name = "Default Max Tokens",
            description = "Default maximum tokens in response"
        )
        int defaultMaxTokens() default 2048;

        @AttributeDefinition(
            name = "Enabled",
            description = "Enable/disable Claude service"
        )
        boolean enabled() default true;
    }

    @Reference
    private HttpClientService httpClient;

    private Config config;

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
        LOG.info("Claude service configured: enabled={}, model={}", config.enabled(), config.defaultModel());
    }

    @Override
    public String getProviderId() {
        return "claude";
    }

    @Override
    public boolean isAvailable() {
        return config != null && config.enabled() && config.apiKey() != null && !config.apiKey().isEmpty();
    }

    @Override
    public LLMResponse complete(LLMRequest request) {
        if (!isAvailable()) {
            return LLMResponse.error("Claude service is not available or not configured");
        }

        long startTime = System.currentTimeMillis();

        try {
            String payload = buildMessagesPayload(request);
            Map<String, String> headers = buildHeaders();

            var response = httpClient.post(MESSAGES_API, payload, headers);

            long duration = System.currentTimeMillis() - startTime;
            LOG.debug("Claude request completed in {}ms", duration);

            if (response.isSuccess()) {
                return parseMessagesResponse(response.getBody());
            } else {
                String errorMsg = parseErrorMessage(response.getBody());
                LOG.error("Claude API error: {} - {}", response.getStatusCode(), errorMsg);
                return LLMResponse.error("Claude API error: " + errorMsg);
            }
        } catch (Exception e) {
            LOG.error("Error calling Claude API", e);
            return LLMResponse.error("Failed to call Claude API: " + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<LLMResponse> completeAsync(LLMRequest request) {
        return CompletableFuture.supplyAsync(() -> complete(request));
    }

    @Override
    public void streamCompletion(LLMRequest request, StreamCallback callback) {
        // For full streaming, would need SSE support in HttpClientService
        // Falling back to regular completion for now
        try {
            LLMResponse response = complete(request);
            if (response.isSuccess()) {
                callback.onChunk(response.getContent());
                callback.onComplete(response);
            } else {
                callback.onError(new RuntimeException(response.getError()));
            }
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    @Override
    public List<float[]> generateEmbeddings(List<String> texts) {
        // Claude doesn't have a native embeddings API
        // For embeddings, use OpenAI or a dedicated embedding service
        LOG.warn("Embeddings not supported by Claude provider. Use OpenAI or a dedicated embedding service.");
        return Collections.emptyList();
    }

    private String buildMessagesPayload(LLMRequest request) throws Exception {
        ObjectNode payload = MAPPER.createObjectNode();

        // Model
        String model = request.getModel() != null ? request.getModel() : config.defaultModel();
        payload.put("model", model);

        // Max tokens (required for Claude)
        int maxTokens = request.getMaxTokens() > 0 ? request.getMaxTokens() : config.defaultMaxTokens();
        payload.put("max_tokens", maxTokens);

        // System prompt (top-level field in Claude API)
        if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
            payload.put("system", request.getSystemPrompt());
        }

        // Messages array
        ArrayNode messages = payload.putArray("messages");

        for (LLMRequest.Message msg : request.getMessages()) {
            ObjectNode msgNode = messages.addObject();
            msgNode.put("role", msg.getRole());
            msgNode.put("content", msg.getContent());
        }

        return MAPPER.writeValueAsString(payload);
    }

    private Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-api-key", config.apiKey());
        headers.put("anthropic-version", API_VERSION);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private LLMResponse parseMessagesResponse(String responseBody) throws Exception {
        JsonNode root = MAPPER.readTree(responseBody);

        JsonNode content = root.get("content");
        if (content != null && content.size() > 0) {
            // Claude returns content as an array of content blocks
            StringBuilder textContent = new StringBuilder();

            for (JsonNode block : content) {
                String type = block.has("type") ? block.get("type").asText() : "";
                if ("text".equals(type)) {
                    textContent.append(block.get("text").asText());
                }
            }

            String modelUsed = root.has("model") ? root.get("model").asText() : null;
            String stopReason = root.has("stop_reason") ? root.get("stop_reason").asText() : null;

            // Parse usage
            LLMResponse.Usage usage = null;
            if (root.has("usage")) {
                JsonNode usageNode = root.get("usage");
                usage = new LLMResponse.Usage(
                    usageNode.get("input_tokens").asInt(),
                    usageNode.get("output_tokens").asInt()
                );
            }

            return new LLMResponse.Builder()
                .success(true)
                .content(textContent.toString())
                .usage(usage)
                .model(modelUsed)
                .finishReason(stopReason)
                .build();
        }

        return LLMResponse.error("No content in Claude response");
    }

    private String parseErrorMessage(String responseBody) {
        try {
            JsonNode root = MAPPER.readTree(responseBody);
            if (root.has("error")) {
                JsonNode error = root.get("error");
                if (error.has("message")) {
                    return error.get("message").asText();
                }
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return responseBody;
    }
}
