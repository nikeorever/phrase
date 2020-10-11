plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

dependencies {
    implementation(Dependencies.Squareup.kotlinpoet)
}

apply("$rootDir/gradle/configure-maven-publish.gradle")
