package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.chat.tool.Tool
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a run object returned by the OpenAI API. The run object itself
 * isn't highly useful for most applications, but it is used to retrieve the
 * messages from the run via [RunStep]s.
 *
 * @property id The unique id of the run, used for retrieving the run
 * @property createdAt The unix timestamp of when the run was created
 * @property threadId The id of the thread that the run belongs to
 * @property assistantId The id of the assistant which is handling the run
 * @property status The current status of the run
 * @property requiredAction The required action for the run, if any
 * @property lastError The last error associated with the run, if any
 * @property expiresAt The unix timestamp of when the run will expire
 * @property startedAt The unix timestamp of when the run started
 * @property cancelledAt The unix timestamp of when the run was cancelled
 * @property failedAt The unix timestamp of when the run failed
 * @property completedAt The unix timestamp of when the run completed
 * @property model The model used for the run
 * @property instructions The instructions used for the run
 * @property tools The tools used for the run
 * @property fileIds The file ids used for the run
 * @property metadata The metadata associated with the run
 */
data class Run(
    @JsonProperty(required = true) val id: String,
    @JsonProperty("created_at", required = true) val createdAt: Int,
    @JsonProperty("thread_id", required = true) val threadId: String,
    @JsonProperty("assistant_id", required = true) val assistantId: String,
    @JsonProperty(required = true) val status: RunStatus,
    @JsonProperty("required_action") val requiredAction: RequiredAction?,
    @JsonProperty("last_error") val lastError: RunError?,
    @JsonProperty("expires_at", required = true) val expiresAt: Int,
    @JsonProperty("started_at") val startedAt: Int?,
    @JsonProperty("cancelled_at") val cancelledAt: Int?,
    @JsonProperty("failed_at") val failedAt: Int?,
    @JsonProperty("completed_at") val completedAt: Int?,
    @JsonProperty(required = true) val model: String,
    @JsonProperty(required = true) val instructions: String,
    @JsonProperty(required = true) val tools: List<Tool>,
    @JsonProperty("file_ids", required = true) val fileIds: List<String>,
    @JsonProperty(required = true) val metadata: Map<String, String>,
)
