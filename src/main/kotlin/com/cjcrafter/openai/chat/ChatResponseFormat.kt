package com.cjcrafter.openai.chat

import com.fasterxml.jackson.annotation.JsonProperty

class ChatResponseFormat {

    enum class Type {
        @JsonProperty("json")
        JSON,
        @JsonProperty("text")
        TEXT
    }
}