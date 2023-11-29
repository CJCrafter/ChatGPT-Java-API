package com.cjcrafter.openai.assistants

import com.cjcrafter.openai.chat.tool.Tool
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the request body for creating an assistant.
 *
 * Be careful when modifying the instance variables. Try to use the [builder]
 * method instead, as it will detect common errors before sending your request
 * to the OpenAI API.
 *
 * @property model The id of the model to use for the assistant
 * @property name The name of the assistant
 * @property description The description of the assistant
 * @property instructions The instructions for the assistant to follow
 * @property tools The tools available to the assistant
 * @property fileIds The files that can be used in tools
 * @property metadata A custom metadata map
 */
data class CreateAssistantRequest internal constructor(
    var model: String,
    var name: String? = null,
    var description: String? = null,
    var instructions: String? = null,
    var tools: MutableList<Tool>? = null,
    @JsonProperty("file_ids") var fileIds: MutableList<String>? = null,
    var metadata: MutableMap<String, String>? = null,
) {

    class Builder internal constructor() : AbstractAssistantBuilder<CreateAssistantRequest>() {

        /**
         * Builds the [CreateAssistantRequest] instance.
         *
         * @throws IllegalStateException if required properties were not set.
         */
        override fun build(): CreateAssistantRequest {
            return CreateAssistantRequest(
                model = model ?: throw IllegalStateException("model must be set"),
                name = name,
                description = description,
                instructions = instructions,
                tools = tools,
                fileIds = fileIds,
                metadata = metadata,
            )
        }
    }

    companion object {

        /**
         * Instantiates a new [CreateAssistantRequest] builder instance. Make sure you set
         * the required value(s):
         * - [model]
         */
        @JvmStatic
        fun builder() = Builder()
    }
}
