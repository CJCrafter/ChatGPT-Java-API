package com.cjcrafter.openai.assistants

import com.fasterxml.jackson.annotation.JsonProperty

data class ListAssistantResponse(
    @JsonProperty(required = true) val data: List<Assistant>,
    @JsonProperty("first_id", required = true) val firstId: String,
    @JsonProperty("last_id", required = true) val lastId: String,
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
)
