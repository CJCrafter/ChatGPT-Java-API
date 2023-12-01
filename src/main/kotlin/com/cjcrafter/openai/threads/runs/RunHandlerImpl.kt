package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.RequestHelper

class RunHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String,
    override val threadId: String,
): RunHandler {
    override fun create(request: CreateRunRequest): Run {
        val httpRequest = requestHelper.buildRequest(request, endpoint).addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, Run::class.java)
    }

    override fun retrieve(id: String): Run {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, Run::class.java)
    }

    override fun modify(id: String, request: ModifyRunRequest): Run {
        val httpRequest = requestHelper.buildRequest(request, "$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, Run::class.java)
    }

    override fun list(request: ListRunsRequest?): ListRunsResponse {
        val httpRequest = requestHelper.buildRequestNoBody(endpoint, request?.toMap()).addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, ListRunsResponse::class.java)
    }

    override fun submitToolOutputs(id: String, submission: SubmitToolOutputs): Run {
        val httpRequest = requestHelper.buildRequest(submission, "$endpoint/$id/submit_tool_outputs").addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, Run::class.java)
    }

    override fun cancel(id: String): Run {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id/cancel").addHeader("OpenAI-Beta", "assistants=v1").method("POST", null).build()
        return requestHelper.executeRequest(httpRequest, Run::class.java)
    }
}