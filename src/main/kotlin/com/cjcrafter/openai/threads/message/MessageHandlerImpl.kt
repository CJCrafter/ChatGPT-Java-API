package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.RequestHelper

class MessageHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String,
    override val threadId: String,
): MessageHandler {

    override fun create(request: CreateThreadMessageRequest): ThreadMessage {
        val httpRequest = requestHelper.buildRequest(request, endpoint).addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, ThreadMessage::class.java)
    }

    override fun retrieve(id: String): ThreadMessage {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, ThreadMessage::class.java)
    }

    override fun modify(messageId: String, request: ModifyThreadMessageRequest): ThreadMessage {
        val httpRequest = requestHelper.buildRequest(request, "$endpoint/$messageId").addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, ThreadMessage::class.java)
    }

    override fun list(request: ListThreadMessagesRequest?): ListThreadMessagesResponse {
        val httpRequest = requestHelper.buildRequestNoBody(endpoint, request?.toMap()).addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, ListThreadMessagesResponse::class.java)
    }

    private val fileHandlers = mutableMapOf<String, MessageFileHandler>()
    override fun files(messageId: String): MessageFileHandler {
        return fileHandlers.getOrPut(messageId) {
            MessageFileHandlerImpl(requestHelper, "$endpoint/$messageId/files", threadId, messageId)
        }
    }
}