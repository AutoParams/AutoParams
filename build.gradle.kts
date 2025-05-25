plugins {
    java
    `maven-publish`
    signing
    checkstyle
}

allprojects {
    repositories {
        mavenCentral()
    }

    group = "io.github.autoparams"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    version = extra["artifactVersion"] as String

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configure<CheckstyleExtension> {
        configFile = file("${rootProject.projectDir}/config/checkstyle/checkstyle.xml")
        configProperties["suppressionFile"] = "${rootProject.projectDir}/config/checkstyle/checkstyle-suppressions.xml"
        toolVersion = "9.3"
        isIgnoreFailures = false
        maxErrors = 0
        maxWarnings = 0
    }

    tasks.withType<Jar>().configureEach {
        manifest {
            attributes(
                mapOf(
                    "Specification-Title" to findProperty("artifactName"),
                    "Specification-Version" to findProperty("artifactVersion"),
                    "Specification-Vendor" to "io.github.autoparams",
                    "Implementation-Title" to findProperty("artifactName"),
                    "Implementation-Version" to findProperty("artifactVersion"),
                    "Implementation-Vendor" to "io.github.autoparams"
                )
            )
        }
    }

    publishing {
        repositories {
            maven {
                url = layout.buildDirectory.dir("publish").get().asFile.toURI()
            }
        }

        publications {
            create<MavenPublication>("maven") {
                artifactId = extra["artifactId"] as String
                from(components["java"])
                pom {
                    name.set(extra["artifactName"] as String)
                    description.set(extra["artifactDescription"] as String)
                    url.set("https://github.com/AutoParams/AutoParams")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/AutoParams/AutoParams/blob/main/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("gyuwon")
                            name.set("Gyuwon Yi")
                            email.set("gyuwon@live.com")
                        }
                        developer {
                            id.set("mhyeon.lee")
                            name.set("Myeonghyeon Lee")
                            email.set("mhyeon.lee@navercorp.com")
                        }
                        developer {
                            id.set("jwchung")
                            name.set("Jin-Wook Chung")
                            email.set("jwchung@hotmail.com")
                        }
                    }
                    scm {
                        connection.set("https://github.com/AutoParams/AutoParams.git")
                        developerConnection.set("https://github.com/AutoParams/AutoParams.git")
                        url.set("https://github.com/AutoParams/AutoParams")
                    }
                }
            }
        }
    }

    signing {
        val signingKey = System.getenv("SIGNING_KEY")
        val signingPassword = System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["maven"])
    }
}
