package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.assistants.Assistant
import com.cjcrafter.openai.chat.tool.Tool
import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents a request to create a [Run].
 *
 * @property assistantId The ID of the Assistant to use for the run
 * @property model The ID of the model to override the assistant's default model
 * @property instructions The instructions to override the assistant's default instructions
 * @property tools The tools to override the assistant's default tools
 * @property metadata The metadata to associate with the run
 */
data class CreateRunRequest(
    @JsonProperty("assistant_id") var assistantId: String,
    var model: String? = null,
    var instructions: String? = null,
    var tools: List<Tool>? = null,
    var metadata: Map<String, String>? = null,
) {

    @OpenAIDslMarker
    class Builder internal constructor() {
        private var assistantId: String? = null
        private var model: String? = null
        private var instructions: String? = null
        private var tools: MutableList<Tool>? = null
        private var metadata: MutableMap<String, String>? = null

        fun assistant(assistant: Assistant) = apply { this.assistantId = assistant.id }
        fun assistantId(assistantId: String) = apply { this.assistantId = assistantId }
        fun model(model: String) = apply { this.model = model }
        fun instructions(instructions: String) = apply { this.instructions = instructions }
        fun tools(tools: MutableList<Tool>) = apply { this.tools = tools }
        fun metadata(metadata: MutableMap<String, String>) = apply { this.metadata = metadata }

        fun build() = CreateRunRequest(
            assistantId = assistantId ?: throw IllegalStateException("assistantId must be set"),
            model = model,
            instructions = instructions,
            tools = tools,
            metadata = metadata,
        )
    }

    companion object {

        /**
         * Creates a new [Builder] for [CreateRunRequest].
         */
        fun builder() = Builder()
    }
}