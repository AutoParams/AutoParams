plugins {
    id("java-library")
}

dependencies {
    api("org.junit.jupiter:junit-jupiter-api:5.11.0")
    api("org.junit.jupiter:junit-jupiter-params:5.11.0")
    api("jakarta.validation:jakarta.validation-api:3.0.2")
    api("com.google.code.findbugs:jsr305:3.0.2")

    testImplementation("org.assertj:assertj-core:3.8.0")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.0")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.test {
    dependsOn("checkstyleMain", "checkstyleTest", "jar")
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}
