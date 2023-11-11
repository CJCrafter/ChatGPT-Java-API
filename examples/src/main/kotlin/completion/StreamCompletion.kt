package completion

import com.cjcrafter.openai.completions.completionRequest
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv

/**
 * In this Kotlin example, we will be using the Completions API to generate a
 * response. We will stream the tokens 1 at a time for a faster response time.
 */
fun main() {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = openAI { apiKey(key) }

    // Here you can change the model's settings, add tools, and more.
    val request = completionRequest {
        model("davinci")
        prompt("The wheels on the bus go")
        maxTokens(500)
    }

    for (chunk in openai.streamCompletion(request)) {
        print(chunk.choices[0].text)
    }
}
