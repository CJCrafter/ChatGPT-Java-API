package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.chat.tool.Tool
import com.fasterxml.jackson.annotation.JsonProperty

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
