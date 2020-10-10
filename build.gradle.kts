@file:Suppress("UnstableApiUsage")

buildscript {
    dependencies {
        classpath(Dependencies.Android.gradlePlugin)
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath(Dependencies.mavenPublish)
        classpath(Dependencies.dokka)
    }

    repositories {
        mavenCentral()
        jcenter()
        google()
    }
}

subprojects {

    repositories {
        mavenCentral()
        jcenter()
        google()
    }

    buildDir = File(rootProject.buildDir, name)

    plugins.withId("org.jetbrains.kotlin.android") {
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf(
                    "-progressive", "-Xjvm-default=enable",
                    "-Xopt-in=kotlin.ExperimentalStdlibApi"
                )
            }
        }

        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    plugins.withId("org.jetbrains.kotlin.jvm") {
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf(
                    "-progressive", "-Xjvm-default=enable",
                    "-Xopt-in=kotlin.ExperimentalStdlibApi"
                )
            }
        }

        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    afterEvaluate {
        // Can't use the normal placeholder syntax to reference the kotlin* version, since that
        // placeholder seems to only be evaluated if the module has a direct dependency on the library.
        val versionProperties = java.util.Properties()
        versionProperties.load(File(rootDir, "versions.properties").reader())
        val kotlinVersion = versionProperties.getProperty("version.kotlin")

        configurations.configureEach {
            // There could be transitive dependencies in tests with a lower version. This could cause
            // problems with a newer Kotlin version that we use.
            resolutionStrategy.force(Dependencies.Kotlin.reflect(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.common(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk8(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk7(kotlinVersion))
            resolutionStrategy.force(Dependencies.Kotlin.Stdlib.jdk6(kotlinVersion))
        }
    }
}