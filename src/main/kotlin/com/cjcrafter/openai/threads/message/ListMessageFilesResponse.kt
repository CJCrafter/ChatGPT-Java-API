package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

data class ListMessageFilesResponse(
    @JsonProperty(required = true) val data: List<MessageFile>,
    @JsonProperty("first_id", required = true) val firstId: String,
    @JsonProperty("last_id", required = true) val lastId: String,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)
