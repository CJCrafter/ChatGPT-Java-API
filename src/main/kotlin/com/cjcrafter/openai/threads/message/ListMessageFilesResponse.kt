package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents a response from the OpenAI API containing a list of [MessageFile]s.
 *
 * When the list is empty, [firstId] and [lastId] will be `null`.
 *
 * @property data The list of [MessageFile]s
 * @property firstId The ID of the first [MessageFile] in the list, or null
 * @property lastId The ID of the last [MessageFile] in the list, or null
 * @property hasMore Whether there are more [MessageFile]s to retrieve from the API
 */
data class ListMessageFilesResponse(
    @JsonProperty(required = true) val data: List<MessageFile>,
    @JsonProperty("first_id") val firstId: String?,
    @JsonProperty("last_id") val lastId: String?,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)
