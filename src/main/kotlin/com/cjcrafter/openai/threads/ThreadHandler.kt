package com.cjcrafter.openai.threads

import com.cjcrafter.openai.threads.message.MessageHandler

interface ThreadHandler {

    fun create(request: CreateThreadRequest): Thread

    fun retrieve(thread: Thread): Thread = retrieve(thread.id)

    fun retrieve(id: String): Thread

    fun delete(thread: Thread): ThreadDeletionStatus = delete(thread.id)

    fun delete(id: String): ThreadDeletionStatus

    fun modify(thread: Thread, request: ModifyThreadRequest): Thread = modify(thread.id, request)

    fun modify(id: String, request: ModifyThreadRequest): Thread

    fun messages(thread: Thread): MessageHandler = messages(thread.id)

    fun messages(threadId: String): MessageHandler

    operator fun get(thread: Thread): MessageHandler = messages(thread)

    operator fun get(threadId: String): MessageHandler = messages(threadId)
}