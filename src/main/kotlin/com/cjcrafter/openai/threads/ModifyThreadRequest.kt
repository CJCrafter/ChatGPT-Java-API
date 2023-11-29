package com.cjcrafter.openai.threads

import com.cjcrafter.openai.util.BuilderHelper
import com.cjcrafter.openai.util.OpenAIDslMarker

data class ModifyThreadRequest(
    var metadata: MutableMap<String, String>?,
) {
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var metadata: MutableMap<String, String>? = null

        fun metadata(metadata: MutableMap<String, String>) = apply {
            BuilderHelper.assertMetadata(metadata)
            this.metadata = metadata
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
    }
}