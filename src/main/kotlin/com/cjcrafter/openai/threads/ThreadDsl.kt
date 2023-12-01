package com.cjcrafter.openai.threads

import com.cjcrafter.openai.threads.message.CreateThreadMessageRequest
import com.cjcrafter.openai.threads.message.MessageHandler
import com.cjcrafter.openai.threads.message.ThreadMessage
import com.cjcrafter.openai.threads.runs.CreateRunRequest
import com.cjcrafter.openai.threads.runs.Run
import com.cjcrafter.openai.threads.runs.RunHandler

fun createThreadRequest(block: CreateThreadRequest.Builder.() -> Unit): CreateThreadRequest {
    return CreateThreadRequest.builder().apply(block).build()
}

fun ThreadHandler.create(block: CreateThreadRequest.Builder.() -> Unit): Thread {
    val request = createThreadRequest(block)
    return create(request)
}

fun modifyThreadRequest(block: ModifyThreadRequest.Builder.() -> Unit): ModifyThreadRequest {
    return ModifyThreadRequest.builder().apply(block).build()
}

fun ThreadHandler.modify(thread: Thread, block: ModifyThreadRequest.Builder.() -> Unit): Thread {
    return modify(thread.id, block)
}

fun ThreadHandler.modify(id: String, block: ModifyThreadRequest.Builder.() -> Unit): Thread {
    val request = modifyThreadRequest(block)
    return modify(id, request)
}

/* MESSAGES */

fun createThreadMessage(block: CreateThreadMessageRequest.Builder.() -> Unit): CreateThreadMessageRequest {
    return CreateThreadMessageRequest.builder().apply(block).build()
}

fun MessageHandler.create(block: CreateThreadMessageRequest.Builder.() -> Unit): ThreadMessage {
    val request = createThreadMessage(block)
    return create(request)
}

/* RUNS */

fun createRunRequest(block: CreateRunRequest.Builder.() -> Unit): CreateRunRequest {
    return CreateRunRequest.builder().apply(block).build()
}

fun RunHandler.create(block: CreateRunRequest.Builder.() -> Unit): Run {
    val request = createRunRequest(block)
    return create(request)
}