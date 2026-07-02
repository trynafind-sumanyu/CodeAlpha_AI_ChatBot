package com.codealpha.chatbot;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Dependency-free tests so the project can be checked with only the JDK.
 */
public class FAQChatbotTest {
    private static int passed = 0;

    public static void main(String[] args) {
        testTokenizeNormalizesText();
        testVectorizeCountsRepeatedTokens();
        testCosineSimilarityForIdenticalVectors();
        testAnswerMatchesCertificateQuestion();
        testUnknownQuestionUsesFallback();
        System.out.println("All " + passed + " tests passed.");
    }

    private static void testTokenizeNormalizesText() {
        assertEquals(Arrays.asList("hello", "java", "2026"), FAQChatbot.tokenize("Hello, JAVA!!! 2026"));
    }

    private static void testVectorizeCountsRepeatedTokens() {
        Map<String, Integer> vector = FAQChatbot.vectorize(Arrays.asList("java", "java", "chatbot"));
        assertEquals(2, vector.get("java"));
        assertEquals(1, vector.get("chatbot"));
    }

    private static void testCosineSimilarityForIdenticalVectors() {
        List<String> tokens = Arrays.asList("codealpha", "java", "internship");
        double score = FAQChatbot.cosineSimilarity(FAQChatbot.vectorize(tokens), FAQChatbot.vectorize(tokens));
        assertTrue(score > 0.99, "Expected identical vectors to have near-perfect similarity");
    }

    private static void testAnswerMatchesCertificateQuestion() {
        FAQChatbot chatbot = new FAQChatbot(FAQChatbot.defaultFaqs());
        MatchResult result = chatbot.answer("Do interns get certificate after completion?");
        assertTrue(!result.isFallback(), "Expected certificate question to match a known FAQ");
        assertTrue(result.getAnswer().toLowerCase().contains("certificate"), "Expected answer to mention certificate");
    }

    private static void testUnknownQuestionUsesFallback() {
        FAQChatbot chatbot = new FAQChatbot(FAQChatbot.defaultFaqs());
        MatchResult result = chatbot.answer("rainbow volcano pizza galaxy");
        assertTrue(result.isFallback(), "Expected unrelated question to use fallback response");
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected <" + expected + "> but got <" + actual + ">");
        }
        passed++;
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        passed++;
    }
}
