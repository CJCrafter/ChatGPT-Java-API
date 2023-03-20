package com.cjcrafter.openai.chat;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * These are the arguments that control the result of the output. For more
 * information, refer to the <a href="https://platform.openai.com/docs/api-reference/completions/create">OpenAI Docs</a>.
 */
public final class ChatRequest {

    private final String model;
    private final List<ChatMessage> messages;
    private final float temperature;
    @SerializedName("top_p")
    private final float topP;
    private final int n;
    private final boolean stream;
    private final String stop;
    @SerializedName("max_tokens")
    private final Integer maxTokens;
    @SerializedName("presence_penalty")
    private final float presencePenalty;
    @SerializedName("frequency_penalty")
    private final float frequencyPenalty;
    @SerializedName("logit_bias")
    private final JsonObject logitBias;
    private final String user;

    /**
     * Shorthand constructor for {@link #ChatRequest(String, List, float, float, int, boolean, String, Integer, float, float, JsonObject, String)}
     *
     * @param model    The model to use to generate the text. Recommended: "gpt-3.5-turbo"
     * @param messages All previous messages from the conversation.
     */
    public ChatRequest(String model, List<ChatMessage> messages) {
        this(model, messages, 1.0f, 1.0f, 1, false, null, null, 0f, 0f, null, null);
    }

    /**
     * @param model            The model used to generate the text. Recommended: "gpt-3.5-turbo."
     * @param messages         All previous messages from the conversation.
     * @param temperature      How "creative" the results are. [0.0, 2.0].
     * @param topP             Controls how "on topic" the tokens are.
     * @param n                Controls how many responses to generate. Numbers >1 will chew through your tokens.
     * @param stream           <b>UNTESTED</b> recommend keeping this false.
     * @param stop             The sequence used to stop generating tokens.
     * @param maxTokens        The maximum number of tokens to use.
     * @param presencePenalty  Prevent talking about duplicate topics.
     * @param frequencyPenalty Prevent repeating the same text.
     * @param logitBias        Control specific tokens from being used.
     * @param user             Who send this request (for moderation).
     */
    public ChatRequest(String model, List<ChatMessage> messages, float temperature, float topP, int n, boolean stream, String stop, Integer maxTokens, float presencePenalty, float frequencyPenalty, JsonObject logitBias, String user) {
        this.model = model;
        this.messages = new ArrayList<>(messages); // Use a mutable list
        this.temperature = temperature;
        this.topP = topP;
        this.n = n;
        this.stream = stream;
        this.stop = stop;
        this.maxTokens = maxTokens;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.logitBias = logitBias;
        this.user = user;
    }

    public String getModel() {
        return this.model;
    }

    public List<ChatMessage> getMessages() {
        return this.messages;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public float getTopP() {
        return this.topP;
    }

    public int getN() {
        return this.n;
    }

    public boolean getStream() {
        return this.stream;
    }

    public String getStop() {
        return this.stop;
    }

    public Integer getMaxTokens() {
        return this.maxTokens;
    }

    public float getPresencePenalty() {
        return this.presencePenalty;
    }

    public float getFrequencyPenalty() {
        return this.frequencyPenalty;
    }

    public JsonObject getLogitBias() {
        return this.logitBias;
    }

    public String getUser() {
        return this.user;
    }
}