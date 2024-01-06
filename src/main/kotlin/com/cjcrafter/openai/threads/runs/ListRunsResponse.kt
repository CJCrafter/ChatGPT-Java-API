package com.cjcrafter.openai.threads.runs

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents a response from the OpenAI API containing a list of [Run]s.
 *
 * When the list is empty, [firstId] and [lastId] will be `null`.
 *
 * @property data The list of [Run]s
 * @property firstId The ID of the first [Run] in the list, or null
 * @property lastId The ID of the last [Run] in the list, or null
 * @property hasMore Whether there are more [Run]s to retrieve from the API
 */
data class ListRunsResponse(
    @JsonProperty(required = true) val data: List<Run>,
    @JsonProperty("first_id") val firstId: String?,
    @JsonProperty("last_id") val lastId: String?,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)
