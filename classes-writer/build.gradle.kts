plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.jakewharton.confundus")
}

dependencies {
    implementation(Dependencies.Squareup.kotlinpoet)
}

apply("$rootDir/gradle/configure-maven-publish.gradle")
