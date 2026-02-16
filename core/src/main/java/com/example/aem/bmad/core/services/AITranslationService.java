package com.example.aem.bmad.core.services;

import com.example.aem.bmad.core.models.TranslationResult;
import org.apache.sling.api.resource.Resource;

import java.util.Locale;
import java.util.Map;

/**
 * AI-powered translation service for AEM content.
 * Uses LLM services to translate pages, components, and text.
 */
public interface AITranslationService {

    /**
     * Translate a single page to the target language
     *
     * @param sourcePage path to source page
     * @param targetPage path to target page (live copy or language copy)
     * @param targetLocale target language
     * @return translation result with statistics
     */
    TranslationResult translatePage(String sourcePage, String targetPage, Locale targetLocale);

    /**
     * Translate a component resource
     *
     * @param sourceComponent source component resource
     * @param targetComponent target component resource
     * @param targetLocale target language
     * @return translation result
     */
    TranslationResult translateComponent(Resource sourceComponent, Resource targetComponent, Locale targetLocale);

    /**
     * Translate a tree of pages
     *
     * @param sourceRoot root of source tree
     * @param targetRoot root of target tree
     * @param targetLocale target language
     * @param recursive include child pages
     * @return aggregated translation results
     */
    TranslationResult translateTree(String sourceRoot, String targetRoot, Locale targetLocale, boolean recursive);

    /**
     * Get translation status for a page
     *
     * @param pagePath page to check
     * @return map of property names to their translation status
     */
    Map<String, TranslationStatus> getTranslationStatus(String pagePath);

    /**
     * Translate specific text (for preview/testing)
     *
     * @param text source text
     * @param sourceLocale source language
     * @param targetLocale target language
     * @return translated text
     */
    String translateText(String text, Locale sourceLocale, Locale targetLocale);

    /**
     * Translation status for a property
     */
    enum TranslationStatus {
        /** Source unchanged, translation matches */
        UP_TO_DATE,
        /** Source changed, needs re-translation */
        SOURCE_CHANGED,
        /** Translation was manually modified */
        MANUALLY_EDITED,
        /** No translation metadata exists */
        NEVER_TRANSLATED
    }
}
