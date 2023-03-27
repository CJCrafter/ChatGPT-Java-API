import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toUserMessage
import com.cjcrafter.openai.chat.ChatRequest
import io.github.cdimascio.dotenv.dotenv
import java.util.*

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)

    // This is the prompt that the bot will refer back to for every message.
    val prompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database."

    // Use a mutable (modifiable) list! Always! You should be reusing the
    // ChatRequest variable, so in order for a conversation to continue you
    // need to be able to modify the list.
    val messages = mutableListOf(prompt.toSystemMessage())

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
        println("Enter text below:\n")
        val input = scan.nextLine()

        // Add the newest user message to the conversation
        messages.add(input.toUserMessage())

        // Use the OpenAI API to generate a response to the current
        // conversation. Print the resulting message.
        val response = openai.createChatCompletion(request)
        println("\n${response[0].message.content}\n")

        // Save the generated message to the conversational memory. It is
        // crucial to save this message, otherwise future requests will be
        // confused that there was no response.
        messages.add(response[0].message)
    }
}