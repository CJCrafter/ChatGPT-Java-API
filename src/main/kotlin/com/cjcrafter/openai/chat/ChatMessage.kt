package com.cjcrafter.openai.chat

import com.cjcrafter.openai.chat.tool.ToolCall
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * ChatGPT's biggest innovation is its conversation memory. To remember the
 * conversation, we need to map each message to who sent it. This data class
 * wraps a message with the user who sent the message.
 *
 * @property role The user who sent this message.
 * @property content The string content of the message.
 * @see ChatUser
 */
data class ChatMessage @JvmOverloads constructor(
    var role: ChatUser,
    @JsonInclude var content: String?, // JsonInclude here is important for tools
    @JsonProperty("tool_calls") var toolCalls: List<ToolCall>? = null,
    @JsonProperty("tool_call_id") var toolCallId: String? = null,
) {
    init {
        if (role == ChatUser.TOOL) {
            requireNotNull(toolCallId) { "toolCallId must be set when role is TOOL" }
        }
    }

    companion object {

        /**
         * Returns a new [ChatMessage] using [ChatUser.SYSTEM].
         */
        @JvmStatic
        fun String.toSystemMessage(): ChatMessage {
            return ChatMessage(ChatUser.SYSTEM, this)
        }

        /**
         * Returns a new [ChatMessage] using [ChatUser.USER].
         */
        @JvmStatic
        fun String.toUserMessage(): ChatMessage {
            return ChatMessage(ChatUser.USER, this)
        }

        /**
         * Returns a new [ChatMessage] using [ChatUser.ASSISTANT].
         */
        @JvmStatic
        fun String.toAssistantMessage(): ChatMessage {
            return ChatMessage(ChatUser.ASSISTANT, this)
        }

        /**
         * Returns a new [ChatMessage] using [ChatUser.TOOL].
         */
        @JvmStatic
        fun String.toToolMessage(toolCallId: String): ChatMessage {
            return ChatMessage(ChatUser.TOOL, this, toolCallId = toolCallId)
        }
    }
}