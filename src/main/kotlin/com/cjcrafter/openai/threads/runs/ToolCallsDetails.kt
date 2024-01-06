package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.chat.tool.ToolCall
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contains the list of all tool calls made during a run step.
 *
 * @property toolCalls All tool calls made during this step.
 */
data class ToolCallsDetails(
    @JsonProperty("tool_calls", required = true) val toolCalls: List<ToolCall>
) : RunStep.Details() {
    override val type = RunStep.Type.TOOL_CALLS
}