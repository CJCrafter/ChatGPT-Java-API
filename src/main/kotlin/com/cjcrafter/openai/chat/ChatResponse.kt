package com.cjcrafter.openai.chat

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

/**
 * The `ChatResponse` contains all the data returned by the OpenAI Chat API.
 * For most use cases, [ChatResponse.get] (passing 0 to the index argument) is
 * all you need.
 *
 * @property id      The unique id for your request.
 * @property created The Unix timestamp (measured in seconds since 00:00:00 UTC on January 1, 1970) when the API response was created.
 * @property choices The list of generated messages.
 * @property usage   The number of tokens used in this request/response.
 * @constructor Create Chat response (for internal usage).
 */
data class ChatResponse(
    @JsonProperty(required = true) val id: String,
    @JsonProperty(required = true) val created: Long,
    @JsonProperty(required = true) val choices: List<ChatChoice>,
    @JsonProperty(required = true) val usage: ChatUsage
) {

    /**
     * Returns the [Instant] time that the OpenAI Chat API sent this response.
     * The time is measured as a unix timestamp (measured in seconds since
     * 00:00:00 UTC on January 1, 1970).
     *
     * Note that users expect time to be measured in their timezone, so
     * [getZonedTime] is preferred.
     *
     * @return The instant the api created this response.
     * @see getZonedTime
     */
    fun getTime(): Instant {
        return Instant.ofEpochSecond(created)
    }

    /**
     * Returns the time-zoned instant that the OpenAI Chat API sent this
     * response. By default, this method uses the system's timezone.
     *
     * @param timezone The user's timezone.
     * @return The timezone adjusted date time.
     * @see TimeZone.getDefault
     */
    @JvmOverloads
    fun getZonedTime(timezone: ZoneId = TimeZone.getDefault().toZoneId()): ZonedDateTime {
        return ZonedDateTime.ofInstant(getTime(), timezone)
    }

    /**
     * Shorthand for accessing the generated messages (shorthand for
     * [ChatResponse.choices]).
     *
     * @param index The index of the message (`0` for most use cases).
     * @return The generated [ChatChoice] at the index.
     */
    operator fun get(index: Int): ChatChoice {
        return choices[index]
    }
}