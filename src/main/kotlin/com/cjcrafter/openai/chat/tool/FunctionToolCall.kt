package com.cjcrafter.openai.chat.tool

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a tool call for [Tool.Type.FUNCTION].
 *
 * @property id The unique id of the tool call. You should use this id in your reply.
 * @property function The details about which function was called, parameters, etc.
 */
data class FunctionToolCall(
    @JsonProperty(required = true) override val id: String,
    @JsonProperty(required = true) val function: FunctionCall,
) : ToolCall() {
    override val type = Tool.Type.FUNCTION

    override fun update(delta: ToolCallDelta) {
        if (delta.function != null)
            function.update(delta.function)
    }
}