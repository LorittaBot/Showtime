import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.include

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.google.cloud.tools.jib") version Versions.JIB
}

group = "net.perfectdreams.showtime"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":web-common"))

    // Logging Stuff
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha14")
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // Ktor
    implementation("io.ktor:ktor-server-netty:${Versions.KTOR}")

    // KotlinX HTML
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")

    // KotlinX Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:${Versions.KOTLIN_SERIALIZATION}")

    implementation("org.jsoup:jsoup:1.14.3")

    // YAML
    implementation("org.yaml:snakeyaml:1.27")

    // Sequins
    api("net.perfectdreams.sequins.ktor:base-route:1.0.2")

    api("commons-codec:commons-codec:1.15")

    api("com.vladsch.flexmark:flexmark-all:0.64.0")
}

jib {
    container {
        ports = listOf("8080")
    }

    to {
        image = "ghcr.io/lorittabot/showtime-backend"

        auth {
            username = System.getProperty("DOCKER_USERNAME") ?: System.getenv("DOCKER_USERNAME")
            password = System.getProperty("DOCKER_PASSWORD") ?: System.getenv("DOCKER_PASSWORD")
        }
    }

    from {
        image = "openjdk:15.0.2-slim-buster"
    }
}

val jsBrowserProductionWebpack = tasks.getByPath(":frontend:jsBrowserProductionWebpack") as org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

tasks {
    val runnableJar = runnableJarTask(
        DEFAULT_SHADED_WITHIN_JAR_LIBRARIES,
        configurations.runtimeClasspath.get(),
        jar.get(),
        "net.perfectdreams.showtime.backend.ShowtimeBackendLauncher",
        mapOf()
    )

    val sass = sassTask("style.scss")

    processResources {
        // We need to wait until the JS build finishes and the SASS files are generated
        dependsOn(jsBrowserProductionWebpack)
        dependsOn(sass)

        // Copy the output from the frontend task to the backend resources
        from(jsBrowserProductionWebpack.destinationDirectory) {
            into("static/v3/assets/js/")
        }

        // Same thing with the SASS output
        from(File(buildDir, "sass")) {
            into("static/v3/assets/css/")
        }
    }

    "build" {
        // This should be ran BEFORE the JAR is compiled!
        dependsOn(runnableJar)
    }
}