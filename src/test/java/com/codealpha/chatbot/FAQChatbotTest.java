package com.codealpha.chatbot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class FAQChatbotTest {
    @Test
    void tokenizeNormalizesText() {
        assertThat(FAQChatbot.tokenize("Hello, JAVA!!! 2026"))
                .containsExactly("hello", "java", "2026");
    }

    @Test
    void vectorizeCountsRepeatedTokens() {
        Map<String, Integer> vector = FAQChatbot.vectorize(Arrays.asList("java", "java", "chatbot"));

        assertThat(vector.get("java")).isEqualTo(2);
        assertThat(vector.get("chatbot")).isEqualTo(1);
    }

    @Test
    void cosineSimilarityForIdenticalVectors() {
        List<String> tokens = Arrays.asList("codealpha", "java", "internship");
        double score = FAQChatbot.cosineSimilarity(FAQChatbot.vectorize(tokens), FAQChatbot.vectorize(tokens));

        assertThat(score).isGreaterThan(0.99);
    }

    @Test
    void answerMatchesCertificateQuestion() {
        FAQChatbot chatbot = new FAQChatbot(FAQChatbot.defaultFaqs());
        MatchResult result = chatbot.answer("Do interns get certificate after completion?");

        assertThat(result.isFallback()).isFalse();
        assertThat(result.getAnswer().toLowerCase()).contains("certificate");
    }

    @Test
    void unknownQuestionUsesFallback() {
        FAQChatbot chatbot = new FAQChatbot(FAQChatbot.defaultFaqs());
        MatchResult result = chatbot.answer("rainbow volcano pizza galaxy");

        assertThat(result.isFallback()).isTrue();
    }
}
