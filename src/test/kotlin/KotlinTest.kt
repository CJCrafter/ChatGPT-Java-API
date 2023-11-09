import com.cjcrafter.openai.FinishReason
import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.OpenAIImpl
import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.tool.FunctionTool
import com.cjcrafter.openai.chat.tool.ToolType
import com.cjcrafter.openai.completions.CompletionRequest
import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import org.intellij.lang.annotations.Language
import java.util.*
import java.util.concurrent.TimeUnit

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

fun main() {
    val scanner = Scanner(System.`in`)

    // Print out the menu of options
    println("""
            ${GREEN}Please select one of the options below by typing a number.
                1. Completion (create)
                2. Completion (stream)
                3. Chat (create)
                4. Chat (stream)
        """.trimIndent()
    )

    when (scanner.nextLine().trim()) {
        "1" -> doCompletion(stream = false)
        "2" -> doCompletion(stream = true)
        "3" -> doChat(stream = false)
        "4" -> doChat(stream = true)
        else -> System.err.println("Invalid option")
    }
}

fun doCompletion(stream: Boolean) {
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
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = OpenAIImpl(key)
    println(RESET + "Generating Response" + PURPLE)

    // Generate a print the completion
    if (stream) {
        for (chunk in openai.streamCompletion(request))
            print(chunk[0].text)

    } else {
        println(openai.createCompletion(request)[0].text)
    }
}

fun doChat(stream: Boolean) {
    val scan = Scanner(System.`in`)

    // This is the prompt that the bot will refer back to for every message.
    val prompt = "Be helpful ChatBot".toSystemMessage()

    // Use a mutable (modifiable) list! Always! You should be reusing the
    // ChatRequest variable, so in order for a conversation to continue
    // you need to be able to modify the list.
    val messages: MutableList<ChatMessage> = ArrayList(listOf(prompt))

    // ChatRequest is the request we send to OpenAI API. You can modify the
    // model, temperature, maxTokens, etc. This should be saved, so you can
    // reuse it for a conversation.
    val request = ChatRequest.builder()
        .model("gpt-3.5-turbo")
        .messages(messages)
        .addTool(FunctionTool.builder()
            .name("measure_length")
            .description("Measures the length of some object/thing")
            .addEnumParameter("unit", mutableListOf("meters", "feet"), required = true)
            .addStringParameter("thing", "Some object to measure, like a door or tree", required = true)
            .build()
        )
        .build()

    // Loads the API key from the .env file in the root directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = OpenAI.builder()
        .apiKey(key)
        .client(OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        ).build()

    // The conversation lasts until the user quits the program
    while (true) {

        // Prompt the user to enter a response
        println("\n${YELLOW}Enter text below:\n")
        val input = scan.nextLine()

        // Add the newest user message to the conversation
        messages.add(input.toUserMessage())
        println(RESET + "Generating Response" + PURPLE)

        var finishReason: FinishReason? = null
        do {
            if (stream) {
                for (chunk in openai.streamChatCompletion(request)) {
                    chunk[0].delta?.content?.let { print(it) }
                    if (chunk[0].isFinished()) {
                        finishReason = chunk[0].finishReason
                        messages.add(chunk[0].message)
                    }
                }
            } else {
                val response = openai.createChatCompletion(request)[0]
                println(response.message.content)
                messages.add(response.message)
                finishReason = response.finishReason
            }

            if (finishReason == FinishReason.TOOL_CALLS) {
                for (tool in messages.last().toolCalls!!) {
                    if (tool.type != ToolType.FUNCTION)
                        continue

                    println("    $CYAN Called function ${tool.function.name} with arguments \n${tool.function.arguments.split("\n").joinToString("        \n")} $PURPLE")

                    val args = tool.function.tryParseArguments()
                    val length = get_length(args["thing"]!!.asText(), args["unit"]!!.asText())
                    messages.add(ChatMessage(
                        role = ChatUser.TOOL,
                        content = length,
                        toolCallId = tool.id,
                    ))
                }
            }
        } while (finishReason == FinishReason.TOOL_CALLS)
    }
}

fun get_length(thing: String, unit: String): String {
    @Language("JSON")
    val json = """{"thing": "$thing", "unit": "$unit", "length": 3}"""
    return json
}