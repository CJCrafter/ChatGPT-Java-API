package com.cjcrafter.openai.assistants

import com.cjcrafter.openai.chat.tool.Tool
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the request body for modifying an assistant. Modifications work
 * by overriding the previous value. If this request has a non-null value for
 * a property, then the value currently stored in the assistant will be replaced.
 *
 * If you want to add a tool, file, or metadata without overriding the previous
 * value, then you must:
 * 1. Get the current values of the assistant
 * 2. Add the new tools/files/metadata
 * 3. Send a [ModifyAssistantRequest] with the new values
 *
 * Be careful when modifying the instance variables. Try to use the [builder]
 * method instead, as it will detect common errors before sending your request.
 *
 * @property model The new model to use for the assistant
 * @property name The new name of the assistant
 * @property description The new description of the assistant
 * @property instructions The new instructions for the assistant to follow
 * @property tools The new tools available to the assistant
 * @property fileIds The new files that can be used in tools
 * @property metadata The new custom metadata map
 */
data class ModifyAssistantRequest(
    var model: String? = null,
    var name: String? = null,
    var description: String? = null,
    var instructions: String? = null,
    var tools: MutableList<Tool>? = null,
    @JsonProperty("file_ids") var fileIds: MutableList<String>? = null,
    var metadata: MutableMap<String, String>? = null,
) {

    class Builder internal constructor() : AbstractAssistantBuilder<ModifyAssistantRequest>() {

        /**
         * Builds the [ModifyAssistantRequest] instance.
         *
         * @throws IllegalStateException if no properties are set. At least 1 should be set.
         */
        override fun build(): ModifyAssistantRequest {
            // It doesn't make sense to build a ModifyAssistantRequest without any modifications
            if (model == null && name == null && description == null && instructions == null && tools == null && fileIds == null && metadata == null)
                throw IllegalStateException("At least one property must be set")

            return ModifyAssistantRequest(
                model = model,
                name = name,
                description = description,
                instructions = instructions,
                tools =  tools,
                fileIds = fileIds,
                metadata = metadata,
            )
        }
    }

    companion object {

        /**
         * Instantiates a new [ModifyAssistantRequest] builder instance. Make sure
         * you set **at least** 1 property before calling [build].
         */
        @JvmStatic
        fun builder() = Builder()
    }
}