package co.yap.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import co.yap.yapcore.R
import co.yap.yapcore.binders.UIBinder
import kotlinx.android.synthetic.main.layout_core_toolbar.view.*


class CoreToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    var titleTranslator: String? = null
        set(value) {
            field = value
            titleTranslator?.let {
                if (it.isNotEmpty()) {
                    UIBinder.setText(toolbarTitle, it)
                    invalidate()
                }
            }

        }
    var titleString: String? = null
        set(value) {
            field = value
            titleString?.let {
                if (it.isNotEmpty()) {
                    toolbarTitle.text = it
                    invalidate()
                }
            }
        }

    var rightTitle: String? = null
        set(value) {
            field = value
            rightTitle?.let {
                tvRightText.text = rightTitle
                invalidate()
            }
        }

    var rightTitleTranslator: String? = null
        set(value) {
            field = value
            rightTitleTranslator?.let {
                UIBinder.setText(tvRightText, it)
                invalidate()
            }
        }

    var leftIconVisibility: Boolean? = null
        set(value) {
            field = value
            ivLeftIcon.visibility = if (leftIconVisibility == true) View.VISIBLE else View.INVISIBLE
            invalidate()

        }

    var bottomLineVisibility: Boolean? = null
        set(value) {
            field = value
            vBottomLine.visibility =
                if (bottomLineVisibility == true) View.VISIBLE else View.INVISIBLE
            invalidate()

        }

    var titleVisibility: Boolean? = null
        set(value) {
            field = value
            toolbarTitle.visibility =
                if (titleVisibility == true) View.VISIBLE else View.INVISIBLE
            invalidate()

        }
    var rightIconVisibility: Boolean? = null
        set(value) {
            field = value
            ivRightIcon.visibility =
                if (rightIconVisibility == true) View.VISIBLE else View.INVISIBLE
            invalidate()

        }

    var rightTitleVisibility: Boolean? = null
        set(value) {
            field = value
            tvRightText.visibility =
                if (rightTitleVisibility == true) View.VISIBLE else View.GONE
            invalidate()

        }

    var rigthTitleDisabled: Boolean = true
        set(value) {
            field = value
            tvRightText.isEnabled = value
            if (!value) tvRightText.alpha = 0.5f
            else tvRightText.alpha = 1f
            invalidate()
        }

    var rightIcon: Int? = null
        set(value) {
            field = value
            rightIcon?.let {
                UIBinder.setImageResId(ivRightIcon, it)
                invalidate()
            }
        }

    var leftIcon: Int? = null
        set(value) {
            field = value
            leftIcon?.let {
                UIBinder.setImageResId(ivLeftIcon, it)
                invalidate()
            }
        }

    var titleDrawableEnd: Drawable? = null
        set(value) {
            field = value
            titleDrawableEnd?.let {
                it.setBounds(0, 0, 60, 60)
                toolbarTitle.setCompoundDrawables(null, null, it, null)
                invalidate()
            }
        }
    var onClick: OnClickListener? = null
        set(value) {
            field = value
            tvRightText.setOnClickListener(onClick)
            ivRightIcon.setOnClickListener(onClick)
            ivLeftIcon.setOnClickListener(onClick)
            toolbarTitle.setOnClickListener(onClick)
            invalidate()
        }


    var view: View = LayoutInflater.from(context)
        .inflate(R.layout.layout_core_toolbar, this, true)

    init {
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.CoreToolbar, 0, 0)
            typedArray.recycle()
        }
    }
}