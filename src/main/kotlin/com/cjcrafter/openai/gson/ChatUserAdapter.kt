package com.cjcrafter.openai.gson

import com.cjcrafter.openai.chat.ChatUser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class ChatUserAdapter : TypeAdapter<ChatUser?>() {

    override fun write(writer: JsonWriter, value: ChatUser?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value.name.lowercase())
        }
    }

    override fun read(reader: JsonReader): ChatUser? {
        return if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            null
        } else {
            val name = reader.nextString()
            ChatUser.valueOf(name.uppercase())
        }
    }
}
