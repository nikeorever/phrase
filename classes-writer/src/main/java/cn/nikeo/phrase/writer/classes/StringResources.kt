package cn.nikeo.phrase.writer.classes

import java.util.regex.Matcher
import java.util.regex.Pattern

class StringResources(private val stringResources: Map<String, String>) {

    fun eachToken(action: (String, List<String>) -> Unit) {
        stringResources.entries.forEach { entry ->
            val stringName = entry.key
            val stringValue = entry.value
            action(stringName, tokensFrom(stringValue))
        }
    }

    companion object {

        private val PATTERN: Pattern = Pattern.compile("\\{([a-z_]+)}")

        fun isPhrase(string: String): Boolean = PATTERN.matcher(string).find()

        private fun tokensFrom(string: String): List<String> {
            val tokens: MutableList<String> = ArrayList()
            val matcher: Matcher = PATTERN.matcher(string)
            while (matcher.find()) {
                tokens.add(matcher.group(1))
            }
            tokens.sort()
            return tokens
        }
    }
}