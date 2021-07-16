package co.yap.modules.dashboard.yapit.topup.addtopupcard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import co.yap.R
import kotlinx.android.synthetic.main.dialog_add_topup_card.*

class AddTopUpCardDialog(context: Context) : Dialog(context) {
    private var onProceedListener: OnProceedListener? = null


    private fun setListeners() {
        btnLater.setOnClickListener {
            onProceedListener?.onProceed(it.id)
            dismiss()
        }
        done.setOnClickListener {
            onProceedListener?.onProceed(it.id)
            dismiss()
        }
    }


    interface OnProceedListener {
        fun onProceed(id: Int)
    }

    companion object {
        @JvmStatic
        fun newInstance(callback: OnProceedListener?, context: Context): AddTopUpCardDialog {
            val dialog =
                AddTopUpCardDialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_add_topup_card)
            dialog.onProceedListener = callback
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setListeners()
            return dialog
        }
    }
}

