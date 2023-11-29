package com.cjcrafter.openai.assistants

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the result of a deletion request for an assistant.
 *
 * @property id The id of the assistant that was deleted
 * @property deleted Whether the assistant was deleted
 */
data class AssistantDeletionStatus(
    @JsonProperty(required = true) val id: String,
    @JsonProperty(required = true) val deleted: Boolean,
)
