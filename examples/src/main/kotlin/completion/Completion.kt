package completion

import com.cjcrafter.openai.completions.completionRequest
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv

/**
 * In this Kotlin example, we will be using the Chat API to create a simple chatbot.
 */
fun main() {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = openAI { apiKey(key) }

    // Here you can change the model's settings, add tools, and more.
    val request = completionRequest {
        model("davinci")
        prompt("What is 9+10")
    }

    val completion = openai.createCompletion(request)[0]
    println(completion.text)
}
