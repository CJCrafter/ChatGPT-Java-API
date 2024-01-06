package com.cjcrafter.openai.threads

import com.cjcrafter.openai.RequestHelper
import com.cjcrafter.openai.threads.message.MessageHandler
import com.cjcrafter.openai.threads.message.MessageHandlerImpl
import com.cjcrafter.openai.threads.runs.RunHandler
import com.cjcrafter.openai.threads.runs.RunHandlerImpl

class ThreadHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String,
): ThreadHandler {
    override fun create(request: CreateThreadRequest): Thread {
        val httpRequest = requestHelper.buildRequest(request, endpoint).addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, Thread::class.java)
    }

    override fun retrieve(id: String): Thread {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").get().build()
        return requestHelper.executeRequest(httpRequest, Thread::class.java)
    }

    override fun delete(id: String): ThreadDeletionStatus {
        val httpRequest = requestHelper.buildRequestNoBody("$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").delete().build()
        return requestHelper.executeRequest(httpRequest, ThreadDeletionStatus::class.java)
    }

    override fun modify(id: String, request: ModifyThreadRequest): Thread {
        val httpRequest = requestHelper.buildRequest(request, "$endpoint/$id").addHeader("OpenAI-Beta", "assistants=v1").build()
        return requestHelper.executeRequest(httpRequest, Thread::class.java)
    }

    private val messageHandlers = mutableMapOf<String, MessageHandler>()
    override fun messages(threadId: String): MessageHandler {
        return messageHandlers.getOrPut(threadId) {
            MessageHandlerImpl(requestHelper, "$endpoint/$threadId/messages", threadId)
        }
    }

    private val runHandlers = mutableMapOf<String, RunHandler>()
    override fun runs(threadId: String): RunHandler {
        return runHandlers.getOrPut(threadId) {
            RunHandlerImpl(requestHelper, "$endpoint/$threadId/runs", threadId)
        }
    }
}