package cn.nikeo.phrase.runtime

import android.content.Context
import android.widget.TextView
import androidx.annotation.StringRes
import com.squareup.phrase.Phrase

open class BasePhrase {

    constructor(pattern: CharSequence) {
        phrase = Phrase.from(pattern)
    }

    constructor(context: Context, @StringRes stringResId: Int) : this(context.getText(stringResId))

    private val phrase: Phrase

    protected fun put(key: String, value: CharSequence) {
        phrase.put(key, value)
    }

    fun format(): CharSequence {
        return phrase.format()
    }
}

fun <T : BasePhrase> T.into(textView: TextView) {
    textView.text = format()
}
