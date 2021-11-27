plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        // When not setting this variable, you get the following warning during building: 'compileJava' task (current
        // target is 17) and 'compileKotlin' task (current target is 1.8) jvm target compatibility should be set to the same Java version.
        // To solve this issue, we should set the target to 17. However, the current version of Gradle doesn't seem to
        // use the latest Kotlin version that supports 17. Setting it to 16 fixed the warning for now. Later we should
        // change it to 17 when that is supported.
        jvmTarget = "16"
    }
}
