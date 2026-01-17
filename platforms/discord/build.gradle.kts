plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    // Apply Kotlin Serialization plugin from `gradle/libs.versions.toml`.
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.ktlint)
}

version = "0.0.1"

dependencies {
    api(project(":core"))

    implementation(libs.slf4jApi)
    implementation(libs.kord.core)
    implementation(libs.bundles.exposed)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(kotlin("test"))
}

publishing {
    repositories {
        maven {
            name = "lizAinslie"

            url =
                uri(
                    if (version.toString().endsWith("SNAPSHOT")) {
                        "https://repo.lizainslie.dev/repository/maven-snapshots/"
                    } else {
                        "https://repo.lizainslie.dev/repository/maven-releases/"
                    },
                )

            credentials(PasswordCredentials::class)
        }
    }

    mavenPublishing {
        coordinates("dev.lizainslie.moeka", "moeka-discord", version.toString())

        pom {
            name.set("Pitohui Core")
            description.set("Core functionality for Moeka, a multi-platform bot framework written in Kotlin")
            inceptionYear.set("2025")
            url.set("https://git.lizainslie.dev/crack-cafe/moeka/")
            licenses {
                license {
                    name.set("The MIT License (MIT)")
                    url.set("https://opensource.org/license/mit")
                    distribution.set("https://opensource.org/license/mit")
                }
            }
            developers {
                developer {
                    id.set("mey")
                    name.set("Mey Ainslie")
                    url.set("https://git.lizainslie.dev/mey/")
                }
            }
            scm {
                url.set("https://git.lizainslie.dev/crack-cafe/moeka/")
                connection.set("scm:git:git://git.lizainslie.dev/crack-cafe/moeka.git")
                developerConnection.set("scm:git:ssh://git@git.lizainslie.dev/crack-cafe/moeka.git")
            }
        }
    }
}
