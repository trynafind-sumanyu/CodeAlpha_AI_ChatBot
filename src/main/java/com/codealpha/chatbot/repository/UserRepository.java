package com.codealpha.chatbot.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.codealpha.chatbot.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
