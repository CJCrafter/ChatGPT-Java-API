package com.cjcrafter.openai.chat

import com.cjcrafter.openai.FinishReason
import com.google.gson.JsonObject

/**
 *
 * The OpenAI API returns a list of [ChatChoiceChunk]. The "new content" is
 * saved to the [delta] property. To access everything that is currently
 * generated, use [message].
 *
 * By default, only 1 [ChatChoiceChunk] is generated (since [ChatRequest.n] == 1).
 * When you increase `n`, more options are generated. The more options you
 * generate, the more tokens you use. In general, it is best to **ONLY**
 * generate 1 response, and to let the user regenerate the response.
 *
 * @property index        The index in the array... 0 if [ChatRequest.n]=1.
 * @property message      All tokens that are currently generated.
 * @property delta        The newly generated tokens (*can be empty!*)
 * @property finishReason The reason the bot stopped generating tokens.
 * @constructor Create a new chat choice, for internal usage.
 * @see FinishReason
 * @see ChatChoice
 */
data class ChatChoiceChunk(val index: Int, val message: ChatMessage, var delta: String, var finishReason: FinishReason?) {

    /**
     * JSON constructor for internal usage.
     */
    constructor(json: JsonObject) : this(

        // The first message from ChatGPT looks like this:
        // data: {"id":"chatcmpl-6xUB4Vi8jEG8u4hMBTMeO8KXgA87z","object":"chat.completion.chunk","created":1679635374,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"role":"assistant"},"index":0,"finish_reason":null}]}
        // So the only data we have so far is that ChatGPT will be responding.
        json["index"].asInt,
        ChatMessage(ChatUser.ASSISTANT, ""),
        "",
        null
    )

    internal fun update(json: JsonObject)  {
        val deltaJson = json["delta"].asJsonObject
        delta = if (deltaJson.has("content")) deltaJson["content"].asString else ""
        message.content += delta
        finishReason = if (json["finish_reason"].isJsonNull) null else FinishReason.valueOf(json["finish_reason"].asString.uppercase())
    }
}

/*
Below is a potential Steam response from OpenAI. You can see that the first
message contains 0 generated content, and the last message (before "[DONE]")
adds the finish_reason.

data: {"id":"chatcmpl-6xUB4Vi8jEG8u4hMBTMeO8KXgA87z","object":"chat.completion.chunk","created":1679635374,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"role":"assistant"},"index":0,"finish_reason":null}]}

data: {"id":"chatcmpl-6xUB4Vi8jEG8u4hMBTMeO8KXgA87z","object":"chat.completion.chunk","created":1679635374,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"content":"Hello"},"index":0,"finish_reason":null}]}

data: {"id":"chatcmpl-6xUB4Vi8jEG8u4hMBTMeO8KXgA87z","object":"chat.completion.chunk","created":1679635374,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"content":" World"},"index":0,"finish_reason":null}]}

data: {"id":"chatcmpl-6xUB4Vi8jEG8u4hMBTMeO8KXgA87z","object":"chat.completion.chunk","created":1679635374,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"content":"."},"index":0,"finish_reason":null}]}

data: {"id":"chatcmpl-6xUB4Vi8jEG8u4hMBTMeO8KXgA87z","object":"chat.completion.chunk","created":1679635374,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{},"index":0,"finish_reason":"stop"}]}

data: [DONE]
 */