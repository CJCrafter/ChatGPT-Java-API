package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

data class ThreadMessage(
    @JsonProperty(required = true) val id: String,
    @JsonProperty("created_at", required = true) val createdAt: Int,
    @JsonProperty("thread_id", required = true) val threadId: String,
    @JsonProperty(required = true) val role: ThreadUser,
    @JsonProperty(required = true) val content: List<ThreadMessageContent>,
    @JsonProperty("assistant_id", required = true) val assistantId: String?,
    @JsonProperty("run_id", required = true) val runId: String?,
    @JsonProperty("file_ids", required = true) val fileIds: List<String>,
    @JsonProperty(required = true) val metadata: Map<String, String>,
) {

}
