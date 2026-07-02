# CodeAlpha AI Internship - Java Spring Boot FAQ Chatbot

This repository contains **Task 2** for the CodeAlpha AI Internship: a Java FAQ chatbot that can run as a Spring Boot web API with MongoDB-backed private chat sessions for each user.

## Live Link :-
https://codealpha-ai-chatbot-ldae.onrender.com

## Features

- Built with Java, Spring Boot, Spring Web, and Spring Data MongoDB.
- Stores users in MongoDB.
- Creates private chat sessions per user.
- Saves each user's dedicated chat history inside that user's chat session.
- Prevents users from opening another user's chat by always querying sessions with both `userId` and `sessionId`.
- Uses text normalization, tokenization, bag-of-words vectors, and cosine similarity for FAQ matching.
- Returns confidence and fallback metadata with bot answers.
- Includes JUnit tests for the chatbot matching logic.
- Includes a browser chat interface served from `/` with HTML, CSS, and JavaScript.
- Ready for Render deployment with `PORT` and `MONGODB_URI` environment variables.

## Project Structure

```text
src/main/java/com/codealpha/chatbot/
├── CodeAlphaChatbotApplication.java       # Spring Boot entry point
├── FAQ.java                               # FAQ question/answer model
├── FAQChatbot.java                        # FAQ matching engine and CLI-compatible main
├── MatchResult.java                       # Bot answer result
├── controller/                            # REST API controllers and exception handler
├── dto/                                   # Request/response DTOs
├── model/                                 # MongoDB user, session, and message documents
├── repository/                            # Spring Data MongoDB repositories
└── service/                               # User and chat business logic

src/main/resources/static/
├── index.html                              # Browser UI layout
├── style.css                               # Responsive dashboard styling
└── app.js                                  # Frontend API calls and chat rendering

src/test/java/com/codealpha/chatbot/
└── FAQChatbotTest.java                    # JUnit tests for matching logic
```

## Requirements

- Java Development Kit (JDK) 17 or newer
- Maven 3.9+
- MongoDB Atlas connection string or local MongoDB

## Environment Variables

| Variable | Purpose | Example |
| --- | --- | --- |
| `PORT` | Web server port. Render sets this automatically. | `8080` |
| `MONGODB_URI` | MongoDB connection string. | `mongodb+srv://user:pass@cluster.mongodb.net/codealpha_chatbot` |

If `MONGODB_URI` is not set, the app defaults to `mongodb://localhost:27017/codealpha_chatbot`.

## Run Locally

```bash
mvn spring-boot:run
```

## Build for Deployment

```bash
mvn clean package
java -jar target/ai-chatbot-0.0.1-SNAPSHOT.jar
```

## Browser Interface

After starting the app, open the chatbot UI in your browser:

```text
http://localhost:8080/
```

The page lets you create/load a user, create private chat sections, select a chat, send messages, and view saved chat history from MongoDB.

## API Endpoints

### Create or Reuse a User

```http
POST /api/users
Content-Type: application/json

{
  "username": "student01",
  "displayName": "Student One"
}
```

### Create a Private Chat Session for a User

```http
POST /api/users/{userId}/chats
Content-Type: application/json

{
  "title": "Internship FAQ Help"
}
```

### List Only That User's Chat Sessions

```http
GET /api/users/{userId}/chats
```

### Open One Private Chat Session

```http
GET /api/users/{userId}/chats/{sessionId}
```

### Send a Message and Store User + Bot Messages

```http
POST /api/users/{userId}/chats/{sessionId}/messages
Content-Type: application/json

{
  "message": "Do I get a certificate?"
}
```

The response includes the updated session, answer, confidence score, and fallback flag.

## MongoDB Collections

- `users`: stores user profiles.
- `chat_sessions`: stores a `userId`, title, timestamps, and the dedicated messages for that user's session.

## Render Deployment

1. Push this repository to GitHub.
2. Create a MongoDB Atlas database and copy the connection string.
3. Create a new Render Web Service from the GitHub repository.
4. Set the build command:

```bash
mvn clean package
```

5. Set the start command:

```bash
java -jar target/ai-chatbot-0.0.1-SNAPSHOT.jar
```

6. Add the `MONGODB_URI` environment variable in Render.
7. Deploy and use your Render URL as the live project link.

## Vercel Note

This project now includes the frontend inside Spring Boot, so Render can host both the API and the browser interface from one deployed service. Vercel is still useful if you later split the frontend into a separate React/Vite app and connect it to this Render API URL.

## Run Tests

```bash
mvn test
```

## Customize FAQs

Add or edit entries in `FAQChatbot.defaultFaqs()` inside `src/main/java/com/codealpha/chatbot/FAQChatbot.java`.
