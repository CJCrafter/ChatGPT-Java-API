package com.cjcrafter.openai.chat

import com.fasterxml.jackson.annotation.JsonProperty

class ChatResponseFormat(@JsonProperty("type") val type: Type = Type.TEXT) {

    enum class Type {
        @JsonProperty("json_object")
        JSON,
        @JsonProperty("text")
        TEXT
    }
}