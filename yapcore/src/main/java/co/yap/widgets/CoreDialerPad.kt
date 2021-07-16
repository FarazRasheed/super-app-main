package co.yap.widgets

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.setPadding
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.SharedPreferenceManager
import kotlinx.android.synthetic.main.core_dialer_pad.view.*


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("Recycle")
class CoreDialerPad @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    var etPassCodeText: EditText? = null
    var buttonRemove: ImageView? = null
    var list: ArrayList<Int> = ArrayList()
    var dialerType = 0
    var showDialerPassCodeView: Boolean = true
        set(value) {
            field = value
            view.findViewById<ConstraintLayout>(R.id.clPassCodeViews).visibility =
                if (value) View.VISIBLE else View.GONE
        }
    var dialerMaxLength = 6
    val animShake = AnimationUtils.loadAnimation(context, R.anim.shake)

    //var onButtonClickListener: OnClickListener? = null
    private var inputEditText: EditText? = null
    private var listener: NumberKeyboardListener? = null
    private var view: View =
        inflate(context, R.layout.core_dialer_pad, null)
    private val keypads: ArrayList<View> = ArrayList()

    private val onClickListener: OnClickListener = OnClickListener {
        if (it.id == R.id.button1) {
            etPassCodeText?.append("1")
            inputEditText?.append("1")
            listener?.onNumberClicked(1, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }

        if (it.id == R.id.button2) {
            etPassCodeText?.append("2")
            inputEditText?.append("2")
            listener?.onNumberClicked(2, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button3) {
            etPassCodeText?.append("3")
            inputEditText?.append("3")
            listener?.onNumberClicked(3, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button4) {
            etPassCodeText?.append("4")
            inputEditText?.append("4")
            listener?.onNumberClicked(4, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button5) {
            etPassCodeText?.append("5")
            inputEditText?.append("5")
            listener?.onNumberClicked(5, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button6) {
            etPassCodeText?.append("6")
            inputEditText?.append("6")
            listener?.onNumberClicked(6, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button7) {

            etPassCodeText?.append("7")
            inputEditText?.append("7")
            listener?.onNumberClicked(7, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button8) {

            etPassCodeText?.append("8")
            inputEditText?.append("8")
            listener?.onNumberClicked(8, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button9) {

            etPassCodeText?.append("9")
            inputEditText?.append("9")
            listener?.onNumberClicked(9, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.button0) {

            etPassCodeText?.append("0")
            inputEditText?.append("0")
            listener?.onNumberClicked(0, etPassCodeText?.text.toString())
            if (dialerType == 1) {
                addListSizeForPasscode(dialerMaxLength)
            }
        }
        if (it.id == R.id.btnFingerPrint) {
            listener?.onLeftButtonClicked()
        }
        //onButtonClickListener?.onClick(it)
    }

    init {
        orientation = VERTICAL
        val lp =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(view, lp)
        etPassCodeText = view.findViewById(R.id.etPassCodeText)
        buttonRemove = view.findViewById(R.id.buttonRemove)
        // editText = etPassCodeText

        attrs?.let { it ->
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CoreDialerPad, 0, 0)
            dialerType = typedArray.getInt(R.styleable.CoreDialerPad_dialer_pass_code, 0)
            //dialerMaxLength = typedArray.getInt(R.styleable.CoreDialerPad_dialer_max_length, 6)
            dialerMaxLength = 6 //To enforce 6 chars
            etPassCodeText?.filters =
                arrayOf<InputFilter>(InputFilter.LengthFilter(dialerMaxLength))
            keypads.add(view.findViewById<View>(R.id.button1))
            keypads.add(view.findViewById<View>(R.id.button2))
            keypads.add(view.findViewById<View>(R.id.button3))
            keypads.add(view.findViewById<View>(R.id.button4))
            keypads.add(view.findViewById<View>(R.id.button5))
            keypads.add(view.findViewById<View>(R.id.button6))
            keypads.add(view.findViewById<View>(R.id.button7))
            keypads.add(view.findViewById<View>(R.id.button8))
            keypads.add(view.findViewById<View>(R.id.button9))
            keypads.add(view.findViewById<View>(R.id.button0))
            keypads.add(view.findViewById<View>(R.id.btnFingerPrint))

            if (dialerType == 1) performPassCode()
            addClickListeners()
            setButtonDimensions()

            view.findViewById<ImageView>(R.id.buttonRemove).setOnClickListener {
                removePasscodeFromList()
                val length = etPassCodeText?.length()!!
                if (length > 0) etPassCodeText?.text?.delete(length - 1, length)
                inputEditText?.let {
                    if (it.length() > 0) it.text.delete(it.length() - 1, it.length())
                }
                listener?.onRightButtonClicked()
            }
        }
    }

    private fun setButtonDimensions() {
        val vto = this.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val topPanelHeight =
                    view.findViewById<ConstraintLayout>(R.id.clPassCodeViews).height

                val row1 =
                    view.findViewById<LinearLayout>(R.id.llPasscodeFirstRow)
                val row2 =
                    view.findViewById<LinearLayout>(R.id.llPasscodeSecondRow)
                val row3 =
                    view.findViewById<LinearLayout>(R.id.llPasscodeThirdRow)
                val row4 =
                    view.findViewById<LinearLayout>(R.id.llPasscodeFourthRow)
                val tvError = view.findViewById<TextView>(R.id.tvError)


                val h = height - topPanelHeight
                val dimension = ((h / 10) * 1.4).toInt()
                val keyDimen = ((h / 10) * 1.4).toInt()
                for (view in keypads) {
                    val prams = view.layoutParams
                    prams.width = keyDimen
                    prams.height = keyDimen
                }
                val pramsrow1 = row1.layoutParams as ConstraintLayout.LayoutParams
                pramsrow1.setMargins(0, 0, 0, 0);
                row1.layoutParams = pramsrow1

                val error = tvError.layoutParams as ConstraintLayout.LayoutParams
                error.setMargins(0, (dimension / 2.5).toInt(), 0, 0);
                tvError.layoutParams = error


                val pramsrow2 = row2.layoutParams as ConstraintLayout.LayoutParams
                pramsrow2.setMargins(0, 0, 0, 0);
                row2.layoutParams = pramsrow2

                val pramsrow3 = row3.layoutParams as ConstraintLayout.LayoutParams
                pramsrow3.setMargins(0, 0, 0, 0);
                row3.layoutParams = pramsrow3

                val pramsrow4 = row4.layoutParams as ConstraintLayout.LayoutParams
                pramsrow4.setMargins(0, 0, 0, 0);
                row4.layoutParams = pramsrow4

                val rm = view.findViewById<ImageView>(R.id.buttonRemove)
                val prams = rm.layoutParams as LinearLayout.LayoutParams
                prams.width = keyDimen
                prams.height = keyDimen
                rm.layoutParams = prams
                rm.setPadding((keyDimen / 3.7).toInt())

                viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
            }
        })
    }


    private fun addClickListeners() {
        for (view in keypads)
            view.setOnClickListener(onClickListener)
    }

    private fun lockKeypad() {
        for (view in keypads) {
            view.isEnabled = false
            buttonRemove?.isEnabled = false
            view.alpha = 0.5f
            buttonRemove?.alpha = 0.5f
        }
    }

    private fun unlockKeypad() {
        for (view in keypads) {
            view.isEnabled = true
            buttonRemove?.isEnabled = true
            view.alpha = 1.0f
            buttonRemove?.alpha = 1.0f
            clearAllData()
        }
    }

    fun setInPutEditText(editText: EditText?) {
        if (!showDialerPassCodeView) {
            inputEditText = editText
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    fun getText(): String {
        return etPassCodeText?.text.toString()
    }

    private fun getInputEditText(): String {
        return inputEditText?.text.toString()
    }

    fun clearAllData() {
        etPassCodeText?.setText("")
        inputEditText?.setText("")
        list.clear()
        view.findViewById<ImageView>(R.id.ivOne).visibility = View.GONE
        view.findViewById<ImageView>(R.id.ivTwo).visibility = View.GONE
        view.findViewById<ImageView>(R.id.ivThree).visibility = View.GONE
        view.findViewById<ImageView>(R.id.ivFour).visibility = View.GONE
        view.findViewById<ImageView>(R.id.ivFive).visibility = View.GONE
        view.findViewById<ImageView>(R.id.ivSix).visibility = View.GONE
    }

    fun reset() {
        etPassCodeText?.text = null
        //editText.text = null
        etPassCodeText?.setText("")
        inputEditText?.setText("")
        //editText.setText("")
//        removeError()
    }

    fun updateDialerLength(length: Int) {
        dialerMaxLength = length
        etPassCodeText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(dialerMaxLength))
    }


    fun startAnimation() {
        view.findViewById<LinearLayout>(R.id.llPasscode).startAnimation(animShake)
    }


    fun showError(error: String) {
        tvError.visibility = View.VISIBLE
        tvError.text = error
        setButtonDimensions()
    }

    fun removeError() {
        tvError.visibility = View.GONE
        setButtonDimensions()
    }

    fun settingUIForError(isScreenLocked: Boolean = false) {
        if (isScreenLocked) lockKeypad() else unlockKeypad()
    }

    fun setPasscodeVisiblity(isAccountBlocked: Boolean) {
        if (isAccountBlocked)
            llPasscode?.visibility = View.GONE
        else
            llPasscode?.visibility = View.VISIBLE
    }

    fun showFingerprintView() {
        view.findViewById<ImageButton>(R.id.btnFingerPrint).visibility = View.VISIBLE
    }

    fun hideFingerprintView() {
        view.findViewById<ImageButton>(R.id.btnFingerPrint).visibility = View.INVISIBLE
    }


    fun settingUIForNormal(isScreenLocked: Boolean = false) {
        if (isScreenLocked) lockKeypad() else unlockKeypad()
    }


    fun performPassCode() {
        etPassCodeText?.textSize =
            resources.getDimension(R.dimen.text_size_h1) //R.dimen.margin_xxl.toFloat()
        etPassCodeText?.visibility = View.GONE
        llPasscode.visibility = View.VISIBLE
        setDrawableTint()
    }

    @SuppressLint("ResourceType")
    fun setDrawableTint() {
        context.getDrawable(R.drawable.ic_fingerprint_purple)?.let {
            val drawableRight: Drawable =
                DrawableCompat.wrap(it)
            if (SharedPreferenceManager(context).getThemeValue()
                    .equals(Constants.THEME_HOUSEHOLD)
            ) {
                DrawableCompat.setTint(drawableRight, Color.RED);
            }

            btnFingerPrint.setImageDrawable(
                drawableRight
            )
        }
    }

    fun upDatedDialerPad(passcode: String? = null) {
        passcode?.let {
            etPassCodeText?.setText(it)
        }
        if (passcode == null) {
            updateDialerPadValues(etPassCodeText?.length() ?: 0)
        } else {
            updateDialerPadValues(passcode.length)
        }
    }

    private fun addListSizeForPasscode(dialerLength: Int) {
        if (list.size < dialerLength) {
            list.add(1)
        }
        if (list.size == 1) {
            view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
        } else if (list.size == 2) {
            view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
        } else if (list.size == 3) {
            view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
        } else if (list.size == 4) {
            view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivFour).visibility = View.VISIBLE
        } else if (list.size == 5) {
            view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivFour).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivFive).visibility = View.VISIBLE
        } else if (list.size == 6) {
            view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivFour).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivFive).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.ivSix).visibility = View.VISIBLE
        }
    }

    private fun updateDialerPadValues(dialerLength: Int) {

        when (dialerLength) {
            4 -> {
                list.add(1)
                list.add(1)
                list.add(1)
                list.add(1)
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.VISIBLE
                ivFour.visibility = View.VISIBLE
            }
            5 -> {
                list.add(1)
                list.add(1)
                list.add(1)
                list.add(1)
                list.add(1)
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.VISIBLE
                ivFour.visibility = View.VISIBLE
                ivFive.visibility = View.VISIBLE
            }
            6 -> {
                list.add(1)
                list.add(1)
                list.add(1)
                list.add(1)
                list.add(1)
                list.add(1)
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.VISIBLE
                ivFour.visibility = View.VISIBLE
                ivFive.visibility = View.VISIBLE
                ivSix.visibility = View.VISIBLE
            }
        }
    }

    private fun removePasscodeFromList() {
        if (dialerType == 1) {
            list.remove(1)
            if (list.size == 0) {
                view.findViewById<ImageView>(R.id.ivOne).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivTwo).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivThree).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivFour).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivFive).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivSix).visibility = View.GONE
            } else if (list.size == 1) {
                view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivTwo).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivThree).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivFour).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivFive).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivSix).visibility = View.GONE
            } else if (list.size == 2) {
                view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivThree).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivFour).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivFive).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivSix).visibility = View.GONE
            } else if (list.size == 3) {
                view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivFour).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivFive).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivSix).visibility = View.GONE
            } else if (list.size == 4) {
                view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivFour).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivFive).visibility = View.GONE
                view.findViewById<ImageView>(R.id.ivSix).visibility = View.GONE
            } else if (list.size == 5) {
                view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivFour).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivFive).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivSix).visibility = View.GONE
            } else if (list.size == 6) {
                view.findViewById<ImageView>(R.id.ivOne).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivTwo).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivThree).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivFour).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivFive).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.ivSix).visibility = View.VISIBLE
            }
        }
    }

    /**
     * Sets keyboard listener.
     */
    fun setNumberKeyboardListener(listener: NumberKeyboardListener?) {
        this.listener = listener
    }
}

///**
// * Enables to listen keyboard events.
// */
interface NumberKeyboardListener {

    /**
     * Invoked when a number key is clicked.
     */
    fun onNumberClicked(number: Int, text: String) {}

    /**
     * Invoked when the left auxiliary button is clicked.
     */
    fun onLeftButtonClicked() {}

    /**
     * Invoked when the right auxiliary button is clicked.
     */
    fun onRightButtonClicked() {}
}





