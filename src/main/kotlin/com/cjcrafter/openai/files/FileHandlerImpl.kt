package com.cjcrafter.openai.files

import com.cjcrafter.openai.RequestHelper

class FileHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String
): FileHandler {
    override fun upload(request: UploadFileRequest): FileObject {
        val httpRequest = requestHelper.buildRequest(request, endpoint).addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, FileObject::class.java)
    }

    override fun retrieve(id: String): FileObject {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, FileObject::class.java)
    }

    override fun retrieveContents(id: String): String {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id/content").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest)
    }

    override fun delete(id: String): FileDeletionStatus {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").delete().build()
        return requestHelper.executeRequest(httpRequest, FileDeletionStatus::class.java)
    }

    override fun list(request: ListFilesRequest?): ListFilesResponse {
        val httpRequest = requestHelper.buildRequestNoBody(endpoint, request?.toMap()).addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, ListFilesResponse::class.java)
    }
}