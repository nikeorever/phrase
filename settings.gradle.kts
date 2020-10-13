import de.fayard.refreshVersions.bootstrapRefreshVersions

rootProject.name = "phrase"

include(":gradle-plugin")
include(":classes-writer")
//include(":runtime")


buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.5")
}

bootstrapRefreshVersions()