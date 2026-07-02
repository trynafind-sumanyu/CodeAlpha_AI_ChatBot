package com.codealpha.chatbot;

/**
 * Represents the chatbot response and the confidence of the selected match.
 */
public final class MatchResult {
    private final String answer;
    private final double confidence;
    private final boolean fallback;

    public MatchResult(String answer, double confidence, boolean fallback) {
        this.answer = answer;
        this.confidence = confidence;
        this.fallback = fallback;
    }

    public String getAnswer() {
        return answer;
    }

    public double getConfidence() {
        return confidence;
    }

    public boolean isFallback() {
        return fallback;
    }
}
