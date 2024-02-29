package com.cjcrafter.openai.moderations

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A moderation object returned by the moderations api.
 *
 * @property id The id of the moderation request. Always starts with "modr-".
 * @property model The model which was used to moderate the content.
 * @property results The results of the moderation request.
 * @constructor Create empty Moderation
 */
data class Moderation(
    @JsonProperty(required = true) val id: String,
    @JsonProperty(required = true) val model: String,
    @JsonProperty(required = true) val results: Results,
) {
    /**
     * The results of the moderation request.
     *
     * @property flagged If any categories were flagged.
     * @property categories The categories that were flagged.
     * @property categoryScores The scores of each category.
     * @constructor Create empty Results
     */
    data class Results(
        @JsonProperty(required = true) val flagged: Boolean,
        @JsonProperty(required = true) val categories: Map<String, Boolean>,
        @JsonProperty("category_scores", required = true) val categoryScores: Map<String, Double>,
    )
}