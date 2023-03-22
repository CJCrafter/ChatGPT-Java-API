package com.cjcrafter.openai.chat

import com.google.gson.JsonObject

/**
 * The [ChatResponse] contains all the data returned by the OpenAI Chat API.
 * For most use cases, [ChatResponse.get] (passing 0 to the index argument) is
 * all you need.
 *
 * @property id
 * @property object
 * @property created
 * @property choices
 * @property usage
 * @constructor Create empty Chat response
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
     *
     * @param index The index of the message (`0` for most use cases).
     * @return The generated [ChatChoice] at the index.
     */
    operator fun get(index: Int): ChatChoice {
        return choices[index]
    }
}