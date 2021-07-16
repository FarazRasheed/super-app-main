package co.yap.widgets

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import co.yap.yapcore.R
import kotlinx.android.synthetic.main.core_dialer_pad.view.*


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("Recycle")
class CoreDialerPadHouseHold @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    var editText: EditText
    var list: ArrayList<Int> = ArrayList()
    var dialerType = 0
    var dialerMaxLength = 6
    val animShake = AnimationUtils.loadAnimation(context, R.anim.shake)
    var onButtonClickListener: View.OnClickListener? = null

    private val onClickListener: View.OnClickListener = OnClickListener {
        if (it.id == R.id.button1) {
            etPassCodeText.append("1")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }

        if (it.id == R.id.button2) {
            etPassCodeText.append("2")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button3) {
            etPassCodeText.append("3")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button4) {
            etPassCodeText.append("4")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button5) {
            etPassCodeText.append("5")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button6) {
            etPassCodeText.append("6")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button7) {
            etPassCodeText.append("7")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button8) {
            etPassCodeText.append("8")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button9) {
            etPassCodeText.append("9")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        if (it.id == R.id.button0) {
            etPassCodeText.append("0")
            if (dialerType == 1) {
                addListSizeForPasscode()
            }
        }
        onButtonClickListener?.onClick(it)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.core_dialer_pad, this, true)
        orientation = VERTICAL
        editText = etPassCodeText

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CoreDialerPad, 0, 0)
            dialerType = typedArray.getInt(R.styleable.CoreDialerPad_dialer_pass_code, 0)
            dialerMaxLength = typedArray.getInt(R.styleable.CoreDialerPad_dialer_max_length, 6)
            etPassCodeText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(dialerMaxLength))

            if (dialerType == 1) performPassCode()

            button1.setOnClickListener(onClickListener)
            button2.setOnClickListener(onClickListener)
            button3.setOnClickListener(onClickListener)
            button4.setOnClickListener(onClickListener)
            button5.setOnClickListener(onClickListener)
            button6.setOnClickListener(onClickListener)
            button7.setOnClickListener(onClickListener)
            button8.setOnClickListener(onClickListener)
            button9.setOnClickListener(onClickListener)
            button0.setOnClickListener(onClickListener)
            btnFingerPrint.setOnClickListener(onClickListener)

            buttonRemove.setOnClickListener {
                removePasscodeFromList()
                val length = etPassCodeText.length()
                if (length > 0) etPassCodeText.text.delete(length - 1, length)
            }
        }
    }

    fun getText(): String {
        return etPassCodeText.text.toString()
    }

    fun updateDialerLength(length: Int) {
        dialerMaxLength = length
        etPassCodeText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(dialerMaxLength))
    }


    fun startAnimation() {
        llPasscode.startAnimation(animShake)
    }

    fun startAnimationDigits() {
        etPassCodeText.startAnimation(animShake)
    }

    fun settingUIForError(error: String) {
        tvError.visibility = View.VISIBLE
        tvError.text = error
    }

    fun showFingerprintView() {
        btnFingerPrint.visibility = View.VISIBLE
    }

    fun hideFingerprintView() {
        btnFingerPrint.visibility = View.INVISIBLE
    }


    fun settingUIForNormal() {
        tvError.visibility = View.INVISIBLE
    }

    fun performPassCode() {
        etPassCodeText.textSize =
            resources.getDimension(R.dimen.text_size_h1) //R.dimen.margin_xxl.toFloat()
        etPassCodeText.visibility = View.GONE
        llPasscode.visibility = View.VISIBLE
        btnFingerPrint.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_fingerprint_purple,
                null
            )
        )
    }

    private fun addListSizeForPasscode() {
        if (list.size < 6) {
            list.add(1)
        }
        if (list.size == 1) {
            ivOne.visibility = View.VISIBLE
        } else if (list.size == 2) {
            ivOne.visibility = View.VISIBLE
            ivTwo.visibility = View.VISIBLE
        } else if (list.size == 3) {
            ivOne.visibility = View.VISIBLE
            ivTwo.visibility = View.VISIBLE
            ivThree.visibility = View.VISIBLE
        } else if (list.size == 4) {
            ivOne.visibility = View.VISIBLE
            ivTwo.visibility = View.VISIBLE
            ivThree.visibility = View.VISIBLE
            ivFour.visibility = View.VISIBLE
        } else if (list.size == 5) {
            ivOne.visibility = View.VISIBLE
            ivTwo.visibility = View.VISIBLE
            ivThree.visibility = View.VISIBLE
            ivFour.visibility = View.VISIBLE
            ivFive.visibility = View.VISIBLE
        } else if (list.size == 6) {
            ivOne.visibility = View.VISIBLE
            ivTwo.visibility = View.VISIBLE
            ivThree.visibility = View.VISIBLE
            ivFour.visibility = View.VISIBLE
            ivFive.visibility = View.VISIBLE
            ivSix.visibility = View.VISIBLE
        }
    }

    private fun removePasscodeFromList() {
        if (dialerType == 1) {
            list.remove(1)
            if (list.size == 0) {
                ivOne.visibility = View.GONE
                ivTwo.visibility = View.GONE
                ivThree.visibility = View.GONE
                ivFour.visibility = View.GONE
                ivFive.visibility = View.GONE
                ivSix.visibility = View.GONE
            } else if (list.size == 1) {
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.GONE
                ivThree.visibility = View.GONE
                ivFour.visibility = View.GONE
                ivFive.visibility = View.GONE
                ivSix.visibility = View.GONE
            } else if (list.size == 2) {
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.GONE
                ivFour.visibility = View.GONE
                ivFive.visibility = View.GONE
                ivSix.visibility = View.GONE
            } else if (list.size == 3) {
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.VISIBLE
                ivFour.visibility = View.GONE
                ivFive.visibility = View.GONE
                ivSix.visibility = View.GONE
            } else if (list.size == 4) {
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.VISIBLE
                ivFour.visibility = View.VISIBLE
                ivFive.visibility = View.GONE
                ivSix.visibility = View.GONE
            } else if (list.size == 5) {
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.VISIBLE
                ivFour.visibility = View.VISIBLE
                ivFive.visibility = View.VISIBLE
                ivSix.visibility = View.GONE
            } else if (list.size == 6) {
                ivOne.visibility = View.VISIBLE
                ivTwo.visibility = View.VISIBLE
                ivThree.visibility = View.VISIBLE
                ivFour.visibility = View.VISIBLE
                ivFive.visibility = View.VISIBLE
                ivSix.visibility = View.VISIBLE
            }
        }
    }
}





