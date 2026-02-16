package com.example.aem.bmad.core.services;

import com.example.aem.bmad.core.models.LLMRequest;
import com.example.aem.bmad.core.models.LLMResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Abstraction layer for Large Language Model services.
 * Implementations can target OpenAI, Anthropic Claude, Google Gemini, or other providers.
 *
 * This service provides a unified interface for:
 * - Text completions (chat-style interactions)
 * - Async completions for non-blocking operations
 * - Streaming responses for real-time UI updates
 * - Embedding generation for RAG/semantic search
 */
public interface LLMService {

    /**
     * Get the provider identifier (e.g., "openai", "claude", "gemini")
     *
     * @return provider ID string
     */
    String getProviderId();

    /**
     * Check if the service is available and properly configured
     *
     * @return true if service can accept requests
     */
    boolean isAvailable();

    /**
     * Generate a completion for the given request synchronously
     *
     * @param request the completion request with prompt and options
     * @return the completion response
     */
    LLMResponse complete(LLMRequest request);

    /**
     * Generate a completion asynchronously
     *
     * @param request the completion request
     * @return future containing the completion response
     */
    CompletableFuture<LLMResponse> completeAsync(LLMRequest request);

    /**
     * Stream a completion response for real-time UI updates
     *
     * @param request the completion request
     * @param callback callback invoked for each chunk of the response
     */
    void streamCompletion(LLMRequest request, StreamCallback callback);

    /**
     * Generate embeddings for text (used for RAG/semantic search)
     *
     * @param texts list of texts to generate embeddings for
     * @return list of embedding vectors (one per input text)
     */
    List<float[]> generateEmbeddings(List<String> texts);

    /**
     * Callback interface for streaming responses
     */
    interface StreamCallback {
        /**
         * Called when a new chunk of text is received
         *
         * @param chunk the text chunk
         */
        void onChunk(String chunk);

        /**
         * Called when the stream is complete
         *
         * @param fullResponse the complete response object
         */
        void onComplete(LLMResponse fullResponse);

        /**
         * Called if an error occurs during streaming
         *
         * @param e the exception that occurred
         */
        void onError(Exception e);
    }
}
