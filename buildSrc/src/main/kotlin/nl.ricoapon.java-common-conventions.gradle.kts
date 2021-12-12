// This plugin contains the basic configuration that all projects in this workspace should use.

plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        // Not sure what the default Java version is, so we explicitly set the version.
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
