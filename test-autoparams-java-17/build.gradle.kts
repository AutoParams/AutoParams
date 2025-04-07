plugins {
    id("java-library")
}

dependencies {
    testImplementation(project(":autoparams"))
    testImplementation("org.assertj:assertj-core:3.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    dependsOn("checkstyleMain", "checkstyleTest")
    useJUnitPlatform()
}
