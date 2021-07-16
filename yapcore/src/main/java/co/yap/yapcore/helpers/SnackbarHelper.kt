package co.yap.yapcore.helpers

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import co.yap.yapcore.R
import co.yap.yapcore.helpers.extentions.dimen
import co.yap.yapcore.helpers.extentions.toastNow
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.Snackbar


fun Activity?.showSnackBar(
    msg: String,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG
) {
    val snakbar = Snackbar.make(
        this?.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.show(gravity)
}

fun Activity.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    actionText: CharSequence,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    val snakbar = Snackbar.make(
        this.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.view.setBackgroundColor(ContextCompat.getColor(this, viewBgColor))
    snakbar.setTextColor(ContextCompat.getColor(this, colorOfMessage))
    val snackRootView = snakbar.view
    val snackTextView = snackRootView
        .findViewById<TextView>(R.id.snackbar_text)
    snackTextView.setTextAppearance(R.style.AppFontLight)
    setMaxLines(snackTextView)
    snakbar.setAction(actionText, clickListener)
    snakbar.show(gravity)
}


fun Activity.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    actionText: String,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    val snakbar = Snackbar.make(
        this.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.view.setBackgroundColor(ContextCompat.getColor(this, viewBgColor))
    snakbar.setTextColor(ContextCompat.getColor(this, colorOfMessage))
    val snackRootView = snakbar.view
    val snackTextView = snackRootView
        .findViewById<TextView>(R.id.snackbar_text)
    snackTextView.setTextAppearance( R.style.AppFontLight)
    setMaxLines(snackTextView)
    snakbar.setAction(actionText, clickListener)
    snakbar.show(gravity)
}


fun Activity?.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG,
    marginTop: Int = this?.dimen(R.dimen.toolbar_height) ?: 0,
    marginBottom: Int = this?.dimen(R.dimen.margin_zero_dp) ?: 0
): Snackbar? {
    this?.let {
        val snakbar = Snackbar
            .make(
                it.window?.decorView?.findViewById(android.R.id.content)!!,
                validateString(msg),
                duration
            )
        snakbar.view.setBackgroundColor(ContextCompat.getColor(it, viewBgColor))
        val snackRootView = snakbar.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        snackTextView.setTextAppearance(R.style.Micro)
        snakbar.setTextColor(ContextCompat.getColor(snakbar.view.context, colorOfMessage))
        setMaxLines(snackTextView)
        snakbar.config(marginTop, marginBottom)
        cancelAllSnackBar()
        snakbar.show(gravity)
        return snakbar
    }
    return null
}

// for activity and action
fun Activity.showSnackBar(
    msg: String,
    actionText: String,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    val snakbar = Snackbar
        .make(
            this.window.decorView.findViewById(android.R.id.content),
            validateString(msg),
            duration
        )
        .setAction(actionText, clickListener)
    snakbar.show(gravity)
}

fun Context?.showSnackBar(msg: String) {
    if (this is Activity) {
        showSnackBar(msg)
    } else {
        toastNow(msg)
    }
}

fun Fragment?.showSnackBar(
    msg: String,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG
) {
    val snakbar = Snackbar.make(
        this?.requireActivity()?.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.show(gravity)
}

fun Fragment.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    actionText: String,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    val snakbar = Snackbar.make(
        this.requireActivity().window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.view.setBackgroundColor(ContextCompat.getColor(this.requireActivity(), viewBgColor))
    snakbar.setTextColor(ContextCompat.getColor(this.requireActivity(), colorOfMessage))
    val snackRootView = snakbar.view
    val snackTextView = snackRootView
        .findViewById<TextView>(R.id.snackbar_text)
    snackTextView.setTextAppearance( R.style.AppFontLight)
    setMaxLines(snackTextView)
    snakbar.setAction(actionText, clickListener)
    snakbar.show(gravity)
}

fun Fragment.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    actionText: CharSequence,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    val snakbar = Snackbar.make(
        this.requireActivity()?.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.view.setBackgroundColor(ContextCompat.getColor(this.requireActivity(), viewBgColor))
    snakbar.setTextColor(ContextCompat.getColor(this.requireActivity(), colorOfMessage))
    val snackRootView = snakbar.view
    val snackTextView = snackRootView
        .findViewById<TextView>(R.id.snackbar_text)
    snackTextView.setTextAppearance( R.style.AppFontLight)
    setMaxLines(snackTextView)
    snakbar.setAction(actionText, clickListener)
    snakbar.show(gravity)
}

fun Fragment?.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG,
    marginTop: Int = this?.requireContext()?.dimen(R.dimen.toolbar_height) ?: 0,
    marginBottom: Int = this?.requireContext()?.dimen(R.dimen.margin_zero_dp) ?: 0
): Snackbar? {
    this?.let {
        val snakbar = Snackbar
            .make(
                it.requireActivity().window?.decorView?.findViewById(android.R.id.content)!!,
                validateString(msg),
                duration
            )
        snakbar.behavior = NoSwipeBehavior()
        snakbar.view.setBackgroundColor(ContextCompat.getColor(it.requireContext(), viewBgColor))
        val snackRootView = snakbar.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        snackTextView.setTextAppearance(R.style.Micro)
        snakbar.setTextColor(ContextCompat.getColor(snakbar.view.context, colorOfMessage))
        setMaxLines(snackTextView)
        snakbar.config(marginTop, marginBottom)
        cancelAllSnackBar()
        snakbar.show(gravity)
        return snakbar
    }
    return null
}

// for activity and action
fun Fragment?.showSnackBar(
    msg: String,
    actionText: String,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    val snackbar = Snackbar
        .make(
            this?.requireActivity()?.window?.decorView?.findViewById(android.R.id.content)!!,
            validateString(msg),
            duration
        )
        .setAction(actionText, clickListener)
    snackbar.show(gravity)
}


// for styling view and action color action
fun View?.showSnackBar(
    viewBgColor: Int,
    colorOfMessage: Int,
    snackBarMsg: String,
    isCapsMesg: Boolean,
    messageSize: Int,
    actionTextColor: Int,
    actionText: String, gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {

    val snackbar = Snackbar.make(this!!, validateString(snackBarMsg), duration)
    val snackbarView: View = snackbar.view

    // styling for rest of text

    /* val textView : TextView = snackbarView.findViewById(android.support.design.R.id.snackbar_text)
     textView.setTextColor(colorOfMessage)
     textView.setAllCaps(isCapsMesg)
     textView.setTextSize((if (messageSize < 10) 20 else messageSize).toFloat())*/


    // styling for background of snackbar

    snackbarView.setBackgroundColor(viewBgColor)
    //styling for action of text
    snackbar.setActionTextColor(actionTextColor)
    snackbar.setAction(actionText, clickListener)
    snackbar.show(gravity)
}

fun View?.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    actionText: CharSequence,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    this?.let {
        val snakbar = Snackbar.make(
            it,
            "",
            duration
        )
        val layout = snakbar.view as Snackbar.SnackbarLayout
        val layoutInflater: LayoutInflater = LayoutInflater.from(it.context)
        val snackView = layoutInflater.inflate(R.layout.snackbar_card_status, null)
        layout.addView(snackView, 0)
        snakbar.behavior = NoSwipeBehavior()
        snakbar.view.setBackgroundColor(ContextCompat.getColor(it.context, viewBgColor))
        val tvMessage = layout.findViewById(R.id.tvMessage) as TextView
        tvMessage.text = validateString(msg)
        val tvAction = layout.findViewById(R.id.tvAction) as TextView
        tvAction.text = actionText
        tvAction.setOnClickListener(clickListener)
        snakbar.show(gravity)
    }

}

private fun Snackbar.show(gravity: Int = Gravity.BOTTOM, addToQueue: Boolean = true) {
    val view = this.view

    when (view.layoutParams) {
        is CoordinatorLayout.LayoutParams -> {
            val param = view.layoutParams as CoordinatorLayout.LayoutParams
            param.gravity = gravity
            view.layoutParams = param
        }
        is LinearLayout.LayoutParams -> {
            val param = view.layoutParams as LinearLayout.LayoutParams
            param.gravity = gravity
        }
        else -> {
            val param = view.layoutParams as FrameLayout.LayoutParams
            param.gravity = gravity
            view.layoutParams = param
        }
    }

    if (view.layoutParams is CoordinatorLayout.LayoutParams) {
        val param = view.layoutParams as CoordinatorLayout.LayoutParams
        param.gravity = gravity
        view.layoutParams = param
    }
    this.animationMode = ANIMATION_MODE_FADE
//    val fadeIn = AlphaAnimation(0f, 1f)
//    fadeIn.interpolator = DecelerateInterpolator() //add this
//    fadeIn.duration = 1000
//
//    val fadeOut = AlphaAnimation(1f, 0f)
//    fadeOut.interpolator = AccelerateInterpolator() //and this
//    fadeOut.startOffset = 1000
//    fadeOut.duration = 1000
//
//    val animation = AnimationSet(false) //change to false
//    animation.addAnimation(fadeIn)
//    animation.addAnimation(fadeOut)
//    view.animation = animation
    if (addToQueue) {
        SnackBarQueue.snackBarQueue.add(this)
    }
    this.addCallback(object : Snackbar.Callback() {
        override fun onShown(sb: Snackbar?) {
            super.onShown(sb)
        }

        override fun onDismissed(sb: Snackbar?, event: Int) {
            super.onDismissed(sb, event)
            // SnackBarQueue.snackBarQueue.remove(sb)

        }
    })
    this.show()
}

fun cancelAllSnackBar() =
    SnackBarQueue.cancelSnackBars()

// for view and action
fun View?.showSnackBar(
    msg: String,
    actionText: String,
    gravity: Int = Gravity.BOTTOM, duration: Int = Snackbar.LENGTH_LONG,
    clickListener: View.OnClickListener
) {
    this?.let {
        val snakbar = Snackbar
            .make(it, validateString(msg), duration)
            .setAction(actionText, clickListener)
        snakbar.show(gravity)
    }

}

fun View?.showSnackBar(
    msg: String, @ColorRes viewBgColor: Int, @ColorRes colorOfMessage: Int,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG,
    marginTop: Int = this?.context?.dimen(R.dimen.toolbar_height) ?: 0,
    marginBottom: Int = this?.context?.dimen(R.dimen.margin_zero_dp) ?: 0
): Snackbar? {
    this?.let {
        val snakbar = Snackbar
            .make(it, validateString(msg), duration)
        snakbar.behavior = NoSwipeBehavior()
        snakbar.view.setBackgroundColor(ContextCompat.getColor(it.context, viewBgColor))
        val snackRootView = snakbar.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        snackTextView.setTextAppearance(R.style.Micro)
        setMaxLines(snackTextView)
        snakbar.setTextColor(ContextCompat.getColor(snakbar.view.context, colorOfMessage))
        snakbar.config(marginTop, marginBottom)
        cancelAllSnackBar()
        snakbar.show(gravity)
        return snakbar
    }
    return null
}

private fun Snackbar.config(
    marginTop: Int = this.view.context.dimen(android.R.attr.actionBarSize),
    marginBottom: Int = this.view.context.dimen(R.dimen.margin_zero_dp)
) {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(0, marginTop, 0, marginBottom)
    this.view.layoutParams = params
    ViewCompat.setElevation(this.view, 6f)
}

fun Snackbar?.updateSnackBarText(msg: String) {
    this?.let {
        val snackRootView = this.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        snackTextView.text = validateString(msg)
        setMaxLines(snackTextView)
    }
}

fun Fragment?.showTextUpdatedAbleSnackBar(errorMessage: String, length: Int) {
    this?.let {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(errorMessage)
            }
        } ?: showSnackBar(
            msg = errorMessage,
            viewBgColor = R.color.errorLightBackground, gravity = Gravity.TOP,
            colorOfMessage = R.color.error, duration = length
        )
    }
}
fun Activity?.showTextUpdatedAbleSnackBar(errorMessage: String, length: Int) {
    this?.let {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(errorMessage)
            }
        } ?: showSnackBar(
            msg = errorMessage,
            viewBgColor = R.color.errorLightBackground, gravity = Gravity.TOP,
            colorOfMessage = R.color.error, duration = length
        )
    }
}

fun validateString(msg: String?): String {
    return msg ?: "null"
}

fun getSnackBarQueue() = SnackBarQueue.snackBarQueue

fun getSnackBarFromQueue(index: Int): Snackbar? {
    return if (getSnackBarQueue().size > 0) {
        SnackBarQueue.snackBarQueue[index]
    } else
        null

}

private fun setMaxLines(snackTextView: TextView) {
    snackTextView.maxLines = 5
}

private object SnackBarQueue {
    val snackBarQueue = mutableListOf<Snackbar>()

    fun cancelSnackBars() {
        snackBarQueue.forEach { it.dismiss() }
        snackBarQueue.clear()
    }

    fun removeSnackBar(snackBar: Snackbar) = snackBarQueue.remove(snackBar)

}
internal class NoSwipeBehavior : BaseTransientBottomBar.Behavior() {
    override fun canSwipeDismissView(child: View): Boolean {
        return false
    }
}