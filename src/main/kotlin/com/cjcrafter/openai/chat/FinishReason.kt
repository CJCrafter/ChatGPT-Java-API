package com.cjcrafter.openai.chat

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
     * [LENGTH] occurs when the bot is not able to finish the response within
     * its token limit. When it reaches the token limit, it sends the
     * incomplete message with finish reason [LENGTH]
     */
    LENGTH,

    /**
     * [TEMPERATURE] is a rare occurrence, and only happens when the
     * [ChatRequest.temperature] is low enough that it is impossible for the
     * model to continue generating text.
     */
    TEMPERATURE
}