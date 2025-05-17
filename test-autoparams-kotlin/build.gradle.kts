import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation(project(":autoparams"))
    testImplementation(project(":autoparams-kotlin"))
    testImplementation("org.assertj:assertj-core:3.20.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-java-parameters"
    }
}

tasks.test {
    useJUnitPlatform()
}
