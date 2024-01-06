plugins {
    `java-library`
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation("ch.qos.logback:logback-classic:1.4.12")

    // https://mvnrepository.com/artifact/org.mariuszgromada.math/MathParser.org-mXparser
    // Used for tool tests
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")
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