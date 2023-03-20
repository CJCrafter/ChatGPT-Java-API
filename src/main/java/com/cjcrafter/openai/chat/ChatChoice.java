package com.cjcrafter.openai.chat;

import com.google.gson.JsonObject;

public final class ChatChoice {

    private final int index;
    private final ChatMessage message;
    private final String finishReason;

    /**
     * Holds the data for 1 generated text completion.
     *
     * @param index        The index in the array... 0 if n=1.
     * @param message      The generated text.
     * @param finishReason Why did the bot stop generating tokens?
     */
    public ChatChoice(int index, ChatMessage message, String finishReason) {
        super();
        this.index = index;
        this.message = message;
        this.finishReason = finishReason;
    }

    public ChatChoice(JsonObject json) {
        this(json.get("index").getAsInt(), new ChatMessage(json.get("message").getAsJsonObject()), json.get("finish_reason").toString());
    }

    public int getIndex() {
        return this.index;
    }

    public ChatMessage getMessage() {
        return this.message;
    }

    public String getFinishReason() {
        return this.finishReason;
    }
}