import org.gradle.api.tasks.javadoc.Javadoc

plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

dependencies {
    testImplementation(project(":autoparams"))
    testImplementation(project(":autoparams-spring"))
    testImplementation(project(":autoparams-lombok"))
    testImplementation(project(":autoparams-mockito"))
    testImplementation(project(":autoparams-kotlin"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Jar>().configureEach {
    enabled = false
}

tasks.named("publish") {
    enabled = false
}

tasks.named("publishToMavenLocal") {
    enabled = false
}

tasks.named<Javadoc>("javadoc") {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}
