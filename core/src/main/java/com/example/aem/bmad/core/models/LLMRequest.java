package com.example.aem.bmad.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request object for LLM completions.
 * Supports chat-style messages and configuration options.
 *
 * Use the Builder pattern for constructing requests:
 * <pre>
 * LLMRequest request = LLMRequest.builder()
 *     .systemPrompt("You are a helpful assistant")
 *     .addUserMessage("Hello!")
 *     .temperature(0.7)
 *     .build();
 * </pre>
 */
public class LLMRequest {

    private final List<Message> messages;
    private final String model;
    private final double temperature;
    private final int maxTokens;
    private final String systemPrompt;
    private final Map<String, Object> metadata;

    private LLMRequest(Builder builder) {
        this.messages = new ArrayList<>(builder.messages);
        this.model = builder.model;
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
        this.systemPrompt = builder.systemPrompt;
        this.metadata = new HashMap<>(builder.metadata);
    }

    /**
     * Create a new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Convenience method for simple single-prompt requests
     *
     * @param prompt the user prompt
     * @return a request with default settings
     */
    public static LLMRequest simple(String prompt) {
        return builder()
            .addUserMessage(prompt)
            .build();
    }

    /**
     * Convenience method with system prompt
     *
     * @param systemPrompt the system/context prompt
     * @param userPrompt the user's message
     * @return a request with system context
     */
    public static LLMRequest withSystem(String systemPrompt, String userPrompt) {
        return builder()
            .systemPrompt(systemPrompt)
            .addUserMessage(userPrompt)
            .build();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getModel() {
        return model;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * Represents a single message in a conversation
     */
    public static class Message {
        private final String role;
        private final String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        /**
         * Get the role (user, assistant, or system)
         */
        public String getRole() {
            return role;
        }

        /**
         * Get the message content
         */
        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return role + ": " + content;
        }
    }

    /**
     * Builder for constructing LLMRequest objects
     */
    public static class Builder {
        private List<Message> messages = new ArrayList<>();
        private String model;
        private double temperature = 0.7;
        private int maxTokens = 2048;
        private String systemPrompt;
        private Map<String, Object> metadata = new HashMap<>();

        /**
         * Add a message with the specified role
         *
         * @param role the role (user, assistant, system)
         * @param content the message content
         */
        public Builder addMessage(String role, String content) {
            messages.add(new Message(role, content));
            return this;
        }

        /**
         * Add a user message
         *
         * @param content the user's message
         */
        public Builder addUserMessage(String content) {
            return addMessage("user", content);
        }

        /**
         * Add an assistant message (for conversation history)
         *
         * @param content the assistant's previous response
         */
        public Builder addAssistantMessage(String content) {
            return addMessage("assistant", content);
        }

        /**
         * Set the model to use (provider-specific)
         *
         * @param model model identifier (e.g., "gpt-4o", "claude-sonnet-4-20250514")
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * Set the temperature (creativity) setting
         *
         * @param temperature value between 0.0 (deterministic) and 1.0 (creative)
         */
        public Builder temperature(double temperature) {
            this.temperature = Math.max(0.0, Math.min(1.0, temperature));
            return this;
        }

        /**
         * Set the maximum tokens in the response
         *
         * @param maxTokens maximum response length in tokens
         */
        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        /**
         * Set the system prompt (context/instructions for the model)
         *
         * @param systemPrompt the system-level instructions
         */
        public Builder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        /**
         * Add metadata for tracking/logging purposes
         *
         * @param key metadata key
         * @param value metadata value
         */
        public Builder metadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        /**
         * Build the request
         *
         * @return the constructed LLMRequest
         */
        public LLMRequest build() {
            return new LLMRequest(this);
        }
    }
}
