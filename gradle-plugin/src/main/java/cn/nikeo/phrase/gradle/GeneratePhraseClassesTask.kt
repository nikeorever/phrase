package cn.nikeo.phrase.gradle

import cn.nikeo.phrase.writer.classes.PhraseClassesWriter
import cn.nikeo.phrase.writer.classes.StringResource
import com.android.SdkConstants
import com.jakewharton.confundus.unsafeCast
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class GeneratePhraseClassesTask : DefaultTask() {

    @get:Input
    abstract val packageName: Property<String>

    abstract val variantSourceSetRes: Property<VariantSourceSetRes>

    @get:Input
    val stringResources: List<StringResource>?
        get() {
            return variantSourceSetRes.get().sourceSetResCollection.map(SourceSetRes::resDirectories)
                .reduceOrNull { acc, collection -> acc + collection }
                ?.mapNotNull { resDirectory ->
                    resDirectory
                        .walkTopDown()
                        .asSequence()
                        .filter {
                            it.isFile && it.endsWith("res/values/" + it.nameWithoutExtension + SdkConstants.DOT_XML)
                        }
                        .mapNotNull { parseStringResourcesXml(it) }
                        .reduceOrNull { acc, list -> acc + list }
                }
                ?.reduceOrNull { acc, list -> acc + list }
        }

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun execute() {
        val outputFile = outputDir.get().asFile
        if (outputFile.exists()) {
            FileUtils.deleteDirectory(outputFile)
        }

        if (!stringResources.isNullOrEmpty()) {
            PhraseClassesWriter(packageName = packageName.get(), stringResources = stringResources.unsafeCast())
                .writeTo(outputFile)
        }
    }
}
