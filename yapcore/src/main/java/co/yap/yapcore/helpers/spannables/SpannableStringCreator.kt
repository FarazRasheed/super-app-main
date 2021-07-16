package co.yap.yapcore.helpers.spannables

import android.content.Context
import android.content.res.Resources
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextUtils.concat
import androidx.annotation.StringRes
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SpannableStringCreator {
    private val parts = ArrayList<CharSequence>()
    private var length = 0
    private val spanMap: MutableMap<IntRange, Iterable<Any>> = HashMap()

    fun appendSpace(newText: CharSequence) = append(" ").append(newText)

    fun appendSpace(newText: CharSequence, spans: Iterable<Any>) =
        append(" ").append(newText, spans)

    fun appendLnNotBlank(newText: CharSequence, spans: Iterable<Any>) =
        applyIf({ !newText.isBlank() }) { appendLn(newText, spans) }

    fun appendLn(newText: CharSequence, spans: Iterable<Any>) = append("\n").append(newText, spans)

    fun append(newText: CharSequence, spans: Iterable<Any>) = apply {
        val end = newText.length
        parts.add(newText)
        spanMap[(length..length + end)] = spans
        length += end
    }

    fun append(newText: CharSequence) = apply {
        parts.add(newText)
        length += newText.length
    }

    inline fun applyIf(
        predicate: () -> Boolean,
        action: SpannableStringCreator.() -> SpannableStringCreator
    ) = if (predicate()) action() else this

    fun toSpannableString() = SpannableString(concat(*parts.toTypedArray())).apply {
        spanMap.forEach {
            val range = it.key
            it.value.forEach {
                setSpan(it, range.start, range.endInclusive, SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}

fun Resources.getSpannable(@StringRes id: Int, vararg spanParts: Pair<Any, Iterable<Any>>): CharSequence {
    val resultCreator = SpannableStringCreator()
    Formatter(
        SpannableAppendable(resultCreator, *spanParts),
        configuration.locale
    )
        .format(getString(id), *spanParts.map { it.first }.toTypedArray())
    return resultCreator.toSpannableString()
}

fun Resources.getText(@StringRes id: Int, vararg formatArgs: Any?) =
    getSpannable(id, *formatArgs.filterNotNull().map { it to emptyList<Any>() }.toTypedArray())

fun Resources.getSpannable(id: String, vararg spanParts: Pair<Any, Iterable<Any>>): CharSequence {
    val resultCreator = SpannableStringCreator()
    Formatter(
        SpannableAppendable(resultCreator, *spanParts),
        configuration.locale
    )
        .format(id, *spanParts.map { it.first }.toTypedArray())
    return resultCreator.toSpannableString()
}

fun Resources.getText( id: String, vararg formatArgs: Any?) =
    getSpannable(id, *formatArgs.filterNotNull().map { it to emptyList<Any>() }.toTypedArray())

//inline fun Context.resSpans(options: ResSpans.() -> Unit) =
//    ResSpans(this).apply(options)