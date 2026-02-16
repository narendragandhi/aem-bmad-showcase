package com.example.aem.bmad.core.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of an AI translation operation.
 */
public class TranslationResult {

    private final boolean success;
    private final int propertiesTranslated;
    private final int propertiesSkipped;
    private final int tokensUsed;
    private final long durationMs;
    private final List<String> errors;
    private final List<String> warnings;

    private TranslationResult(Builder builder) {
        this.success = builder.success;
        this.propertiesTranslated = builder.propertiesTranslated;
        this.propertiesSkipped = builder.propertiesSkipped;
        this.tokensUsed = builder.tokensUsed;
        this.durationMs = builder.durationMs;
        this.errors = new ArrayList<>(builder.errors);
        this.warnings = new ArrayList<>(builder.warnings);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static TranslationResult error(String error) {
        return builder().success(false).addError(error).build();
    }

    public boolean isSuccess() {
        return success;
    }

    public int getPropertiesTranslated() {
        return propertiesTranslated;
    }

    public int getPropertiesSkipped() {
        return propertiesSkipped;
    }

    public int getTokensUsed() {
        return tokensUsed;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        return "TranslationResult{" +
            "success=" + success +
            ", propertiesTranslated=" + propertiesTranslated +
            ", tokensUsed=" + tokensUsed +
            ", durationMs=" + durationMs +
            '}';
    }

    public static class Builder {
        private boolean success = true;
        private int propertiesTranslated = 0;
        private int propertiesSkipped = 0;
        private int tokensUsed = 0;
        private long durationMs = 0;
        private List<String> errors = new ArrayList<>();
        private List<String> warnings = new ArrayList<>();

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder propertiesTranslated(int count) {
            this.propertiesTranslated = count;
            return this;
        }

        public Builder propertiesSkipped(int count) {
            this.propertiesSkipped = count;
            return this;
        }

        public Builder tokensUsed(int tokens) {
            this.tokensUsed = tokens;
            return this;
        }

        public Builder durationMs(long duration) {
            this.durationMs = duration;
            return this;
        }

        public Builder addError(String error) {
            this.errors.add(error);
            this.success = false;
            return this;
        }

        public Builder addWarning(String warning) {
            this.warnings.add(warning);
            return this;
        }

        public TranslationResult build() {
            return new TranslationResult(this);
        }
    }
}
