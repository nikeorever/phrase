package cn.nikeo.phrase.gradle

import cn.nikeo.phrase.writer.classes.PhraseClassesWriter
import cn.nikeo.phrase.writer.classes.StringResources
import com.android.SdkConstants
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

abstract class GeneratePhraseClassesTask : DefaultTask() {

    @get:Input
    abstract val packageName: Property<String>

    abstract val variantSourceSetRes: Property<VariantSourceSetRes>

    @get:Input
    val stringResources: List<Pair<String, String>>
        get() {
            return variantSourceSetRes.get().sourceSetResCollection.map(SourceSetRes::resDirectories)
                .reduceOrNull { acc, collection -> acc + collection }
                ?.mapNotNull { resDirectory ->
                    resDirectory
                        .walkTopDown()
                        .asSequence()
                        .filter { it.isFile && it.path.endsWith(SdkConstants.DOT_XML) }
                        .map(::readXml)
                        .filter { it.documentElement.nodeName == "resources" }
                        .mapNotNull { document ->
                            document.documentElement.getElementsByTagName("string").takeIf {
                                it != null && it.length > 0
                            }
                        }
                        .map { nodeList ->
                            (0 until nodeList.length).map { index ->
                                nodeList.item(index).takeIf { node ->
                                    StringResources.isPhrase(node.textContent)
                                }?.let { node ->
                                    node.attributes.getNamedItem("name").nodeValue to node.textContent
                                }
                            }
                        }
                        .map { it.filterNotNull() }
                        .reduceOrNull { acc, list -> acc + list }
                }
                ?.reduceOrNull { acc, list -> acc + list }
                .orEmpty()
        }

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun execute() {
        PhraseClassesWriter(packageName = packageName.get(), stringResources = StringResources(stringResources.toMap()))
            .writeTo(outputDir.get().asFile)
    }

    private fun readXml(xmlFile: File): Document {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))
        return dBuilder.parse(xmlInput)
    }

}