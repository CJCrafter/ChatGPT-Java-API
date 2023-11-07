package com.cjcrafter.openai

import com.cjcrafter.openai.gson.FinishReasonAdapter

/**
 * [FinishReason] wraps the possible reasons that a generation model may stop
 * generating tokens. For most **PROPER** use cases (see [best practices](https://platform.openai.com/docs/guides/chat/introduction)),
 * the finish reason will be [STOP]. When working with streams, finish reason
 * will be `null` since it has not completed the message yet.
 */
enum class FinishReason {

    /**
     * [STOP] is the most common finish reason, and it occurs when the model
     * completely generates its entire message, and has nothing else to add.
     * Ideally, you always want your finish reason to be [STOP].
     */
    STOP,

    /**
     * Occurs when ChatGPT is not able to finish the response within the token
     * limit. When the model reaches the token limit, it returns the incomplete
     * message with finish reason [LENGTH]. Some models have a higher token
     * limit than others.
     */
    LENGTH,

    /**
     * Occurs due to a flag from OpenAI's content filters. This occurrence is
     * rare, and tends to happen when you blatantly violate OpenAI's terms.
     */
    CONTENT_FILTER,

    /**
     * Occurs when the model uses one of the available tools.
     */
    TOOL_CALLS,

    @Deprecated("functions have been replaced by tools")
    FUNCTION_CALL;

    companion object {

        /**
         * Returns the google gson adapter for serializing this enum to a json
         * file. Whe
         *
         * @return
         */
        @JvmStatic
        fun adapter() : FinishReasonAdapter {
            return FinishReasonAdapter()
        }
    }
}