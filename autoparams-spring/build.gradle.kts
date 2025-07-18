plugins {
    id("java-library")
}

dependencies {
    api("io.github.autoparams:autoparams:[11.2.2, 12.0.0)")
    api("org.springframework:spring-context:[6.1.14,)")
    api("org.springframework:spring-test:[6.1.14,)")
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    dependsOn("checkstyleMain", "checkstyleTest")
}
