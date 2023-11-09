package com.cjcrafter.openai.gson

import com.cjcrafter.openai.chat.tool.ToolChoice
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class ToolChoiceAdapter : TypeAdapter<ToolChoice>() {

    override fun write(out: JsonWriter, value: ToolChoice?) {
        when (value) {
            is ToolChoice.None -> out.value("none")
            is ToolChoice.Auto -> out.value("auto")
            is ToolChoice.Function -> {
                out.beginObject()
                out.name("type").value("function")
                out.name("function")
                out.beginObject()
                out.name("name").value(value.name)
                out.endObject()
                out.endObject()
            }

            else -> out.nullValue()
        }
    }

    override fun read(`in`: JsonReader): ToolChoice {
        if (`in`.peek() == JsonToken.STRING) {
            return when (`in`.nextString()) {
                "none" -> ToolChoice.None
                "auto" -> ToolChoice.Auto
                else -> throw JsonParseException("Invalid tool choice")
            }
        } else if (`in`.peek() == JsonToken.BEGIN_OBJECT) {
            `in`.beginObject()
            var type: String? = null
            var name: String? = null
            while (`in`.hasNext()) {
                when (`in`.nextName()) {
                    "type" -> type = `in`.nextString()
                    "function" -> {
                        `in`.beginObject()
                        while (`in`.hasNext()) {
                            if (`in`.nextName() == "name") {
                                name = `in`.nextString()
                            }
                        }
                        `in`.endObject()
                    }
                }
            }
            `in`.endObject()
            if (type == null)
                throw JsonParseException("Missing type or function name")

            when (type) {
                "function" -> {
                    if (name == null)
                        throw JsonParseException("Missing function name")
                    return ToolChoice.Function(name)
                }
                else -> throw JsonParseException("Invalid tool type")
            }
        } else {
            throw JsonParseException("Unexpected JSON token: " + `in`.peek())
        }
    }
}