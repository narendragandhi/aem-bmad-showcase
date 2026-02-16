package com.example.aem.bmad.core.models;

/**
 * Represents the result of an email sending operation.
 */
public class EmailResult {

    private final boolean success;
    private final String messageId;
    private final String error;

    private EmailResult(boolean success, String messageId, String error) {
        this.success = success;
        this.messageId = messageId;
        this.error = error;
    }

    /**
     * Create a successful result
     */
    public static EmailResult success() {
        return new EmailResult(true, null, null);
    }

    /**
     * Create a successful result with message ID
     */
    public static EmailResult success(String messageId) {
        return new EmailResult(true, messageId, null);
    }

    /**
     * Create an error result
     */
    public static EmailResult error(String error) {
        return new EmailResult(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        if (success) {
            return "EmailResult{success=true, messageId='" + messageId + "'}";
        } else {
            return "EmailResult{success=false, error='" + error + "'}";
        }
    }
}
