package com.codealpha.chatbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Java FAQ chatbot for the CodeAlpha AI Internship task.
 */
public class FAQChatbot {
    private static final double DEFAULT_THRESHOLD = 0.35;
    private static final String FALLBACK_RESPONSE = "Sorry, I do not have an answer for that yet. "
            + "Please ask about CodeAlpha internship tasks, certificates, duration, submission, or support.";

    private final List<FAQ> faqs;
    private final double threshold;

    public FAQChatbot(List<FAQ> faqs) {
        this(faqs, DEFAULT_THRESHOLD);
    }

    public FAQChatbot(List<FAQ> faqs, double threshold) {
        if (faqs == null || faqs.isEmpty()) {
            throw new IllegalArgumentException("FAQ list must not be empty");
        }
        this.faqs = new ArrayList<>(faqs);
        this.threshold = threshold;
    }

    public static List<FAQ> defaultFaqs() {
        return Arrays.asList(
                new FAQ("What is CodeAlpha?", "CodeAlpha is an internship and training platform that helps students build practical projects."),
                new FAQ("What is the duration of the internship?", "The CodeAlpha internship usually runs for one month unless your offer letter says otherwise."),
                new FAQ("How do I submit internship tasks?", "Complete each task, upload the project to GitHub, and submit the required links through the official CodeAlpha submission process."),
                new FAQ("Do I get a certificate after internship completion?", "Yes. Eligible interns receive a certificate after completing and submitting the required tasks successfully."),
                new FAQ("Which programming language should I use?", "Use the language mentioned in your internship track. For a Java internship, build the project in Java."),
                new FAQ("Can I use a graphical user interface?", "Yes. You can extend this chatbot with Java Swing, JavaFX, or a web interface if your task requirements allow it."),
                new FAQ("What should I do if my chatbot does not know an answer?", "Add that question and its answer to the FAQ list, then recompile and run the chatbot again."),
                new FAQ("How can I contact CodeAlpha support?", "Use the official CodeAlpha communication channels mentioned in your internship instructions.")
        );
    }

    public MatchResult answer(String userQuestion) {
        Map<String, Integer> userVector = vectorize(tokenize(userQuestion));
        double bestScore = 0.0;
        FAQ bestFaq = null;

        for (FAQ faq : faqs) {
            double score = cosineSimilarity(userVector, vectorize(tokenize(faq.getQuestion())));
            if (score > bestScore) {
                bestScore = score;
                bestFaq = faq;
            }
        }

        if (bestFaq == null || bestScore < threshold) {
            return new MatchResult(FALLBACK_RESPONSE, bestScore, true);
        }
        return new MatchResult(bestFaq.getAnswer(), bestScore, false);
    }

    public static List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String normalized = text.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", " ").trim();
        if (normalized.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(normalized.split("\\s+"));
    }

    public static Map<String, Integer> vectorize(List<String> tokens) {
        Map<String, Integer> vector = new HashMap<>();
        for (String token : tokens) {
            vector.put(token, vector.getOrDefault(token, 0) + 1);
        }
        return vector;
    }

    public static double cosineSimilarity(Map<String, Integer> first, Map<String, Integer> second) {
        if (first.isEmpty() || second.isEmpty()) {
            return 0.0;
        }

        Set<String> vocabulary = new HashSet<>();
        vocabulary.addAll(first.keySet());
        vocabulary.addAll(second.keySet());

        double dotProduct = 0.0;
        double firstMagnitude = 0.0;
        double secondMagnitude = 0.0;

        for (String word : vocabulary) {
            int firstValue = first.getOrDefault(word, 0);
            int secondValue = second.getOrDefault(word, 0);
            dotProduct += firstValue * secondValue;
            firstMagnitude += firstValue * firstValue;
            secondMagnitude += secondValue * secondValue;
        }

        if (firstMagnitude == 0.0 || secondMagnitude == 0.0) {
            return 0.0;
        }
        return dotProduct / (Math.sqrt(firstMagnitude) * Math.sqrt(secondMagnitude));
    }

    public static void main(String[] args) {
        FAQChatbot chatbot = new FAQChatbot(defaultFaqs());
        Scanner scanner = new Scanner(System.in);

        System.out.println("CodeAlpha Java FAQ Chatbot");
        System.out.println("Ask a question about your internship. Type 'exit' to quit.");

        while (true) {
            System.out.print("You: ");
            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("bye")) {
                System.out.println("Bot: Goodbye and best of luck with your Java internship!");
                break;
            }

            MatchResult result = chatbot.answer(input);
            System.out.printf(Locale.ROOT, "Bot: %s%nConfidence: %.2f%n", result.getAnswer(), result.getConfidence());
        }
    }
}
