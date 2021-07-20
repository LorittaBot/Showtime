pluginManagement {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "Showtime"

include(":web-common")
include(":backend")
include(":frontend")