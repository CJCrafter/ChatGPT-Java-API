package com.cjcrafter.openai.chat

import com.google.gson.JsonObject

/**
 * ChatGPT's biggest innovation is its conversation memory. To remember the
 * conversation, we need to map each message to who sent it. This data class
 * wraps a message with the user who sent the message.
 *
 * Note that
 *
 * @property role The user who sent this message.
 * @property content The string content of the message.
 * @see ChatUser
 */
data class ChatMessage(var role: ChatUser, var content: String) {

    /**
     * JSON constructor for internal usage.
     */
    constructor(json: JsonObject) : this(ChatUser.valueOf(json["role"].asString.uppercase()), json["content"].asString)

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
            return ChatMessage(ChatUser.SYSTEM, this)
        }
    }
}