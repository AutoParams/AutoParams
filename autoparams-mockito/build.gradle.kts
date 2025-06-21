plugins {
    id("java-library")
}

dependencies {
    api("io.github.autoparams:autoparams:[11.1.0, 12.0.0)")
    api("org.mockito:mockito-core:5.14.2")
    testImplementation("org.assertj:assertj-core:3.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    dependsOn("checkstyleMain", "checkstyleTest")
    useJUnitPlatform()
}
