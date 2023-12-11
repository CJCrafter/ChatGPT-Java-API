package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.chat.tool.ToolCall

data class ToolCallsDetails(
    val toolCalls: List<ToolCall>
) : RunStep.Details() {
    override val type = RunStep.Type.TOOL_CALLS
}