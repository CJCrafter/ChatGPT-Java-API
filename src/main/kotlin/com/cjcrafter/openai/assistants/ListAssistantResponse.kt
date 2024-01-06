package com.cjcrafter.openai.assistants

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents a response from the OpenAI API containing a list of [Assistant]s.
 *
 * When the list is empty, [firstId] and [lastId] will be `null`.
 *
 * @property data The list of assistants
 * @property firstId The ID of the first [Assistant] in the list, or null
 * @property lastId The ID of the last [Assistant] in the list, or null
 * @property hasMore Whether there are more [Assistant]s to retrieve from the API
 */
data class ListAssistantResponse(
    @JsonProperty(required = true) val data: List<Assistant>,
    @JsonProperty("first_id") val firstId: String?,
    @JsonProperty("last_id") val lastId: String?,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)
