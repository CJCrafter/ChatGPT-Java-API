package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.chat.ChatUser
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the "delta," or changes of a chat message. This is used by streams
 * stream 1 token at a time.
 *
 * @property role Who sent the message. Will always be [ChatUser.ASSISTANT] for the first message, then `null`
 * @property content 1 token of the message. Will always be `null` for tool calls
 * @property toolCalls Modifications to the tool calls. Will always be `null` when content is not `null`
 */
data class ChatMessageDelta(
    val role: ChatUser? = null,
    val content: String? = null,
    @JsonProperty("tool_calls") val toolCalls: List<ToolCallDelta>? = null,
)