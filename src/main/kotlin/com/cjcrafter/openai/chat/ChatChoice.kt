package com.cjcrafter.openai.chat

import com.google.gson.JsonObject

/**
 * Holds the data for 1 generated text completion. For most use cases, only 1
 * [ChatChoice] is generated.
 *
 * @param index        The index in the array... 0 if [ChatRequest.n]=1.
 * @param message      The generated text.
 * @param finishReason Why did the bot stop generating tokens?
 */
class ChatChoice(val index: Int, val message: ChatMessage, val finishReason: String) {

    /**
     * JSON constructor for internal use.
     */
    constructor(json: JsonObject) : this(
        json["index"].asInt,
        ChatMessage(json["message"].asJsonObject),
        json["finish_reason"].toString()
    )
}