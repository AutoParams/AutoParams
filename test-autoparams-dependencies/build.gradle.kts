import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
}

dependencies {
    api("io.github.autoparams:autoparams:[10.1.1, 11.0.0)")
    api("io.github.autoparams:autoparams-kotlin:[10.1.1, 11.0.0)")
    api("io.github.autoparams:autoparams-lombok:[10.1.1, 11.0.0)")
    api("io.github.autoparams:autoparams-mockito:[10.1.1, 11.0.0)")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.test {
    useJUnitPlatform()
}
