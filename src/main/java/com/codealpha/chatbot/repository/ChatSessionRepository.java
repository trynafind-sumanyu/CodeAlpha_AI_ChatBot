package com.codealpha.chatbot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.codealpha.chatbot.model.ChatSession;

public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {
    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(String userId);

    Optional<ChatSession> findByIdAndUserId(String id, String userId);
}
