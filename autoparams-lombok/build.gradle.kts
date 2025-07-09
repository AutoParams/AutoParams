plugins {
    id("java-library")
    id("io.freefair.lombok") version "8.4"
}

dependencies {
    api("io.github.autoparams:autoparams:[11.2.2, 12.0.0)")
    testImplementation("org.assertj:assertj-core:3.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.12.2")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    dependsOn("checkstyleMain", "checkstyleTest")
    useJUnitPlatform()
}
