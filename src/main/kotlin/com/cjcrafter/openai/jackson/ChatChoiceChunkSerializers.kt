package com.cjcrafter.openai.jackson

import com.cjcrafter.openai.FinishReason
import com.cjcrafter.openai.chat.ChatChoiceChunk
import com.cjcrafter.openai.chat.ChatMessage
import com.cjcrafter.openai.chat.ChatUser
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

// Custom serializer for ChatChoiceChunk
class ChatChoiceChunkSerializer : StdSerializer<ChatChoiceChunk>(ChatChoiceChunk::class.java) {
    override fun serialize(value: ChatChoiceChunk, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeNumberField("index", value.index)
        gen.writeFieldName("message")
        jacksonObjectMapper().writeValue(gen, value.message)
        gen.writeStringField("delta", value.delta)
        gen.writeStringField("finish_reason", value.finishReason?.name?.lowercase())
        gen.writeEndObject()
    }
}

// Custom deserializer for ChatChoiceChunk
class ChatChoiceChunkDeserializer : StdDeserializer<ChatChoiceChunk>(ChatChoiceChunk::class.java) {
    override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: DeserializationContext): ChatChoiceChunk {
        val node: JsonNode = p.codec.readTree(p)
        val index: Int = node.get("index").intValue()
        val messageNode = node.get("message")
        val message: ChatMessage? = if (messageNode.isNull) null else jacksonObjectMapper().treeToValue(messageNode, ChatMessage::class.java)
        val delta: String = node.get("delta").asText("")
        val finishReasonNode = node.get("finish_reason")
        val finishReason: FinishReason? = if (finishReasonNode.isNull) null else FinishReason.valueOf(finishReasonNode.asText().uppercase())

        // When message == null, that means that a message has not been provided yet.
        // In this case, ChatUser is always assistant.
        return ChatChoiceChunk(
            index = index,
            message = message ?: ChatMessage(ChatUser.ASSISTANT, ""),
            delta = delta,
            finishReason = finishReason
        )
    }
}
