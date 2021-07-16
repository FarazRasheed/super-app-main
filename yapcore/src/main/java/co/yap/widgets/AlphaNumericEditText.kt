package co.yap.widgets

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText

class AlphaNumericEditText : AppCompatEditText {

    private var backupString = ""

    constructor(context: Context) : super(context) {
        addTextChangedListener((textWatcher))
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        addTextChangedListener((textWatcher))
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        addTextChangedListener((textWatcher))
//        initTextWatchers()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        updateValue(text.toString())
    }

    private fun updateValue(text: String) {
        setText(text)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(
            charSequence: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            this@AlphaNumericEditText.removeTextChangedListener(this)
            var orignalString = charSequence.toString()
            if (orignalString.isNotEmpty() && Character.isWhitespace(charSequence[0])) {
                if (text?.length ?: 0 > 1) {
                    setText(backupString)
                } else
                    setText("")
            } else {
//                if (orignalString.contains("  ")) {
//                    val selection = orignalString.indexOf("  ")
//                    orignalString = orignalString.replace("  ", " ")
//                    setText(orignalString)
//                    setSelection(if (selection > -1) selection else orignalString.length)
//                }
                backupString = orignalString
            }

            this@AlphaNumericEditText.addTextChangedListener(this)
        }
    }

    private fun initTextWatchers() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                this@AlphaNumericEditText.removeTextChangedListener(this)
                val orignalString = charSequence.toString()
                if (orignalString.isNotEmpty() && Character.isWhitespace(charSequence[0])) {
                    if (text?.length ?: 0 > 1) {
                        setText(backupString)
                    } else
                        setText("")
                } else {

                    backupString = orignalString
                }

                this@AlphaNumericEditText.addTextChangedListener(this)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        removeEndingSpaces()
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            removeEndingSpaces()
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun onEditorAction(actionCode: Int) {
        if (actionCode == EditorInfo.IME_ACTION_NEXT
            || actionCode == EditorInfo.IME_ACTION_DONE
        ) {
            removeEndingSpaces()
        }
        super.onEditorAction(actionCode)
    }

    private fun removeEndingSpaces() {
        removeTextChangedListener(textWatcher)
        setText(text.toString().trim())
        setSelection(text?.length ?: 0)
        addTextChangedListener(textWatcher)
    }
}
