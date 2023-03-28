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

/**
 * A Gson TypeAdapter for serializing and deserializing the `ChatChoiceChunk`
 * data class to and from JSON. This adapter handles the `delta` field of the
 * `ChatChoiceChunk` class, which can be either a string or an object.
 */
class ChatChoiceChunkAdapter : TypeAdapter<ChatChoiceChunk?>() {

    /**
     * Serializes a `ChatChoiceChunk` object to JSON.
     * If the provided value is null, it is serialized to a JSON null value.
     *
     * @param writer The JsonWriter to which the value should be written.
     * @param value The value to be serialized.
     */
    override fun write(writer: JsonWriter, value: ChatChoiceChunk?) {
        writer.beginObject()
        writer.name("index").value(value!!.index)
        writer.name("message").jsonValue(GsonBuilder().create().toJson(value.message))
        writer.name("delta").value(value.delta)
        writer.name("finish_reason")

        // in general, it is bad practice to save a message in progress (but yes... you can do it)
        if (value.finishReason == null) {
            writer.nullValue()
        } else {
            writer.value(value.finishReason!!.name.lowercase())
        }
        writer.endObject()
    }

    /**
     * Deserializes a JSON string to a `ChatChoiceChunk` object.
     *
     * @param reader The JsonReader from which the value should be read.
     * @return The deserialized `ChatChoiceChunk` object, or null if the JSON value is null.
     * @throws IllegalArgumentException if the provided string cannot be parsed to a valid `ChatChoiceChunk` object.
     */
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
                    // delta -> map when returned from OpenAI (we can ignore it since we use #update(json)).
                    // delta -> string when we store it as json.
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

        // when message == null, that means that a message has not been
        // provided yet. This means it is the first json message from OpenAI.
        // In this case, ChatUser is always assistant.
        return ChatChoiceChunk(index, message ?: ChatMessage(ChatUser.ASSISTANT, ""), delta ?: "", finishReason)
    }
}
