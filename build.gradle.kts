import com.github.breadmoirai.githubreleaseplugin.GithubReleaseTask

group = "com.cjcrafter"
version = "1.0.0"

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.github.breadmoirai.github-release") version "2.4.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.google.code.gson:gson:2.10.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(8)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}

// Create javadocJar and sourcesJar tasks
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.named("javadoc"))
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

// Signing artifacts
signing {
    isRequired = true
    //useGpgCmd()

    useInMemoryPgpKeys(
        findProperty("SIGNING_KEY_ID").toString(),
        findProperty("SIGNING_PRIVATE_KEY").toString(),
        findProperty("SIGNING_PASSWORD").toString()
    )
    //sign(configurations["archives"])
    sign(publishing.publications)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(javadocJar)
            artifact(sourcesJar)

            pom {
                name.set("OpenAI Java API")
                description.set("Access OpenAI's API without the raw JSON/HTTPS requests")
                url.set("https://github.com/CJCrafter/ChatGPT-Java-API")

                groupId = "com.cjcrafter"
                artifactId = "openai"

                licenses {
                    license {
                        name.set("The MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("CJCrafter")
                        name.set("Collin Barber")
                        email.set("collinjbarber@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/CJCrafter/ChatGPT-Java-API.git")
                    developerConnection.set("scm:git:ssh://github.com/CJCrafter/ChatGPT-Java-API.git")
                    url.set("https://github.com/CJCrafter/ChatGPT-Java-API")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = findProperty("OSSRH_USERNAME").toString()
                password = findProperty("OSSRH_PASSWORD").toString()
            }
        }
    }
}

tasks.register<GithubReleaseTask>("createGithubRelease").configure {
    // https://github.com/BreadMoirai/github-release-gradle-plugin
    owner.set("CJCrafter")
    repo.set("ChatGPT-Java-API")
    authorization.set("Token ${findProperty("GITHUB_PASSWORD").toString()}")
    tagName.set(version.toString())
    targetCommitish.set("master")
    releaseName.set("v${version} BETA")
    draft.set(true)
    prerelease.set(false)
    generateReleaseNotes.set(true)
    body.set("")
    overwrite.set(false)
    allowUploadToExisting.set(false)
    apiEndpoint.set("https://api.github.com")

    setReleaseAssets(/* TODO */)

    // If set to true, you can debug that this would do
    dryRun.set(false)

    doFirst {
        println("Creating GitHub release")
    }
}