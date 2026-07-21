plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.mavenPublish)
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(project(":core"))

    implementation(libs.bundles.database)

    implementation(libs.kord.core)
    implementation(libs.korelibs.validate.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.slf4jApi)

    testImplementation(kotlin("test"))
}

publishing {
    repositories {
        maven {
            name = "yuriCapital"

            url =
                uri(
                    if (version.toString().endsWith("SNAPSHOT")) {
                        "https://repo.yuri.capital/repository/maven-snapshots/"
                    } else {
                        "https://repo.yuri.capital/repository/maven-releases/"
                    },
                )

            credentials(PasswordCredentials::class)
        }
    }

    mavenPublishing {
        coordinates(group.toString(), "moeka-discord", version.toString())

        pom {
            name.set("Pitohui Core")
            description.set("Core functionality for Moeka, a multi-platform bot framework written in Kotlin")
            inceptionYear.set("2025")
            url.set("https://git.yuri.capital/crackcafe/moeka/")
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
                    url.set("https://git.yuri.capital/mey/")
                }
            }
            scm {
                url.set("https://git.yuri.capital/crackcafe/moeka/")
                connection.set("scm:git:git://git.yuri.capital/crackcafe/moeka.git")
                developerConnection.set("scm:git:ssh://git@git.yuri.capital/crackcafe/moeka.git")
            }
        }
    }
}
