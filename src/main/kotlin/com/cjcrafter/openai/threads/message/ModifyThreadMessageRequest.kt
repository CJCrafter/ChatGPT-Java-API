package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.util.BuilderHelper
import com.cjcrafter.openai.util.OpenAIDslMarker

/**
 * Represents a request to modify a [ThreadMessage].
 *
 * @property metadata The new metadata map to override
 */
data class ModifyThreadMessageRequest(
    var metadata: MutableMap<String, String>
) {
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var metadata: MutableMap<String, String>? = null

        /**
         * Sets the new metadata map.
         *
         * @param metadata The new metadata map.
         * @throws IllegalArgumentException If the metadata map has more than 16 entries,
         * or any key has more than 64 characters, or any value has more than 512 characters
         */
        fun metadata(metadata: MutableMap<String, String>) = apply {
            BuilderHelper.assertMetadata(metadata)
            this.metadata = metadata
        }

        /**
         * Adds metadata to the metadata map.
         *
         * @param key The key, which must be <= 64 characters
         * @param value The value, which must be <= 512 characters
         * @throws IllegalArgumentException If the key or value is too long
         */
        fun addMetadata(key: String, value: String) = apply {
            BuilderHelper.assertMetadata(key, value)
            if (metadata == null) metadata = mutableMapOf()
            BuilderHelper.tryAddMetadata(metadata!!)
            metadata!![key] = value
        }
    }
}