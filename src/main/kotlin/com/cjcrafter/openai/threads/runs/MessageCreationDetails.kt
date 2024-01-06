package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.threads.message.MessageHandler
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the details of a [RunStep.Type.MESSAGE_CREATION] step. This
 * stores the ID of the message that was created. You can use this ID to get
 * the message object from the [MessageHandler.retrieve] function.
 *
 * @property messageCreation The data for the message creation
 */
data class MessageCreationDetails(
    @JsonProperty("message_creation", required = true) val messageCreation: MessageCreation
) : RunStep.Details() {

    override val type = RunStep.Type.MESSAGE_CREATION

    /**
     * Holds the ID of the message that was created.
     *
     * @property messageId The ID of the message that was created. You can use
     * this ID to get the message object from the [MessageHandler.retrieve] function
     */
    data class MessageCreation(
        @JsonProperty("message_id", required = true) val messageId: String
    )
}