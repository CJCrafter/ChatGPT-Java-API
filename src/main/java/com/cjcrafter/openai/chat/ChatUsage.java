package com.cjcrafter.openai.chat;

import com.google.gson.JsonObject;

public final class ChatUsage {

    private final int promptTokens;
    private final int completionTokens;
    private final int totalTokens;

    /**
     * Holds how many tokens that were used by your API request. Use these
     * tokens to calculate how much money you have spent on each request.
     *
     * @param promptTokens     How many tokens the input used.
     * @param completionTokens How many tokens the output used.
     * @param totalTokens      How many tokens in total.
     */
    public ChatUsage(int promptTokens, int completionTokens, int totalTokens) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
    }

    public ChatUsage(JsonObject json) {
        this(json.get("prompt_tokens").getAsInt(), json.get("completion_tokens").getAsInt(), json.get("total_tokens").getAsInt());
    }

    public int getPromptTokens() {
        return this.promptTokens;
    }

    public int getCompletionTokens() {
        return this.completionTokens;
    }

    public int getTotalTokens() {
        return this.totalTokens;
    }
}
