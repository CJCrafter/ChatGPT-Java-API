package com.cjcrafter.openai.threads.message

data class ThreadMessage(
    val id: String,
    val createdAt: Int,
    val threadId: String,
    val role: ThreadUser,
    val content: List<ThreadMessageContent>,
    val assistantId: String?,
    val runId: String?,
    val fileIds: List<String>,
    val metadata: Map<String, String>,
) {

}
