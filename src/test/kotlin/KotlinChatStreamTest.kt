import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.ChatRequest
import io.github.cdimascio.dotenv.dotenv
import java.util.*

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)

    // Prepare the ChatRequest
    val prompt = "Be as unhelpful as possible"
    val messages = mutableListOf(prompt.toSystemMessage())
    val request = ChatRequest(model="gpt-3.5-turbo", messages=messages)

    // Loads the API key from the .env file in the root directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = OpenAI(key)

    // Ask the user for input
    println("Enter text below:\n")
    val input = scan.nextLine()

    // Generate a response, and print it to the user.
    messages.add(input.toUserMessage())
    openai.streamChatCompletionKotlin(request) {
        print(choices[0].delta)

        // Once the message is complete, we should save the message to our
        // conversation (In case you want to generate more responses).
        if (choices[0].finishReason != null)
            messages.add(choices[0].message)
    }
}