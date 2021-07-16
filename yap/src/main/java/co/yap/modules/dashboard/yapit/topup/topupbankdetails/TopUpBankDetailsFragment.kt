package co.yap.modules.dashboard.yapit.topup.topupbankdetails

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentTopUpBankDetailsBinding
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.toast
import co.yap.yapcore.managers.SessionManager

class TopUpBankDetailsFragment : BaseBindingFragment<ITopUpBankDetails.ViewModel>(),
    ITopUpBankDetails.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_top_up_bank_details

    override val viewModel: ITopUpBankDetails.ViewModel
        get() = ViewModelProviders.of(this).get(TopUpBankDetailsViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clickEvent.observe(this, clickEvent)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawableClick(getBindings().layoutUserBankDetails.tvIBan)
        drawableClick(getBindings().layoutUserBankDetails.tvSwiftCode)
    }

    var clickEvent = Observer<Int> {
        when (it) {
            R.id.btnShare -> {
                shareInfo()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun drawableClick(view: EditText?) {
        view?.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (view.right - view.compoundDrawables[DRAWABLE_RIGHT].bounds
                        .width())
                ) {
                    context?.let { context ->
                        Utils.copyToClipboard(context, view.text)
                        toast("Copied to clipboard")
                    }

                    // your action for drawable click event
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun shareInfo() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        // not set because ios team is not doing this.
        //sharingIntent.putExtra(Intent.EXTRA_SUBJECT, viewModel.state.title.get())
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getBody())
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    }

    private fun getBody(): String {
        return "Name: ${SessionManager.user?.currentCustomer?.getFullName()}\n" +
                "IBAN: ${SessionManager.user?.iban}\n" +
                "Swift/BIC: ${SessionManager.user?.bank?.swiftCode}\n" +
                "Account: ${SessionManager.user?.accountNo}\n" +
                "Bank: ${SessionManager.user?.bank?.name}\n" +
                "Address: ${SessionManager.user?.bank?.address}\n"
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                activity?.finish()
            }
        }
    }

    fun getBindings(): FragmentTopUpBankDetailsBinding =
        viewDataBinding as FragmentTopUpBankDetailsBinding
}