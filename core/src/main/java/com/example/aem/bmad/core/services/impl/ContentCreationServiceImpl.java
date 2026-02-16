package com.example.aem.bmad.core.services.impl;

import com.example.aem.bmad.core.models.LLMRequest;
import com.example.aem.bmad.core.models.LLMResponse;
import com.example.aem.bmad.core.services.ContentCreationService;
import com.example.aem.bmad.core.services.LLMService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of ContentCreationService using LLM services.
 */
@Component(service = ContentCreationService.class)
@Designate(ocd = ContentCreationServiceImpl.Config.class)
public class ContentCreationServiceImpl implements ContentCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentCreationServiceImpl.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @ObjectClassDefinition(
        name = "BMAD Content Creation Configuration",
        description = "Configuration for AI content creation service"
    )
    public @interface Config {

        @AttributeDefinition(name = "LLM Provider")
        String llmProvider() default "openai";

        @AttributeDefinition(name = "Model")
        String model() default "gpt-4o";

        @AttributeDefinition(name = "Creative Temperature")
        double creativeTemperature() default 0.8;

        @AttributeDefinition(name = "Factual Temperature")
        double factualTemperature() default 0.3;

        @AttributeDefinition(name = "Enabled")
        boolean enabled() default true;
    }

    @Reference
    private LLMServiceFactory llmFactory;

    private Config config;

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
        LOG.info("Content creation service configured: provider={}, model={}",
            config.llmProvider(), config.model());
    }

    @Override
    public LLMResponse generateContent(String prompt, String context) {
        LLMService llm = getLLMService();
        if (llm == null) {
            return LLMResponse.error("LLM service not available");
        }

        String systemPrompt = "You are a professional content writer for a website. " +
            "Create engaging, clear, and well-structured content. " +
            "Use appropriate HTML formatting when needed.";

        if (context != null && !context.isEmpty()) {
            systemPrompt += "\n\nContext from the page:\n" + truncate(context, 2000);
        }

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage(prompt)
            .model(config.model())
            .temperature(config.creativeTemperature())
            .build();

        return llm.complete(request);
    }

    @Override
    public LLMResponse generateTitle(String content, String style) {
        LLMService llm = getLLMService();
        if (llm == null) {
            return LLMResponse.error("LLM service not available");
        }

        String styleInstructions = "";
        if (style != null && !style.isEmpty()) {
            styleInstructions = " Style: " + style + ".";
        }

        String systemPrompt = "You are a headline writer. Generate a compelling, concise title." +
            styleInstructions + " Return only the title, no quotes or extra formatting.";

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage("Generate a title for this content:\n\n" + truncate(content, 3000))
            .model(config.model())
            .temperature(config.creativeTemperature())
            .maxTokens(100)
            .build();

        return llm.complete(request);
    }

    @Override
    public LLMResponse summarize(String content, int targetLength) {
        LLMService llm = getLLMService();
        if (llm == null) {
            return LLMResponse.error("LLM service not available");
        }

        String systemPrompt = "You are a content summarizer. Create clear, accurate summaries " +
            "that capture the key points. Target approximately " + targetLength + " words.";

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage("Summarize this content:\n\n" + content)
            .model(config.model())
            .temperature(config.factualTemperature())
            .build();

        return llm.complete(request);
    }

    @Override
    public LLMResponse improveContent(String content, String instructions) {
        LLMService llm = getLLMService();
        if (llm == null) {
            return LLMResponse.error("LLM service not available");
        }

        String systemPrompt = "You are an expert editor. Improve the provided content while " +
            "maintaining its core message and meaning. Preserve any HTML formatting.";

        String userPrompt = "Improve this content";
        if (instructions != null && !instructions.isEmpty()) {
            userPrompt += " with these specific instructions: " + instructions;
        }
        userPrompt += "\n\nContent:\n" + content;

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage(userPrompt)
            .model(config.model())
            .temperature(config.factualTemperature())
            .build();

        return llm.complete(request);
    }

    @Override
    public LLMResponse expandContent(String content, int targetLength) {
        LLMService llm = getLLMService();
        if (llm == null) {
            return LLMResponse.error("LLM service not available");
        }

        String systemPrompt = "You are a content writer. Expand the provided content to approximately " +
            targetLength + " words while maintaining consistency in tone and style. " +
            "Add relevant details, examples, or explanations. Preserve any HTML formatting.";

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage("Expand this content:\n\n" + content)
            .model(config.model())
            .temperature(config.creativeTemperature())
            .build();

        return llm.complete(request);
    }

    @Override
    public Map<String, String> generateSEOMetadata(String pageContent) {
        Map<String, String> metadata = new HashMap<>();

        LLMService llm = getLLMService();
        if (llm == null) {
            return metadata;
        }

        String systemPrompt = "You are an SEO expert. Generate metadata for a webpage. " +
            "Return a JSON object with exactly these keys: " +
            "\"title\" (60 chars max), \"description\" (160 chars max), \"keywords\" (comma-separated).";

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage("Generate SEO metadata for:\n\n" + truncate(pageContent, 3000))
            .model(config.model())
            .temperature(config.factualTemperature())
            .maxTokens(300)
            .build();

        LLMResponse response = llm.complete(request);

        if (response.isSuccess()) {
            try {
                String jsonContent = extractJson(response.getContent());
                JsonNode root = MAPPER.readTree(jsonContent);

                if (root.has("title")) {
                    metadata.put("title", root.get("title").asText());
                }
                if (root.has("description")) {
                    metadata.put("description", root.get("description").asText());
                }
                if (root.has("keywords")) {
                    metadata.put("keywords", root.get("keywords").asText());
                }
            } catch (Exception e) {
                LOG.warn("Failed to parse SEO metadata response", e);
            }
        }

        return metadata;
    }

    @Override
    public LLMResponse generateAltText(String imageContext) {
        LLMService llm = getLLMService();
        if (llm == null) {
            return LLMResponse.error("LLM service not available");
        }

        String systemPrompt = "You are an accessibility expert. Generate concise, descriptive alt text " +
            "for images. The alt text should be informative but brief (under 125 characters). " +
            "Return only the alt text, no quotes.";

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage("Generate alt text for an image with this context: " + imageContext)
            .model(config.model())
            .temperature(config.factualTemperature())
            .maxTokens(50)
            .build();

        return llm.complete(request);
    }

    @Override
    public LLMResponse analyzeContent(String content) {
        LLMService llm = getLLMService();
        if (llm == null) {
            return LLMResponse.error("LLM service not available");
        }

        String systemPrompt = "You are an editor and content analyst. Analyze the provided content for:\n" +
            "1. Grammar and spelling issues\n" +
            "2. Clarity and readability\n" +
            "3. Tone consistency\n" +
            "4. SEO-friendliness\n" +
            "5. Engagement potential\n\n" +
            "Provide specific, actionable suggestions for improvement.";

        LLMRequest request = LLMRequest.builder()
            .systemPrompt(systemPrompt)
            .addUserMessage("Analyze this content:\n\n" + content)
            .model(config.model())
            .temperature(config.factualTemperature())
            .build();

        return llm.complete(request);
    }

    private LLMService getLLMService() {
        if (!config.enabled()) {
            return null;
        }
        return llmFactory.getService(config.llmProvider());
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    private String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }
        return response;
    }
}
