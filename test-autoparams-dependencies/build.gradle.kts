import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
}

dependencies {
    api("io.github.autoparams:autoparams:[11.1.0, 12.0.0)")
    api("io.github.autoparams:autoparams-kotlin:[11.1.0, 12.0.0)")
    api("io.github.autoparams:autoparams-lombok:[11.1.0, 12.0.0)")
    api("io.github.autoparams:autoparams-mockito:[11.1.0, 12.0.0)")
    testImplementation("org.assertj:assertj-core:3.8.0")
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
