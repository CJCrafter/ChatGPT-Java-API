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
    val initialPrompt = "Follow the users instructions"
    val messages = mutableListOf(initialPrompt.toSystemMessage())
    val request = ChatRequest("gpt-3.5-turbo", messages)
    val bot = ChatBot(key)

    while (true) {
        println("Enter text below:\n")
        val input = scan.nextLine()

        // Generate a response, and print it to the user.
        messages.add(input.toUserMessage())
        bot.streamResponseKotlin(request) {
            print(choices[0].delta)

            if (choices[0].finishReason != null) {
                messages.add(choices[0].message)
            }
        }
    }
}