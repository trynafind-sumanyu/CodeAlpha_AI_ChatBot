# CodeAlpha AI Internship - Java FAQ Chatbot

This repository contains **Task 2** for the CodeAlpha AI Internship: a Java-based FAQ chatbot that answers common internship questions using simple Natural Language Processing (NLP) techniques.

## Features

- Built fully in Java with no external dependencies.
- Uses text normalization, tokenization, bag-of-words vectors, and cosine similarity.
- Returns a confidence score for every matched FAQ.
- Provides friendly fallback responses for unknown questions.
- Includes an interactive command-line chatbot.
- Includes a lightweight Java test runner for the core chatbot logic.

## Project Structure

```text
src/main/java/com/codealpha/chatbot/
├── FAQ.java          # Immutable FAQ question/answer data model
├── FAQChatbot.java   # Matching engine and CLI entry point
└── MatchResult.java  # Immutable response result with confidence

src/test/java/com/codealpha/chatbot/
└── FAQChatbotTest.java # Dependency-free test runner
```

## Requirements

- Java Development Kit (JDK) 11 or newer

Check your Java version:

```bash
java -version
javac -version
```

## Compile the Project

```bash
mkdir -p out
javac -d out $(find src/main/java -name "*.java")
```

## Run the Chatbot

```bash
java -cp out com.codealpha.chatbot.FAQChatbot
```

Example questions you can ask:

- `What is CodeAlpha?`
- `How long is the internship?`
- `Do I get a certificate?`
- `How do I submit my tasks?`

Type `exit`, `quit`, or `bye` to close the chatbot.

## Run Tests

```bash
mkdir -p out
javac -d out $(find src/main/java src/test/java -name "*.java")
java -cp out com.codealpha.chatbot.FAQChatbotTest
```

## How It Works

1. The chatbot stores a list of predefined internship FAQs.
2. User input and FAQ questions are normalized to lowercase words.
3. Each question is converted into a bag-of-words frequency vector.
4. Cosine similarity compares the user question with every FAQ question.
5. The best match is returned if its score is above the confidence threshold.
6. If the score is too low, the chatbot returns a fallback response.

## Customize FAQs

Add or edit entries in `FAQChatbot.defaultFaqs()` inside `src/main/java/com/codealpha/chatbot/FAQChatbot.java`.
