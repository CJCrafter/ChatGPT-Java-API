package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.util.BuilderHelper
import com.cjcrafter.openai.util.OpenAIDslMarker

/**
 * A data class which represents a request to modify a [Run].
 *
 * @property metadata The metadata to associate with the run
 */
data class ModifyRunRequest(
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
         * Add metadata to the run. Useful for attaching data to a run.
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

    companion object {

        /**
         * Creates a new [Builder] for [ModifyRunRequest].
         */
        fun builder() = Builder()
    }
}