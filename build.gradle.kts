import com.github.breadmoirai.githubreleaseplugin.GithubReleaseTask

group = "com.cjcrafter"
version = "1.3.0"

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.30.0"
    kotlin("jvm") version "1.7.20-RC"
    id("org.jetbrains.dokka") version "1.8.10" // KDoc Documentation Builder
    id("com.github.breadmoirai.github-release") version "2.4.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform()
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

nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    packageGroup = "com.cjcrafter"
    stagingProfileId = findProperty("OSSRH_ID").toString()
    username = findProperty("OSSRH_USERNAME").toString()
    password = findProperty("OSSRH_PASSWORD").toString()
    numberOfRetries = 30
    delayBetweenRetriesInMillis = 3000
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

// After publishing, the nexus plugin will automatically close and release
tasks.named("publish") {
    finalizedBy("closeAndReleaseRepository", "createGithubRelease")
}

tasks.register<GithubReleaseTask>("createGithubRelease").configure {
    // https://github.com/BreadMoirai/github-release-gradle-plugin
    owner.set("CJCrafter")
    repo.set("ChatGPT-Java-API")
    authorization.set("Token ${findProperty("GITHUB_TOKEN").toString()}")
    tagName.set("$version")
    targetCommitish.set("master")
    releaseName.set("$version")
    draft.set(false)
    prerelease.set(false)
    generateReleaseNotes.set(true)
    body.set(""" 
For Gradle projects, add this to your `build.gradle` file in the dependencies block:
```groovy
dependencies {
    implementation 'com.cjcrafter:openai:$version'
}
```
Or, if you are using Kotlin DSL (`build.gradle.kts`), add this to your dependencies block:
```kotlin
dependencies {
    implementation("com.cjcrafter:openai:$version")
}
```
For Maven projects, add this to your `pom.xml` file in the `<dependencies>` block:
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>openai</artifactId>
    <version>$version</version>
</dependency>
```
See the [maven repository](https://central.sonatype.com/artifact/com.cjcrafter/openai/$version) for gradle/ant/etc.
    """.trimIndent())
    overwrite.set(false)
    allowUploadToExisting.set(false)
    apiEndpoint.set("https://api.github.com")

    setReleaseAssets(/* empty */)

    // If set to true, you can debug that this would do
    dryRun.set(false)
}