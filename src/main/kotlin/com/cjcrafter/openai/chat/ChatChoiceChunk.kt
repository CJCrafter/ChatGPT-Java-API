package com.cjcrafter.openai.chat

import com.cjcrafter.openai.FinishReason
import com.cjcrafter.openai.jackson.ChatChoiceChunkDeserializer
import com.cjcrafter.openai.jackson.ChatChoiceChunkSerializer
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

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
 * @since 1.2.0
 */
data class ChatChoiceChunk(
    val index: Int,
    val message: ChatMessage,
    var delta: String,
    @JsonProperty("finish_reason") var finishReason: FinishReason?
) {

    internal fun update(json: String) {
        val node: JsonNode = jacksonObjectMapper().readTree(json)
        val deltaNode = node.get("delta")
        delta = if (deltaNode?.has("content") == true && !deltaNode.get("content").isNull) {
            deltaNode.get("content").asText()
        } else {
            ""
        }

        message.content += delta
        finishReason = node.get("finish_reason")?.takeIf { !it.isNull }?.asText()?.let {
            FinishReason.valueOf(it.uppercase())
        }
    }

    /**
     * Returns `true` if this message chunk is complete. Once complete, no more
     * tokens will be generated, and [ChatChoiceChunk.message] will contain the
     * complete message.
     */
    fun isFinished() = finishReason != null

    companion object {
        @JvmStatic
        fun serializer() = ChatChoiceChunkSerializer()

        @JvmStatic
        fun deserializer() = ChatChoiceChunkDeserializer()
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