import com.cjcrafter.openai.chat.ChatBot
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.ChatRequest
import io.github.cdimascio.dotenv.dotenv
import java.util.*

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)
    val key = dotenv()["OPENAI_TOKEN"]

    // Create the initial prompt, we will reuse it later.
    val initialPrompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database."
    val messages = mutableListOf(initialPrompt.toSystemMessage())
    val request = ChatRequest("gpt-3.5-turbo", messages)
    val bot = ChatBot(key)

    while (true) {
        println("Enter text below:\n")
        val input = scan.nextLine()

        // Generate a response, and print it to the user.
        messages.add(input.toUserMessage())
        val response = bot.generateResponse(request)
        println("\n${response.choices[0].message.content}\n")

        // Save the generated message to the bot's conversational memory
        messages.add(response.choices[0].message)
    }
}