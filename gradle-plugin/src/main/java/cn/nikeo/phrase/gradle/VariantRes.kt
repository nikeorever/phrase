package cn.nikeo.phrase.gradle

import java.io.File

data class VariantSourceSetRes(val variantName: String, val sourceSetResCollection: Collection<SourceSetRes>)

data class SourceSetRes(
    val sourceSetName: String,
    val resDirectories: Collection<File>
)