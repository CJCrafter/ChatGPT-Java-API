package com.cjcrafter.openai.threads

import com.cjcrafter.openai.threads.message.CreateThreadMessageRequest
import com.cjcrafter.openai.threads.message.ThreadUser
import com.cjcrafter.openai.util.BuilderHelper
import com.cjcrafter.openai.util.OpenAIDslMarker

/**
 * Represents a request to create a new [Thread].
 *
 * @property messages The messages to send to the thread
 * @property metadata The metadata to add to the thread
 */
data class CreateThreadRequest(
    val messages: MutableList<CreateThreadMessageRequest>? = null,
    val metadata: MutableMap<String, String>? = null,
) {
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var messages: MutableList<CreateThreadMessageRequest>? = null
        private var metadata: MutableMap<String, String>? = null

        /**
         * Sets which messages are already present in the thread.
         *
         * @param messages The messages to set
         * @throws IllegalArgumentException if any of the messages are not user messages,
         * or if any of the metadata keys or values are too long
         */
        fun messages(messages: MutableList<CreateThreadMessageRequest>) = apply {
            for (message in messages)
                checkMessage(message)

            this.messages = messages
        }

        /**
         * Sets the metadata for the thread. Useful for developers.
         *
         * @param metadata The metadata to set
         * @throws IllegalArgumentException if any of the metadata keys or values are too long
         */
        fun metadata(metadata: MutableMap<String, String>) = apply {
            BuilderHelper.assertMetadata(metadata)
            this.metadata = metadata
        }

        /**
         * Adds a message to the thread.
         *
         * @param message The message to add
         * @throws IllegalArgumentException if the message is not a user message,
         * or if any of the metadata keys or values are too long
         */
        fun addMessage(message: CreateThreadMessageRequest) = apply {
            checkMessage(message)
            if (messages == null)
                messages = mutableListOf()
            messages!!.add(message)
        }

        /**
         * Add metadata to the thread. Useful for developers.
         *
         * @param key The metadata key, 64 characters or less
         * @param value The metadata value, 512 characters or less
         */
        fun addMetadata(key: String, value: String) = apply {
            BuilderHelper.assertMetadata(key, value)
            if (metadata == null)
                metadata = mutableMapOf()
            BuilderHelper.tryAddMetadata(metadata!!)
            metadata!![key] = value
        }

        private fun checkMessage(message: CreateThreadMessageRequest) {
            if (message.role != ThreadUser.USER)
                throw IllegalArgumentException("Only user messages can be sent to the API")

            message.metadata?.let { BuilderHelper.assertMetadata(it) }
        }

        /**
         * Builds the [CreateThreadRequest] instance.
         */
        fun build(): CreateThreadRequest {
            return CreateThreadRequest(
                messages = messages,
                metadata = metadata,
            )
        }
    }

    companion object {

        /**
         * Instantiates a new [CreateThreadRequest] builder instance.
         */
        @JvmStatic
        fun builder() = Builder()
    }
}
