package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.threads.Thread
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a response from the [ListThreadMessagesRequest]. This contains a
 * list of [ThreadMessage]s. If you are trying to get **every** message in a
 * [Thread], you might need to make multiple requests.
 *
 * @property data The list of thread messages
 * @property firstId The id of the first message in the list
 * @property lastId The id of the last message in the list
 * @property hasMore Whether there are more messages to retrieve
 */
data class ListThreadMessagesResponse(
    @JsonProperty(required = true) val data: List<ThreadMessage>,
    @JsonProperty("first_id", required = true) val firstId: String,
    @JsonProperty("last_id", required = true) val lastId: String,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)