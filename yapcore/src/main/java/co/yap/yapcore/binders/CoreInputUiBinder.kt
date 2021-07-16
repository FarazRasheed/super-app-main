package co.yap.yapcore.binders

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import co.yap.translation.Translator
import co.yap.widgets.CoreInputField


object CoreInputUiBinder {

    @JvmStatic
    @BindingAdapter(value = ["realValueAttrChanged"])
    fun setListener(coreInputField: CoreInputField, listener: InverseBindingListener?) {

        if (listener != null) {
            coreInputField.editText.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    override fun afterTextChanged(editable: Editable) {
                        listener.onChange()
                    }
                })
        }
    }

    @JvmStatic
    @BindingAdapter("isActivated")
    fun setIsActivated(view: CoreInputField, value: Boolean) {
        if (!value) {
            view.editText.isActivated = value

        }
    }

    @JvmStatic
    @BindingAdapter("onEditorActionListener")
    fun setOnEditorActionListener(
        view: CoreInputField,
        check: Boolean/*, onEditorActionListener: View.setOnEditorActionListener*/
    ) {

        view.editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                println("IME_ACTION_DONE clickec")
                Log.i("aactionzz", "IME_ACTION_DONE clickec")
                true
            } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Log.i("aactionzz", "IME_ACTION_NEXT clickec")

                true
            } else {
                println("else clickec")

                false
            }
        }
    }

    @JvmStatic
    @BindingAdapter("onEditorActionDoneListener")
    fun setOnEditorActionDoneLitener(
        view: CoreInputField,
        listener: TextView.OnEditorActionListener
    ) {
        view.editText.setOnEditorActionListener(listener)
    }

    @JvmStatic
    @BindingAdapter("realValue")
    fun setRealValue(view: CoreInputField, value: String) {
        if (value.equals(view.editText.text)) {
            view.editText.setText(value)
        }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @InverseBindingAdapter(attribute = "realValue")
    fun getRealValue(editText: CoreInputField): String {

        return editText.getInputText()
    }

    //
    @JvmStatic
    @BindingAdapter("coreInputText")
    fun setCoreInputText(view: CoreInputField, value: String) {
        if (value.equals(view.editText.text)) {
            view.editText.setText(value)
        }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @InverseBindingAdapter(attribute = "coreInputText")
    fun getCoreInputText(editText: CoreInputField): String {

        return editText.getInputText()
    }


    @BindingAdapter(value = ["coreInputHint", "translateHint"], requireAll = false)
    @JvmStatic
    fun setHint(view: CoreInputField, hint: String, translate: Boolean) {
        if (translate) {
            view.editText.setHint(Translator.getString(view.context, hint))
        } else {

            view.editText.setHint(hint)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("coreInputDrawableLeft")
    fun drawableLeft(view: CoreInputField, drawable: Drawable) {
        view.setDrawableLeftIcon(drawable)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("coreInputDrawableRight")
    fun drawableRight(view: CoreInputField, drawable: Drawable?) {
        view.setDrawableRightIcon(drawable)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("coreInputError")
    fun setErrorMessage(view: CoreInputField, error: String) {
        if (null != error && !error.isEmpty()) {
            view.settingUIForError(error)
        } else {
            view.settingUIForNormal()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("resetUI")
    fun resetUI(view: CoreInputField, refresh: Boolean) {
        if (refresh) {
            view.settingUIForNormal()

        }
    }

    /* textwatcher */

    @JvmStatic
    @BindingAdapter("textWatcher")
    fun setTextChangeListener(view: CoreInputField, watcher: TextWatcher) {
        view.editText.addTextChangedListener(watcher)
    }

    @JvmStatic
    @BindingAdapter("changeCoreInputFocus")
    fun setFocusChangeListener(
        view: CoreInputField,
        focusChangeListener: View.OnFocusChangeListener
    ) {
        view.editText.setOnFocusChangeListener(focusChangeListener)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("cursorPlacement")
    fun cursorPlacement(view: CoreInputField, placeCursor: Boolean) {
        view.cursorPlacement()

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("inputText")
    fun setSpannableInputText(view: CoreInputField, text: SpannableStringBuilder) {
        if (!view.editText.text.contains(text.toString())) {

            view.editText.setText(text)
            view.editText.setSelection(view.editText.text.length)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("isCursorVisible")
    fun isCursorVisible(view: CoreInputField, isVisible: Boolean) {
        view.editText.setCursorVisible(isVisible)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("selection")
    fun selection(view: CoreInputField, selection: Int) {
        view.editText.setSelection(selection)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("disableKeyBack")
    fun disableKeyBack(view: CoreInputField, index: Int) {
        val lengthh: Int = view.editText.text.toString().length
        if (view.editText.text.toString().length == 5) {
            view.editText.setCursorVisible(false)
            /* disable backpress */
            view.cursorPlacement()
            view.editText.setOnKeyListener(object : View.OnKeyListener {

                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (lengthh <= 5) {
                            return true
                        }
                    }
                    return false
                }
            })
        } else {
            /*enable backpress*/

            view.cursorPlacement()
            view.editText.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (lengthh <= 5) {
                            return true
                        }
                    }
                    return false
                }
            })
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    @BindingAdapter("deactivateEditText")
    fun deactivateEditText(view: CoreInputField, activate: Boolean) {
        if (activate) {
            view.requestKeyboard()
        }
        view.editText.isActivated = activate
        view.editText.isFocusable = activate
    }


    @JvmStatic
    @BindingAdapter("capitalise")
    fun capitaliseEditText(editText: EditText, activate: Boolean) {
        if (activate) {
            editText.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        }
    }

}
