import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.ChatRequest
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.exception.OpenAIError
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import java.util.function.Consumer

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
    openai.streamChatCompletion(request, {
        print(it[0].delta)

        // Once the message is complete, we should save the message to our
        // conversation (In case you want to generate more responses).
        if (it[0].finishReason != null)
            messages.add(it[0].message)
    })
}
