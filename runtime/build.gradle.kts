plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
}

apply("$rootDir/gradle/configure-android-defaults.gradle")

dependencies {
    implementation(Dependencies.Squareup.phrase)
    implementation(Dependencies.AndroidX.annotation)
}

val dokkaDir = "${buildDir}/dokka"

tasks.withType<org.jetbrains.dokka.gradle.DokkaTask> {
    outputDirectory.set(file(dokkaDir))
}

task<Jar>("dokkaJar") {
    archiveClassifier.set("javadoc")
    from(dokkaDir)
    dependsOn("dokkaHtml")
}

apply(from = "$rootDir/gradle/gradle-mvn-push.gradle.kts")