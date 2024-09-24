package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.assistants.Assistant
import com.cjcrafter.openai.chat.tool.Tool
import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.annotations.Contract

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
    var stream: Boolean? = null,
) {

    /**
     * Builder for [CreateRunRequest].
     */
    @OpenAIDslMarker
    class Builder internal constructor() {

        private var assistantId: String? = null
        private var model: String? = null
        private var instructions: String? = null
        private var tools: MutableList<Tool>? = null
        private var metadata: MutableMap<String, String>? = null

        /**
         * Sets the assistant to use for the run. Shorthand for [assistantId].
         */
        fun assistant(assistant: Assistant) = apply { this.assistantId = assistant.id }

        /**
         * Sets the assistant to use for the run.
         */
        fun assistantId(assistantId: String) = apply { this.assistantId = assistantId }

        /**
         * Sets the model to override the assistant's default model.
         */
        fun model(model: String) = apply { this.model = model }

        /**
         * Sets the instructions to override the assistant's default instructions.
         */
        fun instructions(instructions: String) = apply { this.instructions = instructions }

        /**
         * Sets the tools to override the assistant's default tools.
         */
        fun tools(tools: MutableList<Tool>) = apply { this.tools = tools }

        /**
         * Sets the metadata to attach to the [Run].
         */
        fun metadata(metadata: MutableMap<String, String>) = apply { this.metadata = metadata }

        /**
         * Builds the [CreateRunRequest].
         */
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
        @JvmStatic
        @Contract(pure = true)
        fun builder() = Builder()
    }
}