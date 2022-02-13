plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version Versions.KOTLIN
}

group = "net.perfectdreams.dokyoweb"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    js(IR) { // Use new, but experimental, compiler
        browser {
            binaries.executable()
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLIN_SERIALIZATION}")
                api("io.github.microutils:kotlin-logging:2.0.4")
            }
        }

        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                api("io.github.microutils:kotlin-logging-jvm:2.1.21")
            }
        }

        js().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(npm("buffer", "5.6.1"))
                api("io.github.microutils:kotlin-logging-js:2.1.21")
            }
        }

        val jvmTest by getting {
            dependencies {
                // Required for tests, if this is missing then Gradle will throw
                // "No tests found for given includes: [***Test](filter.includeTestsMatching)"
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.junit.jupiter:junit-jupiter:5.4.2")
            }
        }
    }
}