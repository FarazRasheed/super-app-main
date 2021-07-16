package co.yap.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.text.InputType
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.*
import android.view.Gravity.BOTTOM
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import co.yap.yapcore.R
import kotlinx.android.synthetic.main.custom_widget_edit_text.view.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("CustomViewStyleable")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CoreInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyle, defStyleRes) {


    private var drawableRight: Drawable? = null
    private var drawableLeft: Drawable? = null
    private var paintText: Paint = Paint()
    private var viewWeight: Int = 0
    private var viewHeight: Int = 0
    private var textInput: String = ""
    var countryCode: String = "+971 "
    lateinit var typedArray: TypedArray
    var inputType: Int = 0
    private var imeiActionType: Int = 1
    private var IME_NEXT: Int = 2
    var maxLength: Int = 0
    private var view_id: Int = 0
    private var PHONE_INPUT_TYPE: Int = 1
    private var EMAIL_INPUT_TYPE: Int = 2
    private var PHONE_NUMBER_LENGTH: Int = 16
    var editText: EditText
    var checkFocusChange: Boolean = false
    private var view_plain_background: Boolean = false
    private var viewDataBinding: ViewDataBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.custom_widget_edit_text,
        this,
        true
    )
    private var showKeyboard: Boolean = true

    init {
        viewDataBinding.executePendingBindings()
        editText = viewDataBinding.root.findViewById(R.id.etCoreInput)

        attrs?.let {
            typedArray = context.obtainStyledAttributes(it, R.styleable.CoreInputField, 0, 0)
            val title = resources.getText(
                typedArray
                    .getResourceId(
                        R.styleable.CoreInputField_view_hint_input_field,
                        R.string.empty_string
                    )
            )
            showKeyboard = typedArray.getBoolean(R.styleable.CoreInputField_showKeyboard, true)
            inputType = typedArray.getInt(R.styleable.CoreInputField_view_input_type, inputType)
            maxLength = typedArray.getInt(R.styleable.CoreInputField_view_max_length, maxLength)
            checkFocusChange =
                typedArray.getBoolean(R.styleable.CoreInputField_view_focusable, checkFocusChange)
            view_plain_background = typedArray.getBoolean(
                R.styleable.CoreInputField_view_plain_background,
                view_plain_background
            )
            imeiActionType = typedArray.getInt(
                R.styleable.CoreInputField_view_input_text_imei_actions,
                imeiActionType
            )
            if (view_plain_background) {

                editText.setBackgroundResource(R.drawable.bg_plain_edit_text)
                editText.setPadding(0, 0, 0, 15)
                rlTopMain.setPadding(0, -13, 0, 0)
//                android:gravity="bottom"
                editText.gravity = BOTTOM

                //set plain bg
            } else {
                editText.setBackgroundResource(R.drawable.bg_round_edit_text_general)
            }


            if (null != typedArray.getInt(R.styleable.CoreInputField_view_id, view_id)) {
                view_id = typedArray.getInt(R.styleable.CoreInputField_view_id, view_id)
                if (view_id > 0) {

                    editText.id = view_id
                }
            }


            val error = resources.getText(
                typedArray.getResourceId(
                    R.styleable.CoreInputField_view_error_input_field,
                    R.string.empty_string
                )
            )

            if (null != typedArray.getString(R.styleable.CoreInputField_view_input_text)) {
                textInput =
                    typedArray.getString(R.styleable.CoreInputField_view_input_text) ?: ""
            }

            if (null != typedArray.getDrawable(R.styleable.CoreInputField_view_drawable_right)) {
                drawableRight =
                    typedArray.getDrawable(R.styleable.CoreInputField_view_drawable_right)
                editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null)
            } else {
                drawableRight = null
            }

            if (null != typedArray.getDrawable(R.styleable.CoreInputField_view_drawable_left)) {
                drawableLeft = typedArray.getDrawable(R.styleable.CoreInputField_view_drawable_left)

                editText.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
            }

            if (null != drawableRight && null != drawableLeft) {
                editText.setCompoundDrawablesWithIntrinsicBounds(
                    drawableLeft,
                    null,
                    drawableRight,
                    null
                )
            }

            if (!textInput.isEmpty()) {
                editText.setText(textInput)
            }

            editText.hint = title
            setViewInputType()
            setImeiActionsType()
            if (error.isNotEmpty()) settingUIForError(error = error.toString()) else settingUIForNormal()
            typedArray.recycle()


            /* text paint styling */

            paintText.textAlign = Paint.Align.CENTER
            paintText.style = Paint.Style.FILL

            editText.isFocusable
            onKeyBoardDismissal(true)
            animteKeyboardDismissal()

            if (maxLength > 0) {
                editText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength)))

            }
        }
    }

    private fun setViewInputType() {
        when (inputType) {
            PHONE_INPUT_TYPE -> {
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                setPhoneNumberField()
                if (showKeyboard)
                    requestKeyboard()
            }

            EMAIL_INPUT_TYPE -> {
                editText.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                if (showKeyboard)
                    requestKeyboard()
            }
        }
    }

    private fun setImeiActionsType() {
        when (imeiActionType) {
            IME_NEXT -> {
                editText.imeOptions = EditorInfo.IME_ACTION_NEXT
                checkFocusChange = true

            }
            else -> {
                editText.imeOptions = EditorInfo.IME_ACTION_DONE
                onKeyBoardDismissal(true)

            }
        }
    }

    private fun setPhoneNumberField() {
        editText.setFilters(arrayOf<InputFilter>(InputFilter.LengthFilter(PHONE_NUMBER_LENGTH)))

        val builder = SpannableStringBuilder("")
        builder.color(color = R.color.greySoft) {
            append(countryCode)
        }

        editText.setText(builder)
        editText.setSelection(editText.text.length)
        disableTextSelection()

        editText.setCursorVisible(false)

        editText.setOnClickListener(OnClickListener { editText.setSelection(editText.getText().toString().length) })

        editText.setCursorVisible(true)
    }

    fun cursorPlacement() {
        editText.setOnClickListener(OnClickListener { editText.setSelection(editText.getText().toString().length) })
        editText.setCursorVisible(true)
    }

    private fun disableTextSelection() {
        editText.isLongClickable = false
        editText.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
            }
        })
    }

    fun setDrawableRightIcon(drawable: Drawable?) {

        drawableRight = drawable

        if (null != drawableLeft) {
            editText.setCompoundDrawablesWithIntrinsicBounds(
                drawableLeft,
                null,
                drawableRight,
                null
            )

        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null)
        }

    }

    fun setDrawableLeftIcon(drawable: Drawable) {
        drawableLeft = drawable

        if (null != drawableRight) {
            editText.setCompoundDrawablesWithIntrinsicBounds(
                drawableLeft,
                null,
                drawableRight,
                null
            )

        } else {
            editText.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
        }
    }

    fun setInputText(text: String) {
        editText.setText(text)
    }

    fun getInputText(): String {
        return editText.text.toString()

    }

    fun settingUIForError(error: String) {
        if (view_plain_background) {
            editText.setBackgroundResource(R.drawable.bg_red_line)
        } else {
            editText.setBackgroundResource(R.drawable.bg_round_error_layout)
        }

        tvError.text = error
        tvError.visibility = View.VISIBLE
        setDrawableRightIcon(resources.getDrawable(R.drawable.invalid_name))
    }

    fun settingErrorColor(color: Int) {
        tvError.setTextColor(ContextCompat.getColor(context, color))
    }

    fun settingUIForNormal() {
        if (view_plain_background) {
            editText.setBackgroundResource(R.drawable.bg_plain_edit_text)
        } else {
            editText.setBackgroundResource(R.drawable.bg_round_edit_text_general)
        }
        tvError.text = ""
        drawableRight = null
        tvError.visibility = View.GONE
    }

    override fun onSizeChanged(w: Int, h: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(w, h, oldWidth, oldHeight)
        viewWeight = w
        viewHeight = h
    }


    /* handle keyboard dismissal event */
    fun onKeyBoardDismissal(check: Boolean) {

        if (checkFocusChange) {
            var listener = object : OnFocusChangeListener {
                override fun onFocusChange(v: View, hasFocus: Boolean) {
                    if (!hasFocus) {
                        editText.isActivated = false
                    } else {
                        editText.isActivated = true
                    }
                }
            }

            editText.setOnFocusChangeListener(listener)
        } else {

            editText.getViewTreeObserver().addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (editText.isFocused()) {
                            if (!keyboardShown(editText.getRootView())) {
                                editText.isActivated = false
                            } else {
                                editText.isActivated = true
                            }
                        }
                        return
                    }
                })
        }
    }

    /* animate down keyboard */
    fun animteKeyboardDismissal() {

        val view = this.editText
        view?.let { v ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
        }
    }

    fun keyboardShown(rootView: View): Boolean {

        val softKeyboardHeight = 100
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - r.bottom
        return heightDiff > softKeyboardHeight * dm.density
    }

    fun requestKeyboard() {
        editText.requestFocus()
        (editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}
