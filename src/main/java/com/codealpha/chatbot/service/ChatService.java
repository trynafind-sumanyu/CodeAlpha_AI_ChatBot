package com.codealpha.chatbot.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codealpha.chatbot.FAQChatbot;
import com.codealpha.chatbot.MatchResult;
import com.codealpha.chatbot.model.ChatMessage;
import com.codealpha.chatbot.model.ChatSession;
import com.codealpha.chatbot.model.MessageSender;
import com.codealpha.chatbot.repository.ChatSessionRepository;

@Service
public class ChatService {
    private final ChatSessionRepository chatSessionRepository;
    private final UserService userService;
    private final FAQChatbot chatbot = new FAQChatbot(FAQChatbot.defaultFaqs());

    public ChatService(ChatSessionRepository chatSessionRepository, UserService userService) {
        this.chatSessionRepository = chatSessionRepository;
        this.userService = userService;
    }

    public ChatSession createSession(String userId, String title) {
        userService.requireUser(userId);

        ChatSession session = new ChatSession();
        session.setUserId(userId);
        session.setTitle(title == null || title.isBlank() ? "New chat" : title.trim());
        return chatSessionRepository.save(session);
    }

    public List<ChatSession> listUserSessions(String userId) {
        userService.requireUser(userId);
        return chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    public ChatSession getUserSession(String userId, String sessionId) {
        userService.requireUser(userId);
        return chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found for this user"));
    }

    public ChatSession addMessage(String userId, String sessionId, String message) {
        ChatSession session = getUserSession(userId, sessionId);
        MatchResult result = chatbot.answer(message);

        session.getMessages().add(new ChatMessage(MessageSender.USER, message.trim(), 0.0, false));
        session.getMessages().add(new ChatMessage(MessageSender.BOT, result.getAnswer(), result.getConfidence(), result.isFallback()));
        session.setUpdatedAt(Instant.now());
        return chatSessionRepository.save(session);
    }

    public MatchResult previewAnswer(String message) {
        return chatbot.answer(message);
    }
}
