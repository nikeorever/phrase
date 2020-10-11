@file:Suppress("UnstableApiUsage")

import Dependencies.ktlint

buildscript {
    dependencies {
        classpath(Dependencies.Android.gradlePlugin)
        classpath(Dependencies.Kotlin.gradlePlugin)
        classpath(Dependencies.mavenPublish)
        classpath(Dependencies.dokka)
        classpath(Dependencies.ktlint)
    }

    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
    }
}

subprojects {

    repositories {
        mavenCentral()
        jcenter()
        google()
        gradlePluginPortal()
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    // Configuration documentation: https://github.com/JLLeitschuh/ktlint-gradle#configuration
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        // Prints the name of failed rules
        verbose.set(true)
        // Default "plain" reporter is actually harder to read.
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
        }
        disabledRules.set(
            setOf(
                // IntelliJ refuses to sort imports correctly.
                // This is a known issue: https://github.com/pinterest/ktlint/issues/527
                "import-ordering"
            )
        )
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