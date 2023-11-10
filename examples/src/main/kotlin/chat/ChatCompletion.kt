package chat

import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv
import java.util.*

/**
 * In this Kotlin example, we will be using the Chat API to create a simple chatbot.
 */
fun main() {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = openAI { apiKey(key) }

    // Here you can change the model's settings, add tools, and more.
    val request = chatRequest {
        model("gpt-3.5-turbo")
        addMessage("Help the user with their problem.".toSystemMessage())
    }

    val messages: MutableList<ChatMessage> = request.messages // We'll update this list as the conversation continues
    val scan = Scanner(System.`in`)
    while (true) {
        println("What are you having trouble with?")
        val input = scan.nextLine()
        messages.add(input.toUserMessage())
        println("Generating Response...")

        val completion = openai.createChatCompletion(request)[0]
        println(completion.message.content)

        messages.add(completion.message)
    }
}
