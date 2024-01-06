package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a message in a [Thread].
 *
 * @property id The unique id of the message
 * @property createdAt The unix timestamp of when the message was created
 * @property threadId The id of the thread that this message belongs to
 * @property role The role of the user that created the message
 * @property content The content of the message
 * @property assistantId The id of the assistant that this message belongs to, or null
 * @property runId The id of the run that created this message, or null
 * @property fileIds The ids of the files attached to this message
 * @property metadata The metadata attached to this message
 */
data class ThreadMessage(
    @JsonProperty(required = true) val id: String,
    @JsonProperty("created_at", required = true) val createdAt: Int,
    @JsonProperty("thread_id", required = true) val threadId: String,
    @JsonProperty(required = true) val role: ThreadUser,
    @JsonProperty(required = true) val content: List<ThreadMessageContent>,
    @JsonProperty("assistant_id") val assistantId: String?,
    @JsonProperty("run_id") val runId: String?,
    @JsonProperty("file_ids", required = true) val fileIds: List<String>,
    @JsonProperty(required = true) val metadata: Map<String, String>,
)
