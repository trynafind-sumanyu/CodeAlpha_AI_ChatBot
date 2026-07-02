package com.codealpha.chatbot.dto;

import jakarta.validation.constraints.Size;

public class CreateChatSessionRequest {
    @Size(max = 80)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
