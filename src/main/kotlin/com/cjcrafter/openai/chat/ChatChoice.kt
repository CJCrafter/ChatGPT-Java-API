package com.cjcrafter.openai.chat

import com.cjcrafter.openai.FinishReason
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The OpenAI API returns a list of `ChatChoice`. Each choice has a
 * generated message ([ChatChoice.message]) and a finish reason
 * ([ChatChoice.finishReason]). For most use cases, you only need the generated
 * message.
 *
 * By default, only 1 [ChatChoice] is generated (since [ChatRequest.n] == 1).
 * When you increase `n`, more options are generated. The more options you
 * generate, the more tokens you use. In general, it is best to **ONLY**
 * generate 1 response, and to let the user regenerate the response.
 *
 * @property index        The index in the array... 0 if [ChatRequest.n]=1.
 * @property message      The generated text.
 * @property finishReason The reason the bot stopped generating tokens.
 * @constructor Create a new chat choice, for internal usage.
 * @see FinishReason
 */
data class ChatChoice(
    @JsonProperty(required = true) val index: Int,
    @JsonProperty(required = true) val message: ChatMessage,
    @JsonProperty("finish_reason", required = true) val finishReason: FinishReason
)