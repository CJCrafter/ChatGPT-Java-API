import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.chat.ChatMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.ChatRequest
import com.cjcrafter.openai.chat.ChatResponse
import com.cjcrafter.openai.chat.ChatResponseChunk
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.exception.OpenAIError
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import java.util.*

object KotlinTest {

    // Colors for pretty formatting
    const val RESET = "\u001b[0m"
    const val BLACK = "\u001b[0;30m"
    const val RED = "\u001b[0;31m"
    const val GREEN = "\u001b[0;32m"
    const val YELLOW = "\u001b[0;33m"
    const val BLUE = "\u001b[0;34m"
    const val PURPLE = "\u001b[0;35m"
    const val CYAN = "\u001b[0;36m"
    const val WHITE = "\u001b[0;37m"

    @Throws(OpenAIError::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)

        // Print out the menu of options
        println("""$GREEN
            Please select one of the options below by typing a number.
            
                1. Completion (create, sync)
                2. Completion (stream, sync)
                3. Completion (create, async)
                4. Completion (stream, async)
                5. Chat (create, sync)
                6. Chat (stream, sync)
                7. Chat (create, async)
                8. Chat (stream, async)  
        """.trimIndent())

        when (scanner.nextLine().trim()) {
            "1" -> doCompletion(stream = false, async = false)
            "2" -> doCompletion(stream = true, async = false)
            "3" -> doCompletion(stream = false, async = true)
            "4" -> doCompletion(stream = true, async = true)
            "5" -> doChat(stream = false, async = false)
            "6" -> doChat(stream = true, async = false)
            "7" -> doChat(stream = false, async = true)
            "8" -> doChat(stream = true, async = true)
            else -> System.err.println("Invalid option")
        }
    }

    @Throws(OpenAIError::class)
    fun doCompletion(stream: Boolean, async: Boolean) {
        val scan = Scanner(System.`in`)
        println(YELLOW + "Enter completion: ")
        val input = scan.nextLine()

        // CompletionRequest contains the data we sent to the OpenAI API. We use
        // 128 tokens, so we have a bit of a delay before the response (for testing).
        val request = CompletionRequest.builder()
            .model("davinci")
            .prompt(input)
            .maxTokens(128).build()

        // Loads the API key from the .env file in the root directory.
        val key = Dotenv.load()["OPENAI_TOKEN"]
        val openai = OpenAI(key)
        println(RESET + "Generating Response" + PURPLE)
        if (stream) {
            if (async)
                openai.streamCompletionAsync(request, { print(it[0].text) })
            else
                openai.streamCompletion(request, { print(it[0].text) })
        } else {
            if (async)
                openai.createCompletionAsync(request, { println(it[0].text) })
            else
                println(openai.createCompletion(request)[0].text)
        }
        println("$CYAN  !!! Code has finished executing. Wait for async code to complete.$RESET")
    }

    @Throws(OpenAIError::class)
    fun doChat(stream: Boolean, async: Boolean) {
        val scan = Scanner(System.`in`)

        // This is the prompt that the bot will refer back to for every message.
        val prompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.".toSystemMessage()

        // Use a mutable (modifiable) list! Always! You should be reusing the
        // ChatRequest variable, so in order for a conversation to continue
        // you need to be able to modify the list.
        val messages: MutableList<ChatMessage> = ArrayList(listOf(prompt))

        // ChatRequest is the request we send to OpenAI API. You can modify the
        // model, temperature, maxTokens, etc. This should be saved, so you can
        // reuse it for a conversation.
        val request = ChatRequest(model="gpt-3.5-turbo", messages=messages)

        // Loads the API key from the .env file in the root directory.
        val key = dotenv()["OPENAI_TOKEN"]
        val openai = OpenAI(key)

        // The conversation lasts until the user quits the program
        while (true) {

            // Prompt the user to enter a response
            println("\n${YELLOW}Enter text below:\n")
            val input = scan.nextLine()

            // Add the newest user message to the conversation
            messages.add(input.toUserMessage())
            println(RESET + "Generating Response" + PURPLE)
            if (stream) {
                if (async) {
                    openai.streamChatCompletionAsync(request, { response: ChatResponseChunk ->
                        print(response[0].delta)
                        if (response[0].isFinished()) messages.add(response[0].message)
                    })
                } else {
                    openai.streamChatCompletion(request, { response: ChatResponseChunk ->
                        print(response[0].delta)
                        if (response[0].isFinished()) messages.add(response[0].message)
                    })
                }
            } else {
                if (async) {
                    openai.createChatCompletionAsync(request, { response: ChatResponse ->
                        println(response[0].message.content)
                        messages.add(response[0].message)
                    })
                } else {
                    val response = openai.createChatCompletion(request)
                    println(response[0].message.content)
                    messages.add(response[0].message)
                }
            }
            println("$CYAN  !!! Code has finished executing. Wait for async code to complete.")
        }
    }
}