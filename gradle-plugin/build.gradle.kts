plugins {
    kotlin("jvm")
    `kotlin-dsl`
    id("org.jetbrains.dokka")
}

dependencies {
    compileOnly(Dependencies.Android.gradlePlugin)
    compileOnly(gradleApi())
    compileOnly(kotlin("gradle-plugin"))
    implementation(project(":classes-writer"))
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/gen")
        }
    }
}

val generateVersionTask = tasks.register("generateVersion") {
    val outputDir = file("$buildDir/gen")

    inputs.property("runtimeVersion", rootProject.project(":runtime").version)
    outputs.dir(outputDir)

    doLast {
        val versionFile = file("$outputDir/cn/nikeo/phrase/gradle/runtimeVersion.kt")
        versionFile.parentFile.mkdirs()
        versionFile.writeText(
            """
            // Generated file. Do not edit!
            package cn.nikeo.phrase.gradle
            
            // Version of cn.nikeo.phrase:runtime:_
            internal const val runtimeVersion = "${inputs.properties["runtimeVersion"]}"
        """.trimIndent()
        )
    }
}

tasks.getByName("compileKotlin").dependsOn(generateVersionTask)


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