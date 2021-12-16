plugins {
    id("nl.ricoapon.java-common-conventions")
}

dependencies {
    implementation("org.yaml:snakeyaml:1.29")

    // I am too lazy to create test fixtures. Just add the code to source!
    implementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    implementation("org.junit.jupiter:junit-jupiter-params")
}
