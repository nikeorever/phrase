@file:JvmName("Deps")

object Versions {
    const val kotlin = "1.4.10"
    const val targetSdk = 30
    const val squareup_phrase = "1.1.0"
}

object Dependencies {
    object Kotlin {
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

        object Stdlib {
            const val common = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
            const val jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
            const val jdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
            const val jdk6 = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        }
    }
    const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:1.4.10"

    object Android {
        const val gradlePlugin = "com.android.tools.build:gradle:4.0.1"
    }

    object AndroidX {
        const val annotation = "androidx.annotation:annotation:1.1.0"
    }

    object Squareup {
        const val phrase = "com.squareup.phrase:phrase:${Versions.squareup_phrase}"
        const val kotlinpoet = "com.squareup:kotlinpoet:1.6.0"
    }

}