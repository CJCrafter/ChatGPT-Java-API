package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.AbstractListRequestBuilder
import com.cjcrafter.openai.ListOrder
import com.cjcrafter.openai.util.OpenAIDslMarker
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a request to list all runs for a Thread. If a thread has too many
 * runs, you may need to use the [after] or [before] parameters to page through
 * them via multiple requests.
 *
 * @property limit The maximum number of results to return, between 1 and 100 inclusive
 * @property order The order to return the list in
 * @property after The cursor to use for pagination
 * @property before The cursor to use for pagination
 */
data class ListRunsRequest(
    var limit: Int? = null,
    var order: ListOrder? = null,
    var after: String? = null,
    var before: String? = null,
) {

    /**
     * Converts the request to a map of query parameters.
     */
    @ApiStatus.Internal
    fun toMap(): Map<String, Any> = buildMap {
        if (limit != null) put("limit", limit!!)
        if (order != null) put("order", order!!.jsonProperty)
        if (after != null) put("after", after!!)
        if (before != null) put("before", before!!)
    }

    @OpenAIDslMarker
    class Builder internal constructor(): AbstractListRequestBuilder<ListRunsRequest>() {

        /**
         * Builds the [ListRunsRequest] object.
         */
        override fun build(): ListRunsRequest = ListRunsRequest(limit, order, after, before)
    }

    companion object {

        /**
         * Creates a new [ListRunsRequest] builder.
         */
        @JvmStatic
        fun builder(): Builder = Builder()
    }
}