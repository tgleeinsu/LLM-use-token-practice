plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

kotlin {
    jvmToolchain(17)
}

application {
    // Main.kt(package llmintro) 의 top-level main → llmintro.MainKt
    mainClass.set("llmintro.MainKt")
}

// Gradle 의 run 태스크는 기본적으로 stdin 을 연결하지 않는다.
// 연결해줘야 실험 중 readln() 이 콘솔 입력을 실제로 받는다.
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
