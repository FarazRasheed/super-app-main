package co.yap.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import co.yap.yapcore.R
import kotlinx.android.synthetic.main.layout_pop_up_window.view.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("CustomViewStyleable")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CorePopUpWindow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyle, defStyleRes) {
    private lateinit var mPopupWindow: PopupWindow
    private lateinit var popupView: View

    private val TYPE_WRAP_CONTENT = 0
    private val TYPE_MATCH_PARENT = 1

    lateinit var typedArray: TypedArray

    private var viewDataBinding: ViewDataBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.layout_pop_up_window,
        this,
        true
    )

    init {
        viewDataBinding.executePendingBindings()
        attrs?.let {
            typedArray = context.obtainStyledAttributes(it, R.styleable.CorePopUpWindow, 0, 0)
            val title = resources.getText(
                typedArray
                    .getResourceId(R.styleable.CorePopUpWindow_textValue, R.string.empty_string)
            )

            ivPopUp.setOnClickListener { v ->
                 val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                // Inflate a custom view using layout inflater
                popupView = inflater.inflate(R.layout.pop_up_view, null)
                val tvContent = popupView.findViewById<TextView>(R.id.tvContent)
                tvContent.text = title
                showAsPopUp(root_layout)
            }

            typedArray.recycle()
        }

    }

    private fun initPopupWindow(type: Int) {
        if (type == TYPE_WRAP_CONTENT) {
            mPopupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else if (type == TYPE_MATCH_PARENT) {
            mPopupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        mPopupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
    }

    private fun showAsPopUp(anchor: View) {
        showAsPopUp(anchor, 0, 0)
    }

    private fun showAsPopUp(anchor: View, xoff: Int, yoff: Int) {
        initPopupWindow(TYPE_MATCH_PARENT)
        mPopupWindow.animationStyle = R.style.AnimationUpPopup
        popupView.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val height: Int = popupView.measuredHeight
        val location = IntArray(2)
//        anchor.getLocationInWindow(location)
        mPopupWindow.showAtLocation(
            anchor,
            Gravity.CENTER,
            0,0
        )
    }

    /**
     * touch outside dismiss the popupwindow, default is ture
     * @param isCancelable
     */
    private fun setCancelable(isCancelable: Boolean) {
        if (isCancelable) {
            mPopupWindow.isOutsideTouchable = true
            mPopupWindow.isFocusable = true
        } else {
            mPopupWindow.isOutsideTouchable = false
            mPopupWindow.isFocusable = false
        }
    }
}
