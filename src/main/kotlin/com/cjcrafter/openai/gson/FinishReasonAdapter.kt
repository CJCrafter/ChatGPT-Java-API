package com.cjcrafter.openai.gson

import com.cjcrafter.openai.FinishReason
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class FinishReasonAdapter : TypeAdapter<FinishReason?>() {

    override fun write(writer: JsonWriter, value: FinishReason?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.name)
        }
    }

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
