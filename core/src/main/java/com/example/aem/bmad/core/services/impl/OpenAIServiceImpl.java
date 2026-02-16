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
 * OpenAI implementation of LLMService.
 * Supports GPT-4, GPT-4-turbo, GPT-3.5-turbo and other OpenAI models.
 */
@Component(
    service = LLMService.class,
    property = {
        "provider=openai"
    }
)
@Designate(ocd = OpenAIServiceImpl.Config.class)
public class OpenAIServiceImpl implements LLMService {

    private static final Logger LOG = LoggerFactory.getLogger(OpenAIServiceImpl.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String CHAT_COMPLETIONS_API = "https://api.openai.com/v1/chat/completions";
    private static final String EMBEDDINGS_API = "https://api.openai.com/v1/embeddings";

    @ObjectClassDefinition(
        name = "BMAD OpenAI LLM Configuration",
        description = "Configuration for OpenAI API integration"
    )
    public @interface Config {

        @AttributeDefinition(
            name = "API Key",
            description = "OpenAI API key (use Cloud Manager secrets in production)"
        )
        String apiKey();

        @AttributeDefinition(
            name = "Default Model",
            description = "Default model for completions"
        )
        String defaultModel() default "gpt-4o";

        @AttributeDefinition(
            name = "Embedding Model",
            description = "Model for generating embeddings"
        )
        String embeddingModel() default "text-embedding-3-small";

        @AttributeDefinition(
            name = "Organization ID",
            description = "Optional OpenAI organization ID"
        )
        String organizationId() default "";

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
            description = "Enable/disable OpenAI service"
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
        LOG.info("OpenAI service configured: enabled={}, model={}", config.enabled(), config.defaultModel());
    }

    @Override
    public String getProviderId() {
        return "openai";
    }

    @Override
    public boolean isAvailable() {
        return config != null && config.enabled() && config.apiKey() != null && !config.apiKey().isEmpty();
    }

    @Override
    public LLMResponse complete(LLMRequest request) {
        if (!isAvailable()) {
            return LLMResponse.error("OpenAI service is not available or not configured");
        }

        long startTime = System.currentTimeMillis();

        try {
            String payload = buildChatCompletionPayload(request);
            Map<String, String> headers = buildHeaders();

            var response = httpClient.post(CHAT_COMPLETIONS_API, payload, headers);

            long duration = System.currentTimeMillis() - startTime;
            LOG.debug("OpenAI request completed in {}ms", duration);

            if (response.isSuccess()) {
                return parseChatCompletionResponse(response.getBody());
            } else {
                String errorMsg = parseErrorMessage(response.getBody());
                LOG.error("OpenAI API error: {} - {}", response.getStatusCode(), errorMsg);
                return LLMResponse.error("OpenAI API error: " + errorMsg);
            }
        } catch (Exception e) {
            LOG.error("Error calling OpenAI API", e);
            return LLMResponse.error("Failed to call OpenAI API: " + e.getMessage());
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
        if (!isAvailable() || texts == null || texts.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            ObjectNode payload = MAPPER.createObjectNode();
            payload.put("model", config.embeddingModel());

            ArrayNode inputArray = payload.putArray("input");
            for (String text : texts) {
                inputArray.add(text);
            }

            var response = httpClient.post(EMBEDDINGS_API, payload.toString(), buildHeaders());

            if (response.isSuccess()) {
                return parseEmbeddingsResponse(response.getBody());
            } else {
                LOG.error("Embeddings API error: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            LOG.error("Error generating embeddings", e);
        }

        return Collections.emptyList();
    }

    private String buildChatCompletionPayload(LLMRequest request) throws Exception {
        ObjectNode payload = MAPPER.createObjectNode();

        // Model
        String model = request.getModel() != null ? request.getModel() : config.defaultModel();
        payload.put("model", model);

        // Temperature
        double temperature = request.getTemperature() > 0 ? request.getTemperature() : config.defaultTemperature();
        payload.put("temperature", temperature);

        // Max tokens
        int maxTokens = request.getMaxTokens() > 0 ? request.getMaxTokens() : config.defaultMaxTokens();
        payload.put("max_tokens", maxTokens);

        // Messages
        ArrayNode messages = payload.putArray("messages");

        // Add system prompt if present
        if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
            ObjectNode systemMsg = messages.addObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", request.getSystemPrompt());
        }

        // Add conversation messages
        for (LLMRequest.Message msg : request.getMessages()) {
            ObjectNode msgNode = messages.addObject();
            msgNode.put("role", msg.getRole());
            msgNode.put("content", msg.getContent());
        }

        return MAPPER.writeValueAsString(payload);
    }

    private Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + config.apiKey());
        headers.put("Content-Type", "application/json");

        if (config.organizationId() != null && !config.organizationId().isEmpty()) {
            headers.put("OpenAI-Organization", config.organizationId());
        }

        return headers;
    }

    private LLMResponse parseChatCompletionResponse(String responseBody) throws Exception {
        JsonNode root = MAPPER.readTree(responseBody);

        JsonNode choices = root.get("choices");
        if (choices != null && choices.size() > 0) {
            JsonNode choice = choices.get(0);
            JsonNode message = choice.get("message");

            String content = message.get("content").asText();
            String finishReason = choice.has("finish_reason") ? choice.get("finish_reason").asText() : null;
            String modelUsed = root.has("model") ? root.get("model").asText() : null;

            // Parse usage
            LLMResponse.Usage usage = null;
            if (root.has("usage")) {
                JsonNode usageNode = root.get("usage");
                usage = new LLMResponse.Usage(
                    usageNode.get("prompt_tokens").asInt(),
                    usageNode.get("completion_tokens").asInt()
                );
            }

            return new LLMResponse.Builder()
                .success(true)
                .content(content)
                .usage(usage)
                .model(modelUsed)
                .finishReason(finishReason)
                .build();
        }

        return LLMResponse.error("No choices in OpenAI response");
    }

    private List<float[]> parseEmbeddingsResponse(String responseBody) throws Exception {
        JsonNode root = MAPPER.readTree(responseBody);
        List<float[]> embeddings = new ArrayList<>();

        JsonNode dataArray = root.get("data");
        if (dataArray != null) {
            for (JsonNode data : dataArray) {
                JsonNode embeddingArray = data.get("embedding");
                float[] embedding = new float[embeddingArray.size()];
                for (int i = 0; i < embeddingArray.size(); i++) {
                    embedding[i] = (float) embeddingArray.get(i).asDouble();
                }
                embeddings.add(embedding);
            }
        }

        return embeddings;
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
