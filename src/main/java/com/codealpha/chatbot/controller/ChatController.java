package com.codealpha.chatbot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codealpha.chatbot.MatchResult;
import com.codealpha.chatbot.dto.ChatResponse;
import com.codealpha.chatbot.dto.CreateChatSessionRequest;
import com.codealpha.chatbot.dto.SendMessageRequest;
import com.codealpha.chatbot.model.ChatSession;
import com.codealpha.chatbot.service.ChatService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/{userId}/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatSession createSession(
            @PathVariable String userId,
            @Valid @RequestBody CreateChatSessionRequest request) {
        return chatService.createSession(userId, request.getTitle());
    }

    @GetMapping
    public List<ChatSession> listSessions(@PathVariable String userId) {
        return chatService.listUserSessions(userId);
    }

    @GetMapping("/{sessionId}")
    public ChatSession getSession(@PathVariable String userId, @PathVariable String sessionId) {
        return chatService.getUserSession(userId, sessionId);
    }

    @PostMapping("/{sessionId}/messages")
    public ChatResponse sendMessage(
            @PathVariable String userId,
            @PathVariable String sessionId,
            @Valid @RequestBody SendMessageRequest request) {
        ChatSession session = chatService.addMessage(userId, sessionId, request.getMessage());
        MatchResult answer = chatService.previewAnswer(request.getMessage());
        return new ChatResponse(session, answer.getAnswer(), answer.getConfidence(), answer.isFallback());
    }
}
