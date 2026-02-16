package com.example.aem.bmad.core.services.impl;

import com.example.aem.bmad.core.models.EmailResult;
import com.example.aem.bmad.core.services.EmailService;
import com.example.aem.bmad.core.services.HttpClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email service implementation using SendGrid.
 * Supports simple emails, templated emails using AEM templates,
 * and emails with attachments.
 */
@Component(service = EmailService.class, immediate = true)
@Designate(ocd = SendGridEmailServiceImpl.Config.class)
public class SendGridEmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(SendGridEmailServiceImpl.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String SENDGRID_API = "https://api.sendgrid.com/v3/mail/send";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    @ObjectClassDefinition(
        name = "BMAD SendGrid Email Configuration",
        description = "Configuration for SendGrid email service"
    )
    public @interface Config {

        @AttributeDefinition(
            name = "API Key",
            description = "SendGrid API key"
        )
        String apiKey();

        @AttributeDefinition(
            name = "From Email",
            description = "Default sender email"
        )
        String fromEmail() default "noreply@example.com";

        @AttributeDefinition(
            name = "From Name",
            description = "Default sender name"
        )
        String fromName() default "BMAD Website";

        @AttributeDefinition(
            name = "Enabled",
            description = "Enable/disable email sending"
        )
        boolean enabled() default true;
    }

    @Reference
    private HttpClientService httpClient;

    @Reference
    private ResourceResolverFactory resolverFactory;

    private Config config;

    @Activate
    @Modified
    protected void activate(Config config) {
        this.config = config;
        LOG.info("SendGrid email service configured: enabled={}, from={}",
            config.enabled(), config.fromEmail());
    }

    @Override
    public EmailResult sendEmail(String to, String subject, String body) {
        if (!config.enabled()) {
            LOG.info("Email sending disabled, skipping email to: {}", to);
            return EmailResult.success("DISABLED");
        }

        try {
            String payload = buildEmailPayload(to, subject, body, null);
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + config.apiKey());
            headers.put("Content-Type", "application/json");

            var response = httpClient.post(SENDGRID_API, payload, headers);

            if (response.getStatusCode() == 202) {
                String messageId = response.getHeader("X-Message-Id");
                LOG.info("Email sent successfully to: {}, messageId: {}", to, messageId);
                return EmailResult.success(messageId);
            } else {
                LOG.error("Email send failed: {} - {}", response.getStatusCode(), response.getBody());
                return EmailResult.error("SendGrid returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            LOG.error("Error sending email to: {}", to, e);
            return EmailResult.error(e.getMessage());
        }
    }

    @Override
    public EmailResult sendTemplatedEmail(String to, String templatePath, Map<String, Object> variables) {
        if (!config.enabled()) {
            LOG.info("Email sending disabled, skipping templated email to: {}", to);
            return EmailResult.success("DISABLED");
        }

        try {
            // Render template from AEM
            String renderedContent = renderTemplate(templatePath, variables);
            if (renderedContent == null) {
                return EmailResult.error("Failed to render template: " + templatePath);
            }

            // Extract subject from template if present, or use default
            String subject = extractSubject(renderedContent, variables);
            String body = extractBody(renderedContent);

            return sendEmail(to, subject, body);

        } catch (Exception e) {
            LOG.error("Error sending templated email to: {}", to, e);
            return EmailResult.error(e.getMessage());
        }
    }

    @Override
    public EmailResult sendEmailWithAttachments(String to, String subject, String body, Map<String, byte[]> attachments) {
        if (!config.enabled()) {
            LOG.info("Email sending disabled, skipping email with attachments to: {}", to);
            return EmailResult.success("DISABLED");
        }

        try {
            String payload = buildEmailPayload(to, subject, body, attachments);
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + config.apiKey());
            headers.put("Content-Type", "application/json");

            var response = httpClient.post(SENDGRID_API, payload, headers);

            if (response.getStatusCode() == 202) {
                String messageId = response.getHeader("X-Message-Id");
                LOG.info("Email with {} attachments sent to: {}", attachments.size(), to);
                return EmailResult.success(messageId);
            } else {
                LOG.error("Email send failed: {} - {}", response.getStatusCode(), response.getBody());
                return EmailResult.error("SendGrid returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            LOG.error("Error sending email with attachments to: {}", to, e);
            return EmailResult.error(e.getMessage());
        }
    }

    private String renderTemplate(String templatePath, Map<String, Object> variables) {
        Map<String, Object> authInfo = Collections.singletonMap(
            ResourceResolverFactory.SUBSERVICE, "bmad-email-service"
        );

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(authInfo)) {
            Resource templateResource = resolver.getResource(templatePath);
            if (templateResource == null) {
                LOG.error("Template not found: {}", templatePath);
                return null;
            }

            // Get template content (assuming it's stored as jcr:content/text)
            Resource contentResource = templateResource.getChild("jcr:content");
            if (contentResource == null) {
                contentResource = templateResource;
            }

            String template = contentResource.getValueMap().get("text", String.class);
            if (template == null) {
                template = contentResource.getValueMap().get("jcr:data", String.class);
            }

            if (template == null) {
                LOG.error("No template content found at: {}", templatePath);
                return null;
            }

            // Replace variables
            return replaceVariables(template, variables);

        } catch (Exception e) {
            LOG.error("Error rendering template: {}", templatePath, e);
            return null;
        }
    }

    private String replaceVariables(String template, Map<String, Object> variables) {
        if (variables == null || variables.isEmpty()) {
            return template;
        }

        StringBuffer result = new StringBuffer();
        Matcher matcher = VARIABLE_PATTERN.matcher(template);

        while (matcher.find()) {
            String varName = matcher.group(1);
            Object value = variables.get(varName);
            String replacement = value != null ? Matcher.quoteReplacement(value.toString()) : "";
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private String extractSubject(String renderedContent, Map<String, Object> variables) {
        // Try to extract subject from <title> tag or use variable
        if (variables != null && variables.containsKey("subject")) {
            return variables.get("subject").toString();
        }

        // Try to find <title> tag
        int startIdx = renderedContent.indexOf("<title>");
        int endIdx = renderedContent.indexOf("</title>");
        if (startIdx >= 0 && endIdx > startIdx) {
            return renderedContent.substring(startIdx + 7, endIdx).trim();
        }

        return "Message from BMAD Website";
    }

    private String extractBody(String renderedContent) {
        // If content has <body> tag, extract it; otherwise use full content
        int startIdx = renderedContent.indexOf("<body");
        if (startIdx >= 0) {
            int bodyStart = renderedContent.indexOf(">", startIdx) + 1;
            int bodyEnd = renderedContent.indexOf("</body>");
            if (bodyStart > 0 && bodyEnd > bodyStart) {
                return renderedContent.substring(bodyStart, bodyEnd);
            }
        }
        return renderedContent;
    }

    private String buildEmailPayload(String to, String subject, String body, Map<String, byte[]> attachments) throws Exception {
        Map<String, Object> email = new HashMap<>();

        // From
        Map<String, String> from = new HashMap<>();
        from.put("email", config.fromEmail());
        from.put("name", config.fromName());
        email.put("from", from);

        // Personalizations (To)
        List<Map<String, Object>> personalizations = new ArrayList<>();
        Map<String, Object> personalization = new HashMap<>();
        personalization.put("to", List.of(Map.of("email", to)));
        personalizations.add(personalization);
        email.put("personalizations", personalizations);

        // Subject and content
        email.put("subject", subject);
        email.put("content", List.of(
            Map.of("type", "text/html", "value", body)
        ));

        // Attachments
        if (attachments != null && !attachments.isEmpty()) {
            List<Map<String, Object>> attachmentList = new ArrayList<>();
            for (Map.Entry<String, byte[]> entry : attachments.entrySet()) {
                Map<String, Object> attachment = new HashMap<>();
                attachment.put("content", Base64.getEncoder().encodeToString(entry.getValue()));
                attachment.put("filename", entry.getKey());
                attachment.put("type", getMimeType(entry.getKey()));
                attachment.put("disposition", "attachment");
                attachmentList.add(attachment);
            }
            email.put("attachments", attachmentList);
        }

        return MAPPER.writeValueAsString(email);
    }

    private String getMimeType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".doc") || lower.endsWith(".docx")) return "application/msword";
        if (lower.endsWith(".xls") || lower.endsWith(".xlsx")) return "application/vnd.ms-excel";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".txt")) return "text/plain";
        if (lower.endsWith(".html")) return "text/html";
        return "application/octet-stream";
    }
}
