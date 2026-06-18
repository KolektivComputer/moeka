dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.yuri.capital/repository/maven-public/")
        }
//        maven {
//            url = uri("https://repo.yuri.capital/repository/maven-snapshots/")
//        }
    }
}

plugins {
    // Use the Foojay Toolchains plugin to automatically download JDKs required by subprojects.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(":core")

include(":platforms:discord")

rootProject.name = "moeka"
