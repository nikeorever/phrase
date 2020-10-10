package cn.nikeo.phrase.gradle

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.android.builder.model.BuildType
import com.android.ide.common.symbols.getPackageNameFromManifest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

@Suppress("UnstableApiUsage")
class PhrasePlugin : Plugin<Project> {

    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project

        val isApp = project.plugins.hasPlugin(AppPlugin::class.java)
        val isLibrary = project.plugins.hasPlugin(LibraryPlugin::class.java)

        require(isApp || isLibrary) {
            "Phrase plugin[cn.nikeo.phrase] must applied after AppPlugin[com.android.application] or LibraryPlugin[com.android.library]"
        }

        val android = project.extensions.getByType(BaseExtension::class.java)
        android.buildTypes.onEach(::addDependencies).whenObjectAdded(::addDependencies)

        val variants = when {
            isApp -> project.extensions.getByType<AppExtension>().applicationVariants
            isLibrary -> project.extensions.getByType<LibraryExtension>().libraryVariants
            else -> error("PhrasePlugin[cn.nikeo.phrase] just support app or library")
        }

        variants.onEach(::configureVariant).whenObjectAdded(::configureVariant)
    }

    private fun configureVariant(variant: BaseVariant) {
        val variantSourceSetRes = VariantSourceSetRes(
            variantName = variant.name,
            sourceSetResCollection = variant.sourceSets.map { sourceSet ->
                SourceSetRes(
                    sourceSetName = sourceSet.name,
                    resDirectories = sourceSet.resDirectories
                )
            }
        ).also(::println)

        val outputDir = File(
            project.buildDir, "generated/source/phrase/${variantSourceSetRes.variantName}"
        )

        val generatePhraseTask =
            project.tasks.create<GeneratePhraseClassesTask>("generate${variantSourceSetRes.variantName.capitalize()}PhraseClasses") {
                this.variantSourceSetRes.set(variantSourceSetRes)
                this.packageName.set(project.packageName())
                this.outputDir.set(outputDir)
            }

        variant.registerJavaGeneratingTask(generatePhraseTask, outputDir)

        val kotlinCompileTask =
            project.tasks.findByName("compile${variantSourceSetRes.variantName.capitalize()}Kotlin") as KotlinCompile
        kotlinCompileTask.dependsOn(generatePhraseTask)
        val srcSet = project.objects.sourceDirectorySet(
            "mainPhrase${variantSourceSetRes.variantName}",
            "mainPhrase${variantSourceSetRes.variantName}"
        ).srcDir(outputDir)
        kotlinCompileTask.source(srcSet)
    }

    private fun addDependencies(buildType: BuildType) {
        project.rootProject.allprojects {
            repositories {
                mavenCentral()
            }
        }

        val configuration = if (project.configurations.findByName("api") != null)
            "${buildType.name}Api"
        else
            "${buildType.name}Implementation"

        project.dependencies.add(
            configuration,
            "cn.nikeo.phrase:runtime:$runtimeVersion"
        )
    }

    private fun Project.packageName(): String {
        val androidExtension = extensions.getByType(BaseExtension::class.java)
        androidExtension.sourceSets
            .map { it.manifest.srcFile }
            .filter { it.exists() }
            .forEach {
                return getPackageNameFromManifest(it)
            }
        throw IllegalStateException("No source sets available")
    }
}