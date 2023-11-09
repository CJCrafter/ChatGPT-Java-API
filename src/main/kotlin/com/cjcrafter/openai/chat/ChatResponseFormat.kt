package com.cjcrafter.openai.chat

import com.google.gson.annotations.SerializedName

class ChatResponseFormat {

    enum class Type {
        @SerializedName("json")
        JSON,
        @SerializedName("text")
        TEXT
    }
}