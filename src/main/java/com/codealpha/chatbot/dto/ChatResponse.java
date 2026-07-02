package com.codealpha.chatbot.dto;

import com.codealpha.chatbot.model.ChatSession;

public class ChatResponse {
    private ChatSession session;
    private String answer;
    private double confidence;
    private boolean fallback;

    public ChatResponse(ChatSession session, String answer, double confidence, boolean fallback) {
        this.session = session;
        this.answer = answer;
        this.confidence = confidence;
        this.fallback = fallback;
    }

    public ChatSession getSession() {
        return session;
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
