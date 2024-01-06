package files

import com.cjcrafter.openai.files.FilePurpose
import com.cjcrafter.openai.files.uploadFileRequest
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv
import java.io.File

// To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
// dependency. Then you can add a .env file in your project directory.
val openai = openAI { apiKey(dotenv()["OPENAI_TOKEN"]) }

fun main() {
    do {
        println("""
            1. List files
            2. Upload file
            3. Delete file
            4. Retrieve file
            5. Retrieve file contents
            6. Exit
        """.trimIndent())
        print("Enter your choice: ")
        val choice = readln().toInt()
        when (choice) {
            1 -> listFiles()
            2 -> uploadFile()
            3 -> deleteFile()
            4 -> retrieveFile()
            5 -> retrieveFileContents()
        }
    } while (choice != 6)
}

fun listFiles() {
    val response = openai.files.list()
    println(response)
}

fun uploadFile() {
    print("Enter the file name: ")
    val fileName = readln()
    val input = File(fileName)
    val request = uploadFileRequest {
        file(input)
        purpose(FilePurpose.ASSISTANTS)
    }
    val response = openai.files.upload(request)
    println(response)
}

fun deleteFile() {
    print("Enter the file id: ")
    val fileId = readln()
    val response = openai.files.delete(fileId)
    println(response)
}

fun retrieveFile() {
    print("Enter the file id: ")
    val fileId = readln()
    val response = openai.files.retrieve(fileId)
    println(response)
}

fun retrieveFileContents() {
    print("Enter the file id: ")
    val fileId = readln()
    val contents = openai.files.retrieveContents(fileId)
    println(contents)
}