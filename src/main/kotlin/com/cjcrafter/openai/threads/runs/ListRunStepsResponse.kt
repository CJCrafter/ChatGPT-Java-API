package com.cjcrafter.openai.threads.runs

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents a response from the OpenAI API containing a list of [RunStep]s.
 *
 * @property data The list of [RunStep]s
 * @property firstId The ID of the first [RunStep] in the list
 * @property lastId The ID of the last [RunStep] in the list
 * @property hasMore Whether there are more [RunStep]s to retrieve from the API
 */
data class ListRunStepsResponse(
    @JsonProperty(required = true) val data: List<RunStep>,
    @JsonProperty("first_id", required = true) val firstId: String,
    @JsonProperty("last_id", required = true) val lastId: String,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)
