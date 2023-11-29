package com.cjcrafter.openai.assistants

import com.cjcrafter.openai.chat.tool.Tool
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the assistant metadata returned by the OpenAI API.
 *
 * @property id The unique id of the assistant, always starts with asst_
 * @property createdAt The unix timestamp of when the assistant was created
 * @property name The name of the assistant, if present
 * @property description The description of the assistant, if present
 * @property model The model used by the assistant
 * @property instructions The instructions for the assistant, if present
 * @property tools The tools used by the assistant
 * @property fileIds The list of file ids used in tools
 * @property metadata Data stored by YOU, the developer, to store additional information
 */
data class Assistant(
    @JsonProperty(required = true) val id: String,
    @JsonProperty("created_at", required = true) val createdAt: Int,
    @JsonProperty(required = true) val name: String?,
    @JsonProperty(required = true) val description: String?,
    @JsonProperty(required = true) val model: String,
    @JsonProperty(required = true) val instructions: String?,
    @JsonProperty(required = true) val tools: List<Tool>,
    @JsonProperty("file_ids", required = true) val fileIds: List<String>,
    @JsonProperty(required = true) val metadata: Map<String, String>,
)
