plugins {
    kotlin("jvm")
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

    inputs.property("runtimeVersion", project.property("VERSION_NAME"))
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

apply("$rootDir/gradle/configure-maven-publish.gradle")