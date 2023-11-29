package com.cjcrafter.openai.util

object BuilderHelper {

    const val MAX_METADATA_SIZE = 16
    const val MAX_METADATA_KEY_LENGTH = 64
    const val MAX_METADATA_VALUE_LENGTH = 512

    /**
     * Asserts the given metadata key-value pair is valid, and can be sent to
     * the OpenAI API.
     *
     * @param key The key, which must be <= 64 characters
     * @param value The value, which must be <= 512 characters
     * @throws IllegalArgumentException If the key or value is too long
     */
    fun assertMetadata(key: String, value: String) {
        if (key.length > MAX_METADATA_KEY_LENGTH)
            throw IllegalArgumentException("key must be <= $MAX_METADATA_KEY_LENGTH characters, got ${key.length}: $key")
        if (value.length > MAX_METADATA_VALUE_LENGTH)
            throw IllegalArgumentException("value must be <= $MAX_METADATA_VALUE_LENGTH characters, got ${value.length}: $value")
    }

    /**
     * Asserts the given metadata map contains valid key-value pairs (see
     * [assertMetadata]), and that the map has <= 16 key-value pairs.
     *
     * @param metadata The map of metadata to validate
     * @throws IllegalArgumentException If the map has too many key-value pairs,
     * or if any of the key-value pairs are invalid
     */
    fun assertMetadata(metadata: Map<String, String>) {
        if (metadata.size > MAX_METADATA_SIZE)
            throw IllegalArgumentException("metadata must have <= $MAX_METADATA_SIZE key-value pairs, got ${metadata.size}")
        metadata.forEach { (key, value) -> assertMetadata(key, value) }
    }

    /**
     * Throws an exception if no more metadata can be added to the given map.
     *
     * @param metadata The map to check
     * @throws IllegalArgumentException If the map has 16 key-value pairs already
     */
    fun tryAddMetadata(metadata: Map<String, String>) {
        if (metadata.size == MAX_METADATA_SIZE)
            throw IllegalArgumentException("Tried to add metadata to a map with $MAX_METADATA_SIZE key-value pairs")
    }
}