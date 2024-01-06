package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents a response from the OpenAI API containing a list of [ThreadMessage]s.
 *
 * If the list is empty, [firstId] and [lastId] will be `null`.
 *
 * @property data The list of thread messages
 * @property firstId The id of the first message in the list, or null
 * @property lastId The id of the last message in the list, or null
 * @property hasMore Whether there are more messages to retrieve
 */
data class ListThreadMessagesResponse(
    @JsonProperty(required = true) val data: List<ThreadMessage>,
    @JsonProperty("first_id") val firstId: String?,
    @JsonProperty("last_id") val lastId: String?,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)