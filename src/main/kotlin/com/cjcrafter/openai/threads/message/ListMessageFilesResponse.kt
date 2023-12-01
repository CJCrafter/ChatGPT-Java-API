package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents a response from the OpenAI API containing a list of [MessageFile]s.
 *
 * @property data The list of [MessageFile]s
 * @property firstId The ID of the first [MessageFile] in the list
 * @property lastId The ID of the last [MessageFile] in the list
 * @property hasMore Whether there are more [MessageFile]s to retrieve from the API
 */
data class ListMessageFilesResponse(
    @JsonProperty(required = true) val data: List<MessageFile>,
    @JsonProperty("first_id", required = true) val firstId: String,
    @JsonProperty("last_id", required = true) val lastId: String,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)
