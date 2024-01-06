package chat

import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.tool.FunctionToolCall
import com.cjcrafter.openai.chat.tool.Tool
import com.cjcrafter.openai.chat.tool.ToolCall
import com.cjcrafter.openai.exception.HallucinationException
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License
import java.util.*

/**
 * In this Kotlin example, we will be using the Chat API to create a simple chatbot.
 * Instead of waiting for the full response to generate, we will "stream" tokens
 * 1 by 1 as they are generated. We will also add a Math tool so that the chatbot
 * can solve math problems with a math parser.
 */
fun main() {

    // Use mXparser
    License.iConfirmNonCommercialUse("CJCrafter")

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = openAI { apiKey(key) }

    // Here you can change the model's settings, add tools, and more.
    val request = chatRequest {
        model("gpt-3.5-turbo")
        addMessage("Help the user with their problem.".toSystemMessage())

        function {
            name("solve_math_problem")
            description("Returns the result of a math problem as a double")
            addStringParameter("equation", "The math problem for you to solve", true)
        }
    }

    val messages: MutableList<ChatMessage> = request.messages // We'll update this list as the conversation continues
    val scan = Scanner(System.`in`)
    while (true) {
        println("What are you having trouble with?")
        val input = scan.nextLine()
        messages.add(input.toUserMessage())
        println("Generating Response...")

        // We use a do-while loop since we always need to loop at least once,
        // and ChatGPT may chain function calls. We need to handle those too.
        do {
            var madeToolCall = false
            for (chunk in openai.streamChatCompletion(request)) {
                val delta = chunk[0].deltaContent
                if (delta != null) print(delta)

                // When the response is finished, we can add it to the messages list.
                if (chunk[0].isFinished())
                    messages.add(chunk[0].message)
            }

            // If the API returned a tool call to us, we need to handle it.
            val toolCalls = messages.last().toolCalls
            if (toolCalls != null) {
                madeToolCall = true
                for (call in toolCalls) {
                    val response = handleToolCall(call, request.tools)
                    messages.add(response)
                }
            }

            // Loop until we get a message without tool calls
        } while (madeToolCall)

        // Print a new line to separate the messages
        println()
    }
}

fun handleToolCall(call: ToolCall, validTools: List<Tool>?): ChatMessage {
    // The try-catch here is *crucial*. ChatGPT *isn't very good*
    // at tool calls (And you probably aren't very good at prompt
    // engineering yet!). OpenAI will often "Hallucinate" arguments.
    return try {
        if (call.type !== Tool.Type.FUNCTION)
            throw HallucinationException("Unknown tool call type: " + call.type)

        val function = (call as FunctionToolCall).function
        val arguments = function.tryParseArguments(validTools) // You can pass null here for less strict parsing
        val equation = arguments["equation"]!!.asText()
        val result = solveEquation(equation)

        // NaN implies that the equation was invalid
        if (java.lang.Double.isNaN(result))
            throw HallucinationException("Format was invalid: $equation")

        // Add the result to the messages list
        val json = "{\"result\": $result}"
        ChatMessage(ChatUser.TOOL, json, toolCallId = call.id)
    } catch (ex: HallucinationException) {

        // Lets let ChatGPT know it made a mistake so it can correct itself
        val json = "{\"error\": \"" + ex.message + "\"}"
        ChatMessage(ChatUser.TOOL, json, toolCallId = call.id)
    }
}

fun solveEquation(equation: String?): Double {
    val expression = Expression(equation)
    return expression.calculate()
}
