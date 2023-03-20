package com.cjcrafter.openai.chat;

import com.google.gson.JsonObject;

/**
 * The ChatGPT API takes a list of 'roles' and 'content'. The role is
 * one of 3 options: system, assistant, and user. 'System' is used to
 * prompt ChatGPT before the user gives input. 'Assistant' is a message
 * from ChatGPT. 'User' is a message from the human.
 */
public final class ChatMessage {

    private final String role;
    private final String content;

    /**
     * Constructor requires who sent the message, and the content of the
     * message.
     *
     * @param role    Who sent the message.
     * @param content The raw content of the message.
     */
    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public ChatMessage(JsonObject json) {
        this(json.get("role").getAsString(), json.get("content").getAsString());
    }

    public String getRole() {
        return this.role;
    }

    public String getContent() {
        return this.content;
    }
}
