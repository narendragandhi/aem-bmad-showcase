package com.example.aem.bmad.core.models;

/**
 * Response from an LLM completion request.
 * Contains the generated content, usage statistics, and status information.
 */
public class LLMResponse {

    private final boolean success;
    private final String content;
    private final String error;
    private final Usage usage;
    private final String model;
    private final String finishReason;

    private LLMResponse(Builder builder) {
        this.success = builder.success;
        this.content = builder.content;
        this.error = builder.error;
        this.usage = builder.usage;
        this.model = builder.model;
        this.finishReason = builder.finishReason;
    }

    /**
     * Create a successful response
     *
     * @param content the generated content
     * @param usage token usage statistics
     * @return successful response
     */
    public static LLMResponse success(String content, Usage usage) {
        return new Builder()
            .success(true)
            .content(content)
            .usage(usage)
            .build();
    }

    /**
     * Create an error response
     *
     * @param error the error message
     * @return error response
     */
    public static LLMResponse error(String error) {
        return new Builder()
            .success(false)
            .error(error)
            .build();
    }

    /**
     * Check if the request was successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Get the generated content (null if error)
     */
    public String getContent() {
        return content;
    }

    /**
     * Get the error message (null if success)
     */
    public String getError() {
        return error;
    }

    /**
     * Get token usage statistics
     */
    public Usage getUsage() {
        return usage;
    }

    /**
     * Get the model that was used
     */
    public String getModel() {
        return model;
    }

    /**
     * Get the reason the generation finished (stop, length, etc.)
     */
    public String getFinishReason() {
        return finishReason;
    }

    @Override
    public String toString() {
        if (success) {
            return "LLMResponse{success=true, model='" + model + "', " +
                   "contentLength=" + (content != null ? content.length() : 0) + ", " +
                   "usage=" + usage + "}";
        } else {
            return "LLMResponse{success=false, error='" + error + "'}";
        }
    }

    /**
     * Token usage statistics
     */
    public static class Usage {
        private final int promptTokens;
        private final int completionTokens;
        private final int totalTokens;

        public Usage(int promptTokens, int completionTokens) {
            this.promptTokens = promptTokens;
            this.completionTokens = completionTokens;
            this.totalTokens = promptTokens + completionTokens;
        }

        /**
         * Get the number of tokens in the prompt/input
         */
        public int getPromptTokens() {
            return promptTokens;
        }

        /**
         * Get the number of tokens in the completion/output
         */
        public int getCompletionTokens() {
            return completionTokens;
        }

        /**
         * Get the total tokens used
         */
        public int getTotalTokens() {
            return totalTokens;
        }

        @Override
        public String toString() {
            return "Usage{prompt=" + promptTokens + ", completion=" + completionTokens +
                   ", total=" + totalTokens + "}";
        }
    }

    /**
     * Builder for constructing LLMResponse objects
     */
    public static class Builder {
        private boolean success;
        private String content;
        private String error;
        private Usage usage;
        private String model;
        private String finishReason;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder usage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder finishReason(String finishReason) {
            this.finishReason = finishReason;
            return this;
        }

        public LLMResponse build() {
            return new LLMResponse(this);
        }
    }
}
