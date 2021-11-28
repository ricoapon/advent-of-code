plugins {
    id("nl.ricoapon.java-common-conventions")
    application
}

application {
    mainClass.set("nl.ricoapon.cli.Main")
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
}
