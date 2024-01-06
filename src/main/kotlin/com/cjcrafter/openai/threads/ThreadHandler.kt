package com.cjcrafter.openai.threads

import com.cjcrafter.openai.threads.message.MessageHandler
import com.cjcrafter.openai.threads.runs.RunHandler
import org.jetbrains.annotations.Contract

/**
 * Handler used to interact with a [Thread] objects.
 */
interface ThreadHandler {

    /**
     * Creates a new empty thread.
     *
     * @return The created thread
     */
    fun create(): Thread = create(createThreadRequest { /* intentionally empty */ })

    /**
     * Creates a new thread with the given options.
     *
     * @param request The values of the thread to create
     * @return The created thread
     */
    fun create(request: CreateThreadRequest): Thread

    /**
     * Retrieves the updated thread object from the given thread.
     *
     * This method returns a new thread object wrapper. The thread parameter is
     * used only for [Thread.id]. This method is useful for getting updated
     * information about a thread's status or values.
     *
     * @param thread The thread to retrieve
     * @return The retrieved thread
     */
    @Contract(pure = true)
    fun retrieve(thread: Thread): Thread = retrieve(thread.id)

    /**
     * Retrieves the thread with the given id.
     *
     * @param id The id of the thread to retrieve
     * @return The retrieved thread
     */
    @Contract(pure = true)
    fun retrieve(id: String): Thread

    /**
     * Deletes the given thread from the OpenAI API.
     *
     * You should **always** check the deletion status to ensure the thread was
     * deleted successfully. After confirming deletion, you should discard all
     * of your references to the thread, since they are now invalid.
     *
     * @param thread The thread to delete
     * @return The deletion status
     */
    fun delete(thread: Thread): ThreadDeletionStatus = delete(thread.id)

    /**
     * Deletes the thread with the given id from the OpenAI API.
     *
     * You should **always** check the deletion status to ensure the thread was
     * deleted successfully. After confirming deletion, you should discard all
     * of your references to the thread, since they are now invalid.
     *
     * @param id The id of the thread to delete
     * @return The deletion status
     */
    fun delete(id: String): ThreadDeletionStatus

    /**
     * Modifies the given thread to have the given updated values.
     *
     * This method returns a new thread object wrapper. The thread parameter is
     * used only for [Thread.id]. After this request, you should discard all of
     * your references to the thread, since they are now outdated.
     *
     * @param thread The thread to modify
     * @param request The values to update the thread with
     * @return The modified thread
     */
    fun modify(thread: Thread, request: ModifyThreadRequest): Thread = modify(thread.id, request)

    /**
     * Modifies the thread with the given id to have the given updated values.
     *
     * This method returns a new thread object wrapper. After this request, you
     * should discard your references to the thread, since they are now outdated.
     *
     * @param id The id of the thread to modify
     * @param request The values to update the thread with
     * @return The modified thread
     */
    fun modify(id: String, request: ModifyThreadRequest): Thread

    /**
     * Returns a handler for interacting with the messages in the given thread.
     *
     * @param thread The thread to get the messages for
     * @return The handler for interacting with the messages
     */
    @Contract(pure = true)
    fun messages(thread: Thread): MessageHandler = messages(thread.id)

    /**
     * Returns a handler for interacting with the messages in the thread with
     * the given id.
     *
     * @param threadId The id of the thread to get the messages for
     * @return The handler for interacting with the messages
     */
    @Contract(pure = true)
    fun messages(threadId: String): MessageHandler

    /**
     * Returns a handler for interacting with the runs in the given thread.
     *
     * @param thread The thread to get the runs for
     * @return The handler for interacting with the runs
     */
    @Contract(pure = true)
    fun runs(thread: Thread): RunHandler = runs(thread.id)

    /**
     * Returns a handler for interacting with the runs in the thread with
     * the given id.
     *
     * @param threadId The id of the thread to get the runs for
     * @return The handler for interacting with the runs
     */
    @Contract(pure = true)
    fun runs(threadId: String): RunHandler
}