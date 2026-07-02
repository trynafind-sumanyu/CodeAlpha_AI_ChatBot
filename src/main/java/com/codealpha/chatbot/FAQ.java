package com.codealpha.chatbot;

import java.util.Objects;

/**
 * Immutable FAQ entry used by the chatbot knowledge base.
 */
public final class FAQ {
    private final String question;
    private final String answer;

    public FAQ(String question, String answer) {
        this.question = Objects.requireNonNull(question, "question must not be null");
        this.answer = Objects.requireNonNull(answer, "answer must not be null");
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
