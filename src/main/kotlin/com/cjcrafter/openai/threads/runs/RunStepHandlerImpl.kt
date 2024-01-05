package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.RequestHelper

class RunStepHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String,
    override val threadId: String,
    override val runId: String,
) : RunStepHandler {

    override fun retrieve(id: String): RunStep {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, RunStep::class.java)
    }

    override fun list(request: ListRunStepsRequest?): ListRunStepsResponse {
        val httpRequest = requestHelper.buildRequestNoBody(endpoint, request?.toMap()).addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, ListRunStepsResponse::class.java)
    }
}