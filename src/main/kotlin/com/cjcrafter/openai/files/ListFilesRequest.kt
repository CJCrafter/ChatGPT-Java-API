package com.cjcrafter.openai.files

import com.cjcrafter.openai.OpenAI
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a request to the [list files](https://platform.openai.com/docs/api-reference/files/list)
 * endpoint.
 *
 * @property purpose Only return files with the given purpose.
 */
data class ListFilesRequest internal constructor(
    val purpose: FilePurpose? = null,
) {

    /**
     * Converts this object to a map for URL query parameters.
     */
    @ApiStatus.Internal
    fun toMap(): Map<String, Any> {
        return buildMap {
            if (purpose != null) put("purpose", OpenAI.createObjectMapper().writeValueAsString(purpose))
        }
    }

    /**
     * A builder design pattern for constructing an [ListFilesRequest] instance.
     */
    class Builder internal constructor() {
        private var purpose: FilePurpose? = null

        fun purpose(purpose: FilePurpose) = apply { this.purpose = purpose }

        fun build(): ListFilesRequest {
            return ListFilesRequest(
                purpose = purpose
            )
        }
    }

    companion object {

        /**
         * Returns a builder to construct an [ListFilesRequest] instance.
         */
        @JvmStatic
        fun builder() = Builder()
    }
}