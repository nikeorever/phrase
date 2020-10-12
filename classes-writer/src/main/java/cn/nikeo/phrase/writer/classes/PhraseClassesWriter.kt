package cn.nikeo.phrase.writer.classes

import com.jakewharton.confundus.unsafeCast
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

class PhraseClassesWriter(private val packageName: String, private val stringResources: List<StringResource>) {

    fun writeTo(out: File) {
        FileSpec.builder(packageName, "Phrase")
            .addComment("Automatically generated file. DO NOT MODIFY")
            .addImport(CLASS_NAME_ANDROID_CONTEXT.packageName, CLASS_NAME_ANDROID_CONTEXT.simpleName)
            .addImport(CLASS_NAME_BASE_PHRASE.packageName, CLASS_NAME_BASE_PHRASE.simpleName)
            .generateStringResourceFuncAndClass()
            .build()
            .writeTo(out)
    }

    private fun FileSpec.Builder.generateStringResourceFuncAndClass(): FileSpec.Builder {
        return this.also { builder ->
            stringResources.forEach { stringResource ->
                val stringName = stringResource.name.unsafeCast<String>()

                val phraseClassName = ClassName(packageName, "Phrase_$stringName")

                builder.addType(
                    TypeSpec.classBuilder(phraseClassName)
                        .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("\"%L\"", "ClassName").build())
                        .superclass(CLASS_NAME_BASE_PHRASE)
                        .addSuperclassConstructorParameter(
                            "context = %L, stringResId = %L",
                            "context",
                            "R.string.$stringName"
                        )
                        .primaryConstructor(
                            FunSpec.constructorBuilder()
                                .addParameter("context", CLASS_NAME_ANDROID_CONTEXT)
                                .build()
                        )
                        .addFunctions(
                            stringResource.tokens().map { token ->
                                FunSpec.builder(token)
                                    .addParameter("value", CharSequence::class)
                                    .returns(phraseClassName)
                                    .addStatement("put(\"%L\", value)", token)
                                    .addStatement("return this")
                                    .build()
                            }
                        )
                        .build()
                )

                builder.addFunction(
                    FunSpec.builder(stringName)
                        .addKdoc(
                            """
                            ${stringResource.comment ?: "No comments found"}
                            
                            The string resource corresponding to [$stringName] is in ${stringResource.sourceXml}
                            """.trimIndent()
                        )
                        .addParameter("context", CLASS_NAME_ANDROID_CONTEXT)
                        .returns(phraseClassName)
                        .addStatement("return %T(%L)", phraseClassName, "context")
                        .build()
                )
            }
        }
    }

    companion object {
        val CLASS_NAME_ANDROID_CONTEXT = ClassName("android.content", "Context")
        val CLASS_NAME_BASE_PHRASE = ClassName("cn.nikeo.phrase.runtime", "BasePhrase")
    }
}
