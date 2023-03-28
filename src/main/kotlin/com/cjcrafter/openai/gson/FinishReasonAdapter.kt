package com.cjcrafter.openai.gson

import com.cjcrafter.openai.FinishReason
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * A Gson TypeAdapter for serializing and deserializing the FinishReason enum
 * class to and from JSON.
 */
class FinishReasonAdapter : TypeAdapter<FinishReason?>() {

    /**
     * Serializes a FinishReason enum value to a JSON string.
     * If the provided value is null, it is serialized to a JSON null value.
     * Otherwise, it is serialized to a lowercase string representation of its name.
     *
     * @param writer The JsonWriter to which the value should be written.
     * @param value The value to be serialized.
     */
    override fun write(writer: JsonWriter, value: FinishReason?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.name.lowercase())
        }
    }

    /**
     * Deserializes a JSON string to a FinishReason enum value.
     * If the provided JSON value is null, this method returns null.
     * If the provided string cannot be parsed to a valid FinishReason enum value, this method throws an exception.
     *
     * @param reader The JsonReader from which the value should be read.
     * @return The deserialized FinishReason value, or null if the JSON value is null.
     * @throws IllegalArgumentException if the provided string cannot be parsed to a valid FinishReason enum value.
     */
    override fun read(reader: JsonReader): FinishReason? {
        return if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            null
        } else {
            val name = reader.nextString()
            FinishReason.valueOf(name.uppercase())
        }
    }
}
