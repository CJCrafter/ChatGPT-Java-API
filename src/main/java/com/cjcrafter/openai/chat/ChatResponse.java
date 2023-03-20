package com.cjcrafter.openai.chat;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This is the object returned from the API. You want to access choices[0]
 * to get your response.
 */
public final class ChatResponse {

    private final String id;
    private final String object;
    private final long created;
    private final List<ChatChoice> choices;
    private final ChatUsage usage;

    public ChatResponse(String id, String object, long created, List<ChatChoice> choices, ChatUsage usage) {
        super();
        this.id = id;
        this.object = object;
        this.created = created;
        this.choices = choices;
        this.usage = usage;
    }

    public ChatResponse(JsonObject json) {
        this(json.get("id").getAsString(), json.get("object").getAsString(), json.get("created").getAsLong(), StreamSupport.stream(json.get("choices").getAsJsonArray().spliterator(), false).map(element -> new ChatChoice(element.getAsJsonObject())).collect(Collectors.toList()), new ChatUsage(json.get("usage").getAsJsonObject()));
    }

    public String getId() {
        return this.id;
    }

    public String getObject() {
        return this.object;
    }

    public long getCreated() {
        return this.created;
    }

    public List<ChatChoice> getChoices() {
        return this.choices;
    }

    public ChatUsage getUsage() {
        return this.usage;
    }
}