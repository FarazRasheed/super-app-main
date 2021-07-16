package co.yap.yapcore.helpers

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import co.yap.widgets.CoreButton
import co.yap.yapcore.BaseActivity
import co.yap.yapcore.R
import co.yap.yapcore.helpers.extentions.makeCall
import co.yap.yapcore.helpers.extentions.makeLinks
import co.yap.yapcore.managers.ChatManager
import co.yap.yapcore.managers.SessionManager

/**
 * Display AlertDialog instantly with confirm
 *
 * @param[title] optional, title
 * @param[message] to display
 * @param[positiveButton] optional, button text
 * @param[negativeButton] optional, button text
 * @param[cancelable] able to cancel
 * @param[callback] callback of click ok button
 */
@JvmOverloads
fun Context.confirm(
    message: String,
    title: String = "",
    positiveButton: String? = null,
    negativeButton: String? = null,
    cancelable: Boolean = true,
    callback: DialogInterface.() -> Unit
) =
    AlertDialog.Builder(this).apply {
        if (title.isEmpty().not())
            setTitle(title)
        setMessage(message)
        setPositiveButton(
            positiveButton ?: getString(android.R.string.ok)
        ) { dialog, _ -> dialog.callback() }
        setNegativeButton(negativeButton ?: getString(android.R.string.no)) { _, _ -> }
        setCancelable(cancelable)
        show()
    }

/**
 * Display AlertDialog instantly with confirm
 *
 * @param[title] optional, title
 * @param[message] to display
 * @param[positiveButton] optional, button text
 * @param[negativeButton] optional, button text
 * @param[cancelable] able to cancel
 * @param[positiveCallback] callback of click ok button
 * @param[negativeCallback] callback of click cancel button
 */
@JvmOverloads
fun Context.confirm(
    message: String,
    title: String = "",
    positiveButton: String? = null,
    negativeButton: String? = null,
    cancelable: Boolean = true,
    positiveCallback: DialogInterface.() -> Unit,
    negativeCallback: DialogInterface.() -> Unit
) =
    AlertDialog.Builder(this).apply {
        if (title.isEmpty().not())
            setTitle(title)
        setMessage(message)
        setPositiveButton(
            positiveButton ?: getString(android.R.string.ok)
        )
        { dialog, _ -> dialog.positiveCallback() }
        setNegativeButton(
            negativeButton ?: getString(android.R.string.no)
        )
        { dialog, _ -> dialog.negativeCallback() }
        setCancelable(cancelable)
        show()
    }

/**
 * Display AlertDialog instantly with confirm
 *
 * @param[title] optional, title
 * @param[message] to display
 * @param[positiveButton] optional, button text
 * @param[negativeButton] optional, button text
 * @param[cancelable] able to cancel
 * @param[callback] callback of click ok button
 */
@JvmOverloads
fun Fragment.confirm(
    message: String,
    title: String = "",
    positiveButton: String? = "Yes",
    negativeButton: String? = "No",
    cancelable: Boolean = true,
    callback: DialogInterface.() -> Unit
) =
    AlertDialog.Builder(requireContext()).apply {
        if (title.isEmpty().not())
            setTitle(title)
        setMessage(message)
        setPositiveButton(
            positiveButton ?: getString(android.R.string.ok)
        )
        { dialog, _ -> dialog.callback() }
        setNegativeButton(negativeButton ?: getString(android.R.string.no)) { _, _ -> }
        setCancelable(cancelable)
        show()
    }

/**
 * Display AlertDialog instantly
 *
 * @param[title] optional, title
 * @param[message] to display
 * @param[positiveButton] optional, button text
 * @param[cancelable] able to cancel
 * @param[callback] callback of click ok button
 */
@JvmOverloads
fun Context.alert(
    message: String,
    title: String = "",
    positiveButton: String? = null,
    cancelable: Boolean = true,
    callback: (DialogInterface) -> Unit = {}
) =
    AlertDialog.Builder(this).apply {
        if (title.isEmpty().not())
            setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButton ?: getString(android.R.string.ok)) { dialog, _ ->
            callback(
                dialog
            )
        }
        setCancelable(cancelable)
        show()
    }


fun Context.showYapAlertDialog(
    title: String? = null,
    message: String?
) {
    val builder = android.app.AlertDialog.Builder(this)
    var alertDialog: android.app.AlertDialog? = null
    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    title?.let { builder.setTitle(title) }
    val dialogLayout: View =
        inflater.inflate(R.layout.alert_dialogue, null)
    val label = dialogLayout.findViewById<TextView>(R.id.tvTitle)
    label.text = message
    val ok = dialogLayout.findViewById<TextView>(R.id.tvButtonTitle)
    ok.text = "OK"
    ok.setOnClickListener {
        alertDialog?.dismiss()
    }

    builder.setView(dialogLayout)
    builder.setCancelable(false)
    alertDialog = builder.create()

    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    alertDialog.show()

}

fun Activity.showAlertCustomDialog(
    title: String? = "",
    message: String? = "",
    buttonText: String? = "OK"
) {
    val dialogLayout = Dialog(this)
    dialogLayout.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialogLayout.setCancelable(false)
    dialogLayout.setContentView(R.layout.alert_dialogue_custom)
    val dialogTitle = dialogLayout.findViewById<TextView>(R.id.tvDialogTitle)
    val label = dialogLayout.findViewById<TextView>(R.id.tvTitle)
    label.text = message
    dialogTitle.text = title
    val ok = dialogLayout.findViewById<CoreButton>(R.id.btnAction)
    ok.text = buttonText
    ok.setOnClickListener {
        dialogLayout.dismiss()
    }
    dialogLayout.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialogLayout.show()

}

fun Activity.showAlertDialogAndExitApp(
    Title: String? = null,
    dialogTitle: String? = "",
    message: String?,
    leftButtonText: String = "OK",
    rightButtonText: String = "Cancel",
    callback: () -> Unit = {},
    titleVisibility: Boolean = false,
    closeActivity: Boolean = true,
    isOtpBlocked: Boolean = false,
    isTwoButton: Boolean = false
) {
    val builder = android.app.AlertDialog.Builder(this)
    var alertDialog: android.app.AlertDialog? = null
    val inflater: LayoutInflater = layoutInflater
    Title?.let { builder.setTitle(Title) }
    val dialogLayout: View =
        inflater.inflate(R.layout.alert_dialogue, null)
    val label = dialogLayout.findViewById<TextView>(R.id.tvTitle)
    label.text = message
    val dTitle = dialogLayout.findViewById<TextView>(R.id.tvDialogTitle)
    val cancel = dialogLayout.findViewById<TextView>(R.id.tvButtonCancel)
    val ok = dialogLayout.findViewById<TextView>(R.id.tvButtonTitle)
    val btnDivider = dialogLayout.findViewById<View>(R.id.btnDivider)
    ok.text = leftButtonText
    cancel.text = rightButtonText
    cancel.setOnClickListener {
        alertDialog?.dismiss()
    }
    if (titleVisibility) {
        dTitle.text = dialogTitle
        dTitle.visibility = View.VISIBLE
    }
    ok.setOnClickListener {
        alertDialog?.dismiss()
        if (closeActivity)
            finish()
        callback()
    }

    if (isTwoButton) {
        cancel.visibility = View.VISIBLE
        btnDivider.visibility = View.VISIBLE
    }
    if (isOtpBlocked) {
        label.makeLinks(
            Pair(SessionManager.helpPhoneNumber, View.OnClickListener {
                makeCall(SessionManager.helpPhoneNumber)
            }),
            Pair("live chat", View.OnClickListener {
                ChatManager.config(ok.context as BaseActivity<*>)
            })
        )
    }

    builder.setView(dialogLayout)
    builder.setCancelable(false)
    alertDialog = builder.create()

    alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    alertDialog.show()

}
