package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.files.FileObject
import com.cjcrafter.openai.util.BuilderHelper
import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a request to create a new message in a [Thread].
 *
 * @property role The role of the user creating the message.
 * @property content The content of the message.
 * @property fileIds The IDs of the files to attach to the message.
 * @property metadata The metadata to attach to the message.
 */
data class CreateThreadMessageRequest(
    var role: ThreadUser,
    var content: String,
    @JsonProperty("file_ids") var fileIds: MutableList<String>?,
    var metadata: MutableMap<String, String>?,
) {
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var role: ThreadUser? = null
        private var content: String? = null
        private var fileIds: MutableList<String>? = null
        private var metadata: MutableMap<String, String>? = null

        /**
         * Who is creating the message. This must always be [ThreadUser.USER].
         *
         * @param role The user creating the message
         */
        fun role(role: ThreadUser) = apply {
            if (role != ThreadUser.USER)
                throw IllegalArgumentException("role must be USER")

            this.role = role
        }

        /**
         * The content of the message.
         *
         * @param content The content of the message
         */
        fun content(content: String) = apply {
            this.content = content
        }

        /**
         * The IDs of the files to attach to the message.
         *
         * @param fileIds The IDs of the files to attach to the message
         */
        fun fileIds(fileIds: MutableList<String>) = apply {
            this.fileIds = fileIds
        }

        /**
         * The metadata to attach to the message.
         *
         * @param metadata The metadata to attach to the message
         */
        fun metadata(metadata: MutableMap<String, String>) = apply {
            BuilderHelper.assertMetadata(metadata)
            this.metadata = metadata
        }

        /**
         * Adds a file to the list of files to attach to the message.
         *
         * @param file The file to attach to the message
         */
        fun addFile(file: FileObject) = apply {
            if (fileIds == null) fileIds = mutableListOf()
            fileIds!!.add(file.id)
        }

        /**
         * Adds a file to the list of files to attach to the message.
         *
         * @param fileId The ID of the file to attach to the message
         */
        fun addFile(fileId: String) = apply {
            if (fileIds == null) fileIds = mutableListOf()
            fileIds!!.add(fileId)
        }

        /**
         * Adds a metadata key-value pair to the message.
         *
         * @param key The key of the metadata
         * @param value The value of the metadata
         */
        fun addMetadata(key: String, value: String) = apply {
            BuilderHelper.assertMetadata(key, value)
            if (metadata == null) metadata = mutableMapOf()
            BuilderHelper.tryAddMetadata(metadata!!)
            metadata!![key] = value
        }

        fun build() = CreateThreadMessageRequest(
            role ?: throw IllegalArgumentException("role must be set"),
            content ?: throw IllegalArgumentException("content must be set"),
            fileIds,
            metadata,
        )
    }

    companion object {

        /**
         * Returns a new [Builder] instance to create a [CreateThreadMessageRequest].
         */
        @JvmStatic
        fun builder() = Builder()
    }
}
