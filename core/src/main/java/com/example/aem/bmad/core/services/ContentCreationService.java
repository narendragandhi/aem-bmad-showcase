package com.example.aem.bmad.core.services;

import com.example.aem.bmad.core.models.LLMResponse;

import java.util.Map;

/**
 * AI-powered content creation service for AEM authors.
 * Provides various content generation capabilities.
 */
public interface ContentCreationService {

    /**
     * Generate content based on a custom prompt
     *
     * @param prompt the user's prompt
     * @param context optional context (e.g., page content)
     * @return generated content
     */
    LLMResponse generateContent(String prompt, String context);

    /**
     * Generate a title based on content
     *
     * @param content the content to generate a title for
     * @param style optional style hints (e.g., "formal", "casual", "SEO-optimized")
     * @return generated title
     */
    LLMResponse generateTitle(String content, String style);

    /**
     * Summarize content to a specified length
     *
     * @param content the content to summarize
     * @param targetLength target word count (approximate)
     * @return summarized content
     */
    LLMResponse summarize(String content, int targetLength);

    /**
     * Improve existing content
     *
     * @param content the content to improve
     * @param instructions specific improvement instructions
     * @return improved content
     */
    LLMResponse improveContent(String content, String instructions);

    /**
     * Expand/extend existing content
     *
     * @param content the content to expand
     * @param targetLength target word count
     * @return extended content
     */
    LLMResponse expandContent(String content, int targetLength);

    /**
     * Generate SEO metadata
     *
     * @param pageContent the page content
     * @return map containing title, description, keywords
     */
    Map<String, String> generateSEOMetadata(String pageContent);

    /**
     * Generate alt text for an image
     *
     * @param imageContext description or context about the image
     * @return suggested alt text
     */
    LLMResponse generateAltText(String imageContext);

    /**
     * Check content for issues (grammar, tone, clarity)
     *
     * @param content content to check
     * @return analysis with suggestions
     */
    LLMResponse analyzeContent(String content);
}
