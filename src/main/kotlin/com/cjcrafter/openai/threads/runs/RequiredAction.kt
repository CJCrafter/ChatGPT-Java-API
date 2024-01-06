package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.chat.tool.ToolCall
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Represents a required action for a run. This is used when the Assistant
 * requests tool calls. In this future, this may be used for other required
 * actions as well.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(RequiredAction.SubmitToolCallAction::class, name = "submit_tool_outputs"),
)
sealed class RequiredAction {

    /**
     * Returns the type of the required action. This is effectively the same as
     * checking the type via [Class.isInstance] (or `instanceof` in java, `is`
     * in Kotlin).
     */
    abstract val type: Type

    /**
     * Represents the type of the required action.
     */
    enum class Type {

        /**
         * Used when the Assistant requests a tool call.
         */
        @JsonProperty("submit_tool_calls")
        SUBMIT_TOOL_CALLS,
    }

    /**
     * Represents a request by the Assistant for a list of tool calls.
     *
     * @property toolCalls The list of tool calls
     */
    data class SubmitToolCallAction(
        @JsonProperty("tool_calls", required = true) val toolCalls: List<ToolCall>,
    ): RequiredAction() {
        override val type = Type.SUBMIT_TOOL_CALLS
    }
}