package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.chat.ChatUser
import com.fasterxml.jackson.annotation.JsonProperty

data class ChatMessageDelta(
    val role: ChatUser? = null,
    val content: String? = null,
    @JsonProperty("tool_calls") val toolCalls: List<ToolCallDelta>? = null,
) {
}