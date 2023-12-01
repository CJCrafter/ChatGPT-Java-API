package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonProperty

data class SubmitToolOutputs(
    @JsonProperty("tool_outputs") var toolOutputs: MutableList<ToolCallOutputs>,
) {
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var toolOutputs: MutableList<ToolCallOutputs>? = null

        fun toolOutputs(toolOutputs: MutableList<ToolCallOutputs>) = apply { this.toolOutputs = toolOutputs }

        fun addToolOutput(toolCallOutputs: ToolCallOutputs) = apply {
            if (toolOutputs == null) toolOutputs = mutableListOf()
            toolOutputs!!.add(toolCallOutputs)
        }

        fun build() = SubmitToolOutputs(
            toolOutputs ?: throw IllegalStateException("toolOutputs must be set")
        )
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()
    }
}