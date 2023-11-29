package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.threads.Thread

interface MessageFileHandler {

    /**
     * The id of the [Thread] that this message belongs to.
     */
    val threadId: String

    /**
     * The id of this [ThreadMessage].
     */
    val messageId: String

    fun retrieve(file: MessageFile): MessageFile = retrieve(file.id)

    fun retrieve(fileId: String): MessageFile

    fun list(): ListMessageFilesResponse = list(null)

    fun list(request: ListMessageFilesRequest? = null): ListMessageFilesResponse
}