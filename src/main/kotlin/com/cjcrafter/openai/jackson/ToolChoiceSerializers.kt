package com.cjcrafter.openai.jackson

import com.cjcrafter.openai.chat.tool.ToolChoice
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import java.io.IOException

// Custom serializer for ToolChoice
class ToolChoiceSerializer : StdSerializer<ToolChoice>(ToolChoice::class.java) {
    @Throws(IOException::class)
    override fun serialize(value: ToolChoice, gen: JsonGenerator, provider: SerializerProvider) {
        when (value) {
            is ToolChoice.None -> gen.writeString("none")
            is ToolChoice.Auto -> gen.writeString("auto")
            is ToolChoice.Function -> {
                gen.writeStartObject()
                gen.writeStringField("type", "function")
                gen.writeObjectFieldStart("function")
                gen.writeStringField("name", value.name)
                gen.writeEndObject()
                gen.writeEndObject()
            }
        }
    }
}


// Custom deserializer for ToolChoice
class ToolChoiceDeserializer : StdDeserializer<ToolChoice>(ToolChoice::class.java) {
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ToolChoice {
        val node: JsonNode = p.codec.readTree(p)
        return when {
            node.isTextual -> {
                when (val textValue = node.asText()) {
                    "none" -> ToolChoice.None
                    "auto" -> ToolChoice.Auto
                    else -> throw InvalidFormatException(
                        p, "Invalid tool choice", textValue, ToolChoice::class.java
                    )
                }
            }
            node.isObject -> {
                val type = node.get("type").asText()
                val functionNameNode = node.get("function").get("name")
                if (type == "function" && functionNameNode != null && !functionNameNode.isNull) {
                    ToolChoice.Function(functionNameNode.asText())
                } else {
                    throw InvalidFormatException(
                        p, "Invalid tool type", node.toString(), ToolChoice::class.java
                    )
                }
            }
            else -> throw InvalidFormatException(
                p, "Unexpected JSON token", node.toString(), ToolChoice::class.java
            )
        }
    }
}
