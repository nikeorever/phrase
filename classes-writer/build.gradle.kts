plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
}

dependencies {
    implementation(Dependencies.Squareup.kotlinpoet)
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