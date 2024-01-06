package com.cjcrafter.openai.threads.message

import org.jetbrains.annotations.Contract

/**
 * Handler used to interact with a [ThreadMessage] objects.
 */
interface MessageHandler {

    /**
     * The id of the [Thread] that this handler is for.
     */
    val threadId: String

    /**
     * Creates a new [ThreadMessage] object.
     *
     * @param request The values of the message to create
     * @return The created message
     */
    fun create(request: CreateThreadMessageRequest): ThreadMessage

    /**
     * Retrieves the updated message object from the given message.
     *
     * @param msg The message to retrieve
     * @return The retrieved message
     */
    @Contract(pure = true)
    fun retrieve(msg: ThreadMessage): ThreadMessage = retrieve(msg.id)

    /**
     * Retrieves the message with the given id.
     *
     * @param id The id of the message to retrieve
     * @return The retrieved message
     */
    @Contract(pure = true)
    fun retrieve(id: String): ThreadMessage

    /**
     * Modifies the given message to have the given updated values.
     *
     * @param message The message to modify
     * @param request The values to update the message with
     * @return The modified message
     */
    fun modify(message: ThreadMessage, request: ModifyThreadMessageRequest): ThreadMessage = modify(message.id, request)

    /**
     * Modifies the message with the given id to have the given updated values.
     *
     * @param messageId The id of the message to modify
     * @param request The values to update the message with
     * @return The modified message
     */
    fun modify(messageId: String, request: ModifyThreadMessageRequest): ThreadMessage

    /**
     * Lists the 20 most recent messages in the thread.
     *
     * @return The list of messages
     */
    @Contract(pure = true)
    fun list(): ListThreadMessagesResponse = list(null)

    /**
     * Lists messages in the thread.
     *
     * @param request The values to filter the messages by
     * @return The list of messages
     */
    @Contract(pure = true)
    fun list(request: ListThreadMessagesRequest?): ListThreadMessagesResponse

    /**
     * Returns a handler for interacting with the files in the given message.
     *
     * @param msg The message to get the files for
     * @return The handler for interacting with the files
     */
    @Contract(pure = true)
    fun files(msg: ThreadMessage): MessageFileHandler = files(msg.id)

    /**
     * Returns a handler for interacting with the files in the message with the
     * given id.
     *
     * @param messageId The id of the message to get the files for
     * @return The handler for interacting with the files
     */
    @Contract(pure = true)
    fun files(messageId: String): MessageFileHandler
}
