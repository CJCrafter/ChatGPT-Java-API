import com.github.breadmoirai.githubreleaseplugin.GithubReleaseTask

group = "com.cjcrafter"
version = "2.1.1"

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.dokka") version "1.8.10" // KDoc Documentation Builder
    id("com.github.breadmoirai.github-release") version "2.4.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")

    implementation("org.slf4j:slf4j-api:2.0.9")

    implementation("org.jetbrains:annotations:24.0.1")

    testImplementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
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

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(System.getenv("OSSRH_USERNAME") ?: findProperty("OSSRH_USERNAME").toString())
            password.set(System.getenv("OSSRH_PASSWORD") ?: findProperty("OSSRH_PASSWORD").toString())
        }
    }
}

signing {
    isRequired = true
    useInMemoryPgpKeys(
        System.getenv("SIGNING_KEY_ID") ?: findProperty("SIGNING_KEY_ID").toString(),
        System.getenv("SIGNING_PRIVATE_KEY") ?: findProperty("SIGNING_PRIVATE_KEY").toString(),
        System.getenv("SIGNING_PASSWORD") ?: findProperty("SIGNING_PASSWORD").toString(),
    )
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
}

// After publishing, the nexus plugin will automatically close and release
tasks.named("publishToSonatypeRepository") {
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