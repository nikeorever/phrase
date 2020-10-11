plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
}

apply("$rootDir/gradle/configure-android-defaults.gradle")
apply("$rootDir/gradle/configure-maven-publish.gradle")

dependencies {
    implementation(Dependencies.Squareup.phrase)
    implementation(Dependencies.AndroidX.annotation)
}
