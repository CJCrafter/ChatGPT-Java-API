package com.cjcrafter.openai.chat

/**
 * ChatGPT's biggest innovation is its conversation memory. To remember the
 * conversation, we need to map each message to who sent it. This data class
 * wraps a message with the user who sent the message.
 *
 * @property role The user who sent this message.
 * @property content The string content of the message.
 * @see ChatUser
 */
data class ChatMessage(var role: ChatUser, var content: String) {

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
    }
}