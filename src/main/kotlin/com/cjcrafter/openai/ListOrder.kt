package com.cjcrafter.openai

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the order for a list, sorted by time created. In general, since
 * you probably want the most recent objects from the OpenAI API, you should
 * use [DESCENDING] (which is the default value for all requests).
 *
 * @property jsonProperty How each enum is represented as raw json string.
 */
enum class ListOrder(val jsonProperty: String) {

    /**
     * Ascending order. Objects created a long time ago are ordered before
     * objects created more recently.
     */
    @JsonProperty("asc")
    ASCENDING(jsonProperty = "asc"),

    /**
     * Descending order. Objects created more recently are ordered before
     * objects created a long time ago. This is the default value for list
     * requests.
     */
    @JsonProperty("desc")
    DESCENDING(jsonProperty = "desc"),
}