package cn.nikeo.phrase.writer.classes

import com.jakewharton.confundus.unsafeCast
import java.io.File
import java.io.Serializable
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Represent a string source
 *
 * <!--This is comments of greeting-->
 * <string name="greeting">Hello, {other_name}! My name is {my_name}.</string>
 */
class StringResource : Serializable {
    var name: String? = null
    var textContent: String? = null
    var comment: String? = null
    var sourceXml: File? = null

    fun isPhrase(): Boolean {
        return !name.isNullOrEmpty() && !textContent.isNullOrEmpty() &&
            PATTERN.matcher(textContent.unsafeCast()).find()
    }

    fun tokens(): List<String> {
        val tokens: MutableList<String> = ArrayList()
        if (!textContent.isNullOrEmpty()) {
            val matcher: Matcher = PATTERN.matcher(textContent.unsafeCast())
            while (matcher.find()) {
                tokens.add(matcher.group(1))
            }
            tokens.sort()
        }
        return tokens
    }

    override fun toString(): String {
        return "StringResource(name=$name, textContent=$textContent, comment=$comment, sourceXml=$sourceXml)"
    }

    companion object {
        private val PATTERN: Pattern = Pattern.compile("\\{([a-z_]+)}")
    }
}
