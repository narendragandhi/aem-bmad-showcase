package com.example.aem.bmad.core.services;

import com.example.aem.bmad.core.models.EmailResult;

import java.util.Map;

/**
 * Service for sending emails through external providers.
 */
public interface EmailService {

    /**
     * Send a simple email
     *
     * @param to recipient email address
     * @param subject email subject
     * @param body email body (HTML)
     * @return result of the email operation
     */
    EmailResult sendEmail(String to, String subject, String body);

    /**
     * Send a templated email using AEM template
     *
     * @param to recipient email address
     * @param templatePath path to AEM template
     * @param variables template variables for personalization
     * @return result of the email operation
     */
    EmailResult sendTemplatedEmail(String to, String templatePath, Map<String, Object> variables);

    /**
     * Send email with attachments
     *
     * @param to recipient email address
     * @param subject email subject
     * @param body email body (HTML)
     * @param attachments map of filename to file content
     * @return result of the email operation
     */
    EmailResult sendEmailWithAttachments(String to, String subject, String body, Map<String, byte[]> attachments);
}
