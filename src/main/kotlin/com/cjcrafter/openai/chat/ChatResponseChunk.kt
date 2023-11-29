package com.cjcrafter.openai.chat

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

/**
 * The [ChatResponseChunk] contains all the data returned by the OpenAI Chat API.
 * For most use cases, [ChatResponseChunk.get] (passing 0 to the index argument)
 * is all you need.
 *
 * This class is similar to [ChatResponse], except with [ChatResponseChunk] you
 * determine the number of generated tokens.
 *
 * @property id      The unique id for your request.
 * @property created The Unix timestamp (measured in seconds since 00:00:00 UTC on January 1, 1970) when the API response was created.
 * @property choices The list of generated messages.
 * @constructor Create Chat response (for internal usage).
 * @see ChatResponse
 * @since 1.2.0
 */
data class ChatResponseChunk(
    @JsonProperty(required = true) val id: String,
    @JsonProperty(required = true) val created: Long,
    @JsonProperty(required = true) val choices: List<ChatChoiceChunk>,
) {
    internal fun update(json: ObjectNode) {
        val choicesArray = json.get("choices") as? ArrayNode
        choicesArray?.forEachIndexed { index, jsonNode ->
            choices[index].update(jsonNode)
        }
    }

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

    // TODO add tokenizier so we can determine token count

    /**
     * Shorthand for accessing the generated messages (shorthand for
     * [ChatResponseChunk.choices]).
     *
     * @param index The index of the message (`0` for most use cases).
     * @return The generated [ChatChoiceChunk] at the index.
     */
    operator fun get(index: Int): ChatChoiceChunk {
        return choices[index]
    }
}