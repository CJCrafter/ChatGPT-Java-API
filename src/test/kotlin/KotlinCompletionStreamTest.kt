import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.completions.CompletionRequest
import io.github.cdimascio.dotenv.dotenv

fun main(args: Array<String>) {

    // Prepare the ChatRequest
    val request = CompletionRequest(model="davinci", prompt="The wheels on the bus", maxTokens = 128)

    // Loads the API key from the .env file in the root directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = OpenAI(key)

    // Generate a response, and print it to the user
    //println(openai.createCompletion(request))
    val list = openai.streamCompletionKotlin(request) {
        print(choices[0].text)
    }
}