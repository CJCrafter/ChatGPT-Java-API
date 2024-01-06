package com.cjcrafter.openai

import com.cjcrafter.openai.assistants.Assistant

/**
 * An abstract builder type for list requests. Many objects stored by the OpenAI
 * API are stored in lists. This abstract request stores the data that each request
 * has in common.
 */
abstract class AbstractListRequestBuilder<T> {

    protected var limit: Int? = null
    protected var order: ListOrder? = null
    protected var after: String? = null
    protected var before: String? = null

    /**
     * The maximum number of results to return. This value must be between
     * 1 and 100 (inclusive).
     *
     * @param limit The maximum number of results to return
     * @throws IllegalArgumentException If the limit is not between 1 and 100
     */
    fun limit(limit: Int?) = apply {
        if (limit != null && (limit < 1 || limit > 100))
            throw IllegalArgumentException("Limit must be between 1 and 100")
        this.limit = limit
    }

    /**
     * How the returned list should be ordered. If not specified, the default
     * value is [ListOrder.DESCENDING]. Use the [ascending] and [descending]
     * methods.
     *
     * @param order The order to return the list in
     */
    fun order(order: ListOrder?) = apply { this.order = order }

    /**
     * A cursor for use in pagination. `after` is an object ID that defines
     * your place in the list. For instance, if you make a list request and
     * receive 100 objects, ending with `"obj_foo"`, your subsequent call
     * can include `after="obj_foo"` in order to fetch the next page of the
     * list.
     *
     * @param after The cursor to use for pagination
     */
    fun after(after: String?) = apply { this.after = after }

    /**
     * A cursor for use in pagination. `after` is an object ID that defines
     * your place in the list. For instance, if you make a list request and
     * receive 100 objects, ending with `"obj_foo"`, your subsequent call
     * can include `after="obj_foo"` in order to fetch the next page of the
     * list.
     *
     * @param after The cursor to use for pagination
     */
    fun after(after: Assistant) = apply { this.after = after.id }

    /**
     * A cursor for use in pagination. `before` is an object ID that defines
     * your place in the list. For instance, if you make a list request and
     * receive 100 objects, ending with `"obj_foo"`, your subsequent call can
     * include `before="obj_foo"` in order to fetch the previous page of the
     * list.
     *
     * @param before The cursor to use for pagination
     */
    fun before(before: String?) = apply { this.before = before }

    /**
     * A cursor for use in pagination. `before` is an object ID that defines
     * your place in the list. For instance, if you make a list request and
     * receive 100 objects, ending with `"obj_foo"`, your subsequent call can
     * include `before="obj_foo"` in order to fetch the previous page of the
     * list.
     *
     * @param before The cursor to use for pagination
     */
    fun before(before: Assistant) = apply { this.before = before.id }

    /**
     * Sets the order to [ListOrder.ASCENDING].
     */
    fun ascending() = apply { this.order = ListOrder.ASCENDING }

    /**
     * Sets the order to [ListOrder.DESCENDING].
     */
    fun descending() = apply { this.order = ListOrder.DESCENDING }

    abstract fun build(): T
}