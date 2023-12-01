package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the response to a specific tool call. This is used when submitting tool outputs
 * back to the [RunHandler].
 *
 * @property toolCallId The ID of the tool call.
 * @property output The output of the tool call, usually as a JSON string.
 */
data class ToolCallOutputs(
    @JsonProperty("tool_call_id") var toolCallId: String,
    var output: String,
) {
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var toolCallId: String? = null
        private var output: String? = null

        fun toolCallId(toolCallId: String) = apply { this.toolCallId = toolCallId }
        fun output(output: String) = apply { this.output = output }

        fun build() = ToolCallOutputs(
            toolCallId ?: throw IllegalStateException("toolCallId must be set"),
            output ?: throw IllegalStateException("output must be set")
        )
    }

    companion object {

        /**
         * Returns a new [Builder] instance for building [ToolCallOutputs] objects.
         */
        @JvmStatic
        fun builder() = Builder()
    }
}