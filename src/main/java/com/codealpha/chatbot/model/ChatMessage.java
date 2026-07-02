package com.codealpha.chatbot.model;

import java.time.Instant;

public class ChatMessage {
    private MessageSender sender;
    private String text;
    private double confidence;
    private boolean fallback;
    private Instant createdAt = Instant.now();

    public ChatMessage() {
    }

    public ChatMessage(MessageSender sender, String text, double confidence, boolean fallback) {
        this.sender = sender;
        this.text = text;
        this.confidence = confidence;
        this.fallback = fallback;
        this.createdAt = Instant.now();
    }

    public MessageSender getSender() {
        return sender;
    }

    public void setSender(MessageSender sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public boolean isFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
