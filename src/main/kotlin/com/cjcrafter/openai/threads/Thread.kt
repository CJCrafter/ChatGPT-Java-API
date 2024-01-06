package com.cjcrafter.openai.threads

import com.cjcrafter.openai.assistants.Assistant
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a thread object returned by the OpenAI API. Threads are objects
 * that contain a list of messages that can interacted with by an [Assistant].
 *
 * @property id The id of the thread
 * @property createdAt The unix timestamp of when the thread was created
 * @property metadata The metadata associated with the thread
 */
data class Thread(
    @JsonProperty(required = true) val id: String,
    @JsonProperty("created_at", required = true) val createdAt: Int,
    @JsonProperty(required = true) val metadata: Map<String, String>,
)
