package com.cjcrafter.openai.chat

import com.cjcrafter.openai.FinishReason
import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.chat.tool.ChatMessageDelta
import com.cjcrafter.openai.chat.tool.ToolCall
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.lang.IllegalArgumentException

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
    var delta: ChatMessageDelta? = null,
    @JsonProperty("finish_reason") var finishReason: FinishReason?
) {
    val message: ChatMessage = ChatMessage(delta?.role!!, delta?.content, delta?.toolCalls?.map { it.toToolCall() })

    // Reads the json from the OpenAI API, and sets delta. message accumulates changes
    internal fun update(json: JsonNode) {
        val deltaNode = json.get("delta") ?: throw IllegalArgumentException("Passed a json without delta")
        val delta = OpenAI.createObjectMapper().treeToValue(deltaNode, ChatMessageDelta::class.java)

        // The "bread and butter" of streaming. You can start showing the user
        // generated content usually *within a second*. However, for Tool Calls,
        // this will always be null.
        if (message.content == null)
            message.content = delta?.content // Always null for tool calls
        else if (delta.content != null)
            message.content += delta.content

        // Handle updating the tool call
        if (message.toolCalls != null && delta.toolCalls != null) {
            for (deltaToolCall in delta.toolCalls) {
                val toolCall = message.toolCalls!![deltaToolCall.index]
                toolCall.update(deltaToolCall)
            }
        }

        // The reason the bot stopped generating tokens. This is null until done.
        finishReason = json.get("finish_reason")?.let { if (it.isNull) null else FinishReason.valueOf(it.asText().uppercase()) }

        // People can manually check changes instead of using this API's
        // accumulative changes.
        this.delta = delta
    }

    /**
     * Returns `true` if this message chunk is complete. Once complete, no more
     * tokens will be generated, and [ChatChoiceChunk.message] will contain the
     * complete message.
     */
    fun isFinished() = finishReason != null
}
