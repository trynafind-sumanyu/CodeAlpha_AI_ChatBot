const state = {
    user: JSON.parse(localStorage.getItem('codealphaUser') || 'null'),
    activeSession: null,
    sessions: []
};

const userForm = document.querySelector('#user-form');
const usernameInput = document.querySelector('#username');
const displayNameInput = document.querySelector('#display-name');
const userStatus = document.querySelector('#user-status');
const newChatButton = document.querySelector('#new-chat-button');
const sessionsList = document.querySelector('#sessions-list');
const chatTitle = document.querySelector('#active-chat-title');
const connectionPill = document.querySelector('#connection-pill');
const chatLog = document.querySelector('#chat-log');
const messageForm = document.querySelector('#message-form');
const messageInput = document.querySelector('#message-input');
const sendButton = document.querySelector('#send-button');

async function requestJson(url, options = {}) {
    const response = await fetch(url, {
        headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
        ...options
    });

    if (!response.ok) {
        let message = `Request failed with status ${response.status}`;
        try {
            const errorBody = await response.json();
            message = errorBody.error || message;
        } catch (error) {
            // Keep the default message if the server did not return JSON.
        }
        throw new Error(message);
    }

    return response.json();
}

function setUserStatus(message, isError = false) {
    userStatus.textContent = message;
    userStatus.classList.toggle('error', isError);
}

function setConnected(isConnected) {
    connectionPill.textContent = isConnected ? 'Connected' : 'Not connected';
    connectionPill.classList.toggle('connected', isConnected);
}

function saveUser(user) {
    state.user = user;
    localStorage.setItem('codealphaUser', JSON.stringify(user));
    usernameInput.value = user.username || '';
    displayNameInput.value = user.displayName || '';
    newChatButton.disabled = false;
    setConnected(true);
    setUserStatus(`Loaded ${user.displayName || user.username}. Your chats are private to this user.`);
}

function renderSessions() {
    sessionsList.innerHTML = '';

    if (!state.user) {
        sessionsList.innerHTML = '<p class="empty-state">No user selected yet.</p>';
        return;
    }

    if (state.sessions.length === 0) {
        sessionsList.innerHTML = '<p class="empty-state">No chats yet. Create your first private chat.</p>';
        return;
    }

    state.sessions.forEach((session) => {
        const button = document.createElement('button');
        button.type = 'button';
        button.className = `session-button ${state.activeSession?.id === session.id ? 'active' : ''}`;
        button.textContent = session.title || 'New chat';
        button.addEventListener('click', () => openSession(session.id));
        sessionsList.appendChild(button);
    });
}

function renderMessages(session) {
    chatLog.innerHTML = '';

    if (!session || !session.messages || session.messages.length === 0) {
        appendMessage('BOT', 'This private chat is ready. Ask me anything about your CodeAlpha internship.', 0, false);
        return;
    }

    session.messages.forEach((message) => {
        appendMessage(message.sender, message.text, message.confidence, message.fallback);
    });
}

function appendMessage(sender, text, confidence = 0, fallback = false) {
    const message = document.createElement('article');
    const isUser = sender === 'USER';
    message.className = `message ${isUser ? 'user-message' : 'bot-message'}`;

    const body = document.createElement('p');
    body.textContent = text;
    message.appendChild(body);

    if (!isUser && confidence > 0) {
        const meta = document.createElement('p');
        meta.className = 'message-meta';
        meta.textContent = `Confidence: ${confidence.toFixed(2)}${fallback ? ' · fallback' : ''}`;
        message.appendChild(meta);
    }

    chatLog.appendChild(message);
    chatLog.scrollTop = chatLog.scrollHeight;
}

function enableMessaging(enabled) {
    messageInput.disabled = !enabled;
    sendButton.disabled = !enabled;
}

async function loadSessions() {
    if (!state.user) {
        return;
    }

    state.sessions = await requestJson(`/api/users/${state.user.id}/chats`);
    renderSessions();
}

async function openSession(sessionId) {
    const session = await requestJson(`/api/users/${state.user.id}/chats/${sessionId}`);
    state.activeSession = session;
    chatTitle.textContent = session.title || 'Private chat';
    enableMessaging(true);
    renderSessions();
    renderMessages(session);
}

userForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    try {
        setUserStatus('Loading user...');
        const user = await requestJson('/api/users', {
            method: 'POST',
            body: JSON.stringify({
                username: usernameInput.value,
                displayName: displayNameInput.value
            })
        });
        saveUser(user);
        await loadSessions();
    } catch (error) {
        setConnected(false);
        setUserStatus(error.message, true);
    }
});

newChatButton.addEventListener('click', async () => {
    if (!state.user) {
        return;
    }

    try {
        const title = prompt('Chat title:', 'Internship FAQ Help') || 'New chat';
        const session = await requestJson(`/api/users/${state.user.id}/chats`, {
            method: 'POST',
            body: JSON.stringify({ title })
        });
        await loadSessions();
        await openSession(session.id);
    } catch (error) {
        setUserStatus(error.message, true);
    }
});

messageForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const message = messageInput.value.trim();
    if (!message || !state.user || !state.activeSession) {
        return;
    }

    messageInput.value = '';
    appendMessage('USER', message);

    try {
        const response = await requestJson(`/api/users/${state.user.id}/chats/${state.activeSession.id}/messages`, {
            method: 'POST',
            body: JSON.stringify({ message })
        });
        state.activeSession = response.session;
        renderMessages(response.session);
        await loadSessions();
    } catch (error) {
        appendMessage('BOT', error.message, 0, true);
    }
});

async function hydrate() {
    if (!state.user) {
        setConnected(false);
        enableMessaging(false);
        return;
    }

    saveUser(state.user);
    try {
        await loadSessions();
    } catch (error) {
        setConnected(false);
        setUserStatus('Could not load saved user. Create or load the user again.', true);
        localStorage.removeItem('codealphaUser');
        state.user = null;
    }
}

hydrate();
