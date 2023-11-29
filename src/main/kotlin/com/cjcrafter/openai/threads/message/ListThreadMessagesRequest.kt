package com.cjcrafter.openai.threads.message

import com.cjcrafter.openai.AbstractListRequestBuilder
import com.cjcrafter.openai.ListOrder
import com.cjcrafter.openai.threads.Thread
import com.cjcrafter.openai.util.OpenAIDslMarker
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a request to list [ThreadMessage]s in a [Thread]. If a thread has
 * too many messages, you may need to use the [after] or [before] parameters to
 * page through them via multiple requests.
 *
 * @property limit The maximum number of results to return, between 1 and 100 inclusive
 * @property order The order to return the list in
 * @property after The cursor to use for pagination
 * @property before The cursor to use for pagination
 * @constructor Create empty List thread messages request
 */
data class ListThreadMessagesRequest(
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
    class Builder internal constructor() : AbstractListRequestBuilder<ListThreadMessagesRequest>() {

        /**
         * Builds the [ListThreadMessagesRequest] object.
         */
        override fun build(): ListThreadMessagesRequest = ListThreadMessagesRequest(limit, order, after, before)
    }

    companion object {

        /**
         * Creates a new [ListThreadMessagesRequest] builder.
         */
        @JvmStatic
        fun builder() = Builder()
    }
}