package com.cjcrafter.openai.chat

import com.google.gson.JsonObject

/**
 * This is the object returned from the API. You want to access choices[0]
 * to get your response.
 */
class ChatResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val choices: List<ChatChoice>,
    val usage: ChatUsage
) {

    /**
     * JSON constructor for internal usage.
     */
    constructor(json: JsonObject) : this(
        json["id"].asString,
        json["object"].asString,
        json["created"].asLong,
        json["choices"].asJsonArray.map { ChatChoice(it.asJsonObject) },
        ChatUsage(json["usage"].asJsonObject)
    )

    /**
     * Shorthand for accessing the generated messages (shorthand for
     * [ChatResponse.choices]).
     */
    operator fun get(index: Int): ChatChoice {
        return choices[index]
    }
}