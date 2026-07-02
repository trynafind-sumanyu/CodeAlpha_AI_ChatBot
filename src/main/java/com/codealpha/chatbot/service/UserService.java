package com.codealpha.chatbot.service;

import org.springframework.stereotype.Service;

import com.codealpha.chatbot.model.User;
import com.codealpha.chatbot.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String displayName) {
        String normalizedUsername = username.trim().toLowerCase();
        return userRepository.findByUsername(normalizedUsername).orElseGet(() -> {
            User user = new User();
            user.setUsername(normalizedUsername);
            user.setDisplayName(displayName == null || displayName.isBlank() ? normalizedUsername : displayName.trim());
            return userRepository.save(user);
        });
    }

    public User requireUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}
