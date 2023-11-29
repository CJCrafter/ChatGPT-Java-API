package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.threads.Thread
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a user that sends messages in a [Thread].
 */
enum class ThreadUser {

    /**
     * Marks messages from the user, or the client.
     */
    @JsonProperty("user")
    USER,

    /**
     * Marks messages from the assistant, or the bot.
     */
    @JsonProperty("assistant")
    ASSISTANT,
}