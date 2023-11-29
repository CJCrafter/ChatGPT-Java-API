package assistant

import com.cjcrafter.openai.assistants.createAssistantRequest
import com.cjcrafter.openai.assistants.modifyAssistantRequest
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv

// To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
// dependency. Then you can add a .env file in your project directory.
val openai = openAI { apiKey(dotenv()["OPENAI_TOKEN"]) }

fun main() {
    do {
        println("""
            1. Create
            2. Retrieve
            3. List
            4. Delete
            5. Modify
            6. Exit
        """.trimIndent())
        print("Choice: ")
        val choice = readln().toInt()
        when (choice) {
            1 -> create()
            2 -> retrieve()
            3 -> list()
            4 -> delete()
            5 -> modify()
        }
    } while (choice != 6)
}

fun create() {
    print("Model: ")
    val model = readln()
    print("Name: ")
    val name = readln()
    print("Description: ")
    val description = readln()
    print("Instructions: ")
    val instructions = readln()

    val request = createAssistantRequest {
        model(model)
        name(name)
        description(description)
        instructions(instructions)
    }

    println("Request: $request")
    val response = openai.assistants.create(request)
    println("Response: $response")
}

fun retrieve() {
    print("ID: ")
    val id = readln()

    val response = openai.assistants.retrieve(id)
    println("Response: $response")
}

fun list() {
    val response = openai.assistants.list()
    println("Response: $response")
}

fun delete() {
    print("ID: ")
    val id = readln()

    val response = openai.assistants.delete(id)
    println("Response: $response")
}

fun modify() {
    print("ID: ")
    val id = readln()
    print("Name: ")
    val name = readln()
    print("Description: ")
    val description = readln()
    print("Instructions: ")
    val instructions = readln()

    val request = modifyAssistantRequest {
        name(name)
        description(description)
        instructions(instructions)
    }

    println("Request: $request")
    val response = openai.assistants.modify(id, request)
    println("Response: $response")
}