package com.cjcrafter.openai.gson

import com.cjcrafter.openai.FinishReason
import com.cjcrafter.openai.chat.ChatChoiceChunk
import com.cjcrafter.openai.chat.ChatMessage
import com.cjcrafter.openai.chat.ChatUser
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class ChatChoiceChunkAdapter : TypeAdapter<ChatChoiceChunk?>() {

    override fun write(writer: JsonWriter, value: ChatChoiceChunk?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.beginObject()
            writer.name("index").value(value.index)
            writer.name("message").jsonValue(GsonBuilder().create().toJson(value.message))
            writer.name("delta").value(value.delta)
            writer.name("finish_reason")
            if (value.finishReason == null) {
                writer.nullValue()
            } else {
                writer.value(value.finishReason!!.name.lowercase())
            }
            writer.endObject()
        }
    }

    override fun read(reader: JsonReader): ChatChoiceChunk? {
        var index: Int = -1
        var message: ChatMessage? = null
        var delta: String? = null
        var finishReason: FinishReason? = null

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "index" -> index = reader.nextInt()
                "message" -> {
                    when (reader.peek()) {
                        JsonToken.NULL -> reader.nextNull()
                        else -> message = GsonBuilder().create().fromJson(reader, ChatMessage::class.java)
                    }
                }
                "delta" -> {
                    when (reader.peek()) {
                        JsonToken.BEGIN_OBJECT -> reader.skipValue()
                        else -> delta = reader.nextString()
                    }
                }
                "finish_reason" -> {
                    when (reader.peek()) {
                        JsonToken.NULL -> reader.skipValue()
                        else -> finishReason = FinishReason.valueOf(reader.nextString().uppercase())
                    }
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return ChatChoiceChunk(index, message ?: ChatMessage(ChatUser.ASSISTANT, ""), delta ?: "", finishReason)
    }
}
