package com.cjcrafter.openai.chat

import com.google.gson.annotations.SerializedName

/**
 * ChatGPT's biggest innovation is its conversational memory. To remember the
 * conversation, we need to map each message to who sent it. This enum stores
 * the 3 possible users.
 */
enum class ChatUser {

    /**
     * Before the conversation between the [USER] and the [ASSISTANT] starts,
     * there is a [SYSTEM] message to *"prime"* the AI. System messages are
     * almost like a script given to the AI to follow throughout the entire
     * conversation. In other words, system messages help the AI "get in the
     * mood" for the conversation.
     *
     * [SYSTEM] should only send 1 message, and it should be the first message
     * in the conversation.
     *
     * @see <a href="https://github.com/f/awesome-chatgpt-prompts">System Message Examples/Ideas</a>
     */
    @SerializedName("system")
    SYSTEM,

    /**
     * [USER] is the human that is asking the questions. After a message from
     * the user, you should lock the conversation until [ASSISTANT] replies to
     * the user's message.
     */
    @SerializedName("user")
    USER,

    /**
     * [ASSISTANT] is the AI that generates responses.
     */
    @SerializedName("assistant")
    ASSISTANT,

    @SerializedName("tool")
    TOOL,

    @SerializedName("function")
    @Deprecated("functions have been replaced by tools")
    FUNCTION;
}