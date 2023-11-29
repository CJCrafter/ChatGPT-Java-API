package com.cjcrafter.openai.assistants

import com.cjcrafter.openai.RequestHelper

/**
 * Holds the default underlying implementation of the [AssistantHandler] interface.
 *
 * @param requestHelper The request helper to use
 * @param endpoint The assistants endpoint
 */
class AssistantHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String
): AssistantHandler {

    override fun create(request: CreateAssistantRequest): Assistant {
        val httpRequest = requestHelper.buildRequest(request, endpoint).addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, Assistant::class.java)
    }

    override fun retrieve(id: String): Assistant {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, Assistant::class.java)
    }

    override fun delete(id: String): AssistantDeletionStatus {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").delete().build()
        return requestHelper.executeRequest(httpRequest, AssistantDeletionStatus::class.java)
    }

    override fun list(request: ListAssistantRequest?): ListAssistantResponse {
        val httpRequest = requestHelper.buildRequestNoBody(endpoint, request?.toMap()).addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, ListAssistantResponse::class.java)
    }

    override fun modify(id: String, request: ModifyAssistantRequest): Assistant {
        val httpRequest = requestHelper.buildRequest(request, "$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, Assistant::class.java)
    }
}