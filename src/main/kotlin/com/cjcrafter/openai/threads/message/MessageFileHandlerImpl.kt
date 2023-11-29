package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.RequestHelper

class MessageFileHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String,
    override val threadId: String,
    override val messageId: String,
): MessageFileHandler {
    override fun retrieve(fileId: String): MessageFile {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$fileId").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, MessageFile::class.java)
    }

    override fun list(request: ListMessageFilesRequest?): ListMessageFilesResponse {
        val httpRequest = requestHelper.buildRequestNoBody(endpoint, request?.toMap()).addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, ListMessageFilesResponse::class.java)
    }
}