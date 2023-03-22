package com.cjcrafter.openai.chat

import com.google.gson.JsonObject

/**
 * The OpenAI API returns a list of [ChatChoice]. Each chat choice has a
 * generated message ([ChatChoice.message]) and a finish reason
 * ([ChatChoice.finishReason]). For most use cases, you only need the generated
 * message.
 *
 * By default, only 1 [ChatChoice] is generated (since [ChatRequest.n] == 1).
 * When you increase `n`, more options are generated. The more options you
 * generate, the more tokens you use. In general, it is best to **ONLY**
 * generate 1 response, and to let the user regenerate the response.
 *
 * @param index        The index in the array... 0 if [ChatRequest.n]=1.
 * @param message      The generated text.
 * @param finishReason Why did the bot stop generating tokens?
 * @see FinishReason
 */
class ChatChoice(val index: Int, val message: ChatMessage, val finishReason: FinishReason?) {

    /**
     * JSON constructor for internal use.
     */
    constructor(json: JsonObject) : this(
        json["index"].asInt,
        ChatMessage(json["message"].asJsonObject),
        if (json["finish_reason"].isJsonNull) null else FinishReason.valueOf(json["finish_reason"].asString.uppercase())
    )
}