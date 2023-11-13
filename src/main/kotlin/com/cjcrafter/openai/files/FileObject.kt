package com.cjcrafter.openai.files

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

/**
 * Represents a [file uploaded to the OpenAI API](https://platform.openai.com/docs/api-reference/files/object).
 *
 * @property id The unique id of the file
 * @property bytes How large the file is in bytes
 * @property createdAt The unix timestamp this file wrapper was created
 * @property fileName The name of the file (with the extension at the end)
 * @property purpose The reason this file was uploaded
 * @constructor Create empty File object
 */
data class FileObject(
    val id: String,
    val bytes: Int,
    @JsonProperty("created_at") val createdAt: Int,
    @JsonProperty("filename") val fileName: String,
    val purpose: FilePurpose,
) {

    /**
     * Returns the [Instant] time that this file object was created.
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
        return Instant.ofEpochSecond(createdAt.toLong())
    }

    /**
     * Returns the time-zoned instant that this file object was created.
     * By default, this method uses the system's timezone.
     *
     * @param timezone The user's timezone.
     * @return The timezone adjusted date time.
     * @see TimeZone.getDefault
     */
    @JvmOverloads
    fun getZonedTime(timezone: ZoneId = TimeZone.getDefault().toZoneId()): ZonedDateTime {
        return ZonedDateTime.ofInstant(getTime(), timezone)
    }
}