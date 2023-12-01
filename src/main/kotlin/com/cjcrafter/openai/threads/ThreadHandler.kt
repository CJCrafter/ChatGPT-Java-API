package com.cjcrafter.openai.threads

import com.cjcrafter.openai.threads.message.MessageHandler
import com.cjcrafter.openai.threads.runs.RunHandler

/**
 * Handles all API requests involving the [Thread] object (not to be confused
 * with [java.lang.Thread]).
 */
interface ThreadHandler {

    /**
     * Creates a new empty thread.
     */
    fun create(): Thread = create(createThreadRequest { /* intentionally empty */ })

    /**
     * Creates a new thread with the given options.
     */
    fun create(request: CreateThreadRequest): Thread

    /**
     * Retrieves the updated thread object from the given thread.
     *
     * This method returns a new thread object wrapper. The thread parameter is
     * used only for [Thread.id]. This method is useful for getting updated
     * information about a thread's status or values.
     */
    fun retrieve(thread: Thread): Thread = retrieve(thread.id)

    /**
     * Retrieves the thread with the given id.
     */
    fun retrieve(id: String): Thread

    /**
     * Deletes the given thread from the OpenAI API.
     *
     * You should **always** check the deletion status to ensure the thread was
     * deleted successfully. After confirming deletion, you should discard all
     * of your references to the thread, since they are now invalid.
     */
    fun delete(thread: Thread): ThreadDeletionStatus = delete(thread.id)

    /**
     * Deletes the thread with the given id from the OpenAI API.
     *
     * You should **always** check the deletion status to ensure the thread was
     * deleted successfully. After confirming deletion, you should discard all
     * of your references to the thread, since they are now invalid.
     */
    fun delete(id: String): ThreadDeletionStatus

    /**
     * Modifies the given thread to have the given updated values.
     *
     * This method returns a new thread object wrapper. The thread parameter is
     * used only for [Thread.id]. After this request, you should discard all of
     * your references to the thread, since they are now outdated.
     */
    fun modify(thread: Thread, request: ModifyThreadRequest): Thread = modify(thread.id, request)

    /**
     * Modifies the thread with the given id to have the given updated values.
     *
     * This method returns a new thread object wrapper. After this request, you
     * should discard your references to the thread, since they are now outdated.
     */
    fun modify(id: String, request: ModifyThreadRequest): Thread

    /**
     * Returns a handler for the messages endpoint for the given thread.
     */
    fun messages(thread: Thread): MessageHandler = messages(thread.id)

    /**
     * Returns a handler for the messages endpoint for the thread with the given id.
     */
    fun messages(threadId: String): MessageHandler

    /**
     * Returns a handler for the runs endpoint for the given thread.
     */
    fun runs(thread: Thread): RunHandler = runs(thread.id)

    /**
     * Returns a handler for the runs endpoint for the thread with the given id.
     */
    fun runs(threadId: String): RunHandler
}