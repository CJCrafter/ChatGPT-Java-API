package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a file attached to a [ThreadMessage].
 *
 * @property id The ID of the file, which can be used in endpoints
 * @property createdAt The timestamp of when the file was created
 * @property messageId The ID of the message that this file is attached to
 */
data class MessageFile(
    @JsonProperty(required = true) val id: String,
    @JsonProperty("created_at", required = true) val createdAt: Int,
    @JsonProperty("message_id", required = true) val messageId: String,
)
