package com.cjcrafter.openai.threads.message

interface MessageHandler {

    val threadId: String

    fun create(request: CreateThreadMessageRequest): ThreadMessage

    fun retrieve(msg: ThreadMessage): ThreadMessage = retrieve(msg.id)

    fun retrieve(id: String): ThreadMessage

    fun modify(message: ThreadMessage, request: ModifyThreadMessageRequest): ThreadMessage = modify(message.id, request)

    fun modify(messageId: String, request: ModifyThreadMessageRequest): ThreadMessage

    fun list(): ListThreadMessagesResponse = list(null)

    fun list(request: ListThreadMessagesRequest?): ListThreadMessagesResponse

    fun files(msg: ThreadMessage): MessageFileHandler = files(msg.id)

    fun files(messageId: String): MessageFileHandler

    operator fun get(msg: ThreadMessage): MessageFileHandler = files(msg)

    operator fun get(messageId: String): MessageFileHandler = files(messageId)
}
