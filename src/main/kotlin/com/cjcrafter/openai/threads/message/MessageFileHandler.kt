package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.threads.Thread
import org.jetbrains.annotations.Contract

/**
 * Handler used to interact with a [MessageFile] objects.
 */
interface MessageFileHandler {

    /**
     * The id of the [Thread] that this handler is for.
     */
    val threadId: String

    /**
     * The id of the [ThreadMessage] that this handler is for.
     */
    val messageId: String

    /**
     * Retrieves the file.
     *
     * @param file The file to retrieve
     * @return The retrieved file
     */
    @Contract(pure = true)
    fun retrieve(file: MessageFile): MessageFile = retrieve(file.id)

    /**
     * Retrieves the file with the given id.
     *
     * @param fileId The id of the file to retrieve
     * @return The retrieved file
     */
    @Contract(pure = true)
    fun retrieve(fileId: String): MessageFile

    /**
     * Lists the 20 most recent files in the message.
     *
     * @return The list of files
     */
    @Contract(pure = true)
    fun list(): ListMessageFilesResponse = list(null)

    /**
     * Lists the files in the message.
     *
     * @param request The request to use for listing the files
     * @return The list of files
     */
    @Contract(pure = true)
    fun list(request: ListMessageFilesRequest? = null): ListMessageFilesResponse
}