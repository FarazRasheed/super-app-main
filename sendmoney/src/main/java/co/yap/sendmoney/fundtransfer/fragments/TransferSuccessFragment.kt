package co.yap.sendmoney.fundtransfer.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.sendmoney.InviteBottomSheet
import co.yap.sendmoney.fundtransfer.activities.BeneficiaryFundTransferActivity
import co.yap.sendmoney.fundtransfer.interfaces.ITransferSuccess
import co.yap.sendmoney.fundtransfer.viewmodels.TransferSuccessViewModel
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.databinding.FragmentTransferSuccessBinding
import co.yap.translation.Strings
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.SendMoneyBeneficiaryType


class TransferSuccessFragment : BeneficiaryFundTransferBaseFragment<ITransferSuccess.ViewModel>(),
    ITransferSuccess.View, InviteBottomSheet.OnItemClickListener {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_transfer_success

    override val viewModel: TransferSuccessViewModel
        get() = ViewModelProviders.of(this).get(TransferSuccessViewModel::class.java)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is BeneficiaryFundTransferActivity) {
            getBindings().flTransactionComplete.visibility = View.VISIBLE
            setData()
        }
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.confirmButton -> {
                    setResultData()
                }
            }
        })
    }

    private fun setResultData() {
        val intent = Intent()
        intent.putExtra(Constants.MONEY_TRANSFERED, true)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }


    override fun onResume() {
        setObservers()
        super.onResume()
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    private fun setData() {
        viewModel.parentViewModel?.beneficiary?.value?.let { beneficiary ->
                beneficiary.beneficiaryType?.let { beneficiaryType ->
                    if (beneficiaryType.isNotEmpty())
                        when (SendMoneyBeneficiaryType.valueOf(beneficiaryType)) {
                            SendMoneyBeneficiaryType.RMT -> {
                                setDataForRmt()
                            }
                            SendMoneyBeneficiaryType.SWIFT -> {
                                setDataForSwift()
                            }
                            SendMoneyBeneficiaryType.CASHPAYOUT -> {
                                setDataForCashPayout()
                            }
                            SendMoneyBeneficiaryType.DOMESTIC -> {
                                setDataForDomestic()
                            }
                            SendMoneyBeneficiaryType.UAEFTS -> {
                                setDataForUAEFTS()
                            }
                            else -> {
                                //common views

                            }
                        }
                }
            }
    }

    private fun setDataForCashPayout() {
        viewModel.state.flagLayoutVisibility = false
        viewModel.parentViewModel?.state?.toolbarVisibility?.set(false)
            viewModel.state.successHeader =
                getString(Strings.screen_cash_pickup_funds_transfer_display_text_title)
            viewModel.state.transferAmountHeading =
                getString(Strings.screen_cash_pickup_funds_transfer_display_text_amount_heading)
            viewModel.state.buttonTitle =
                getString(Strings.screen_cash_pickup_funds_transfer_back_to_dashboard)

    }

    private fun setDataForDomestic() {
        viewModel.state.flagLayoutVisibility = false
        viewModel.parentViewModel?.state?.leftIcon?.set(false)
            viewModel.state.successHeader =
                getString(Strings.screen_cash_pickup_funds_success_toolbar_header)
            viewModel.state.successHeader =
                getString(Strings.screen_domestic_funds_transfer_display_text_title)
            viewModel.state.transferAmountHeading =
                getString(Strings.screen_domestic_funds_transfer_display_text_amount_heading)
            viewModel.state.buttonTitle =
                getString(Strings.screen_cash_pickup_funds_transfer_back_to_dashboard)

    }

    private fun setDataForUAEFTS() {
        viewModel.state.flagLayoutVisibility = false
        viewModel.parentViewModel?.state?.toolbarVisibility?.set(false)
            viewModel.state.successHeader =
                getString(Strings.screen_cash_pickup_funds_transfer_display_text_title)
            viewModel.state.transferAmountHeading =
                getString(Strings.screen_cash_pickup_funds_transfer_display_text_amount_heading)
            viewModel.state.buttonTitle =
                getString(Strings.screen_cash_pickup_funds_transfer_back_to_dashboard)

        if (!viewModel.parentViewModel?.transferData?.value?.cutOffTimeMsg.isNullOrBlank())
            viewModel.state.cutOffMessage.set(
                getString(Strings.screen_international_funds_transfer_display_text_cutoff_time_uaefts)
            )

    }

    private fun setDataForSwift() {
        viewModel.state.flagLayoutVisibility = true
        viewModel.parentViewModel?.state?.toolbarVisibility?.set(false)
            viewModel.state.successHeader =
                getString(Strings.screen_international_funds_transfer_display_text_success_title)
            viewModel.state.transferAmountHeading =
                getString(Strings.screen_international_funds_transfer_display_text_amount_heading)
            viewModel.state.buttonTitle =
                getString(Strings.screen_international_funds_transfer_back_to_dashboard)

        if (!viewModel.parentViewModel?.transferData?.value?.cutOffTimeMsg.isNullOrBlank())
            viewModel.state.cutOffMessage.set(
                getString(Strings.screen_international_funds_transfer_display_text_cutoff_time_swift)
            )
    }

    private fun setDataForRmt() {
        viewModel.state.flagLayoutVisibility = true
        viewModel.parentViewModel?.state?.toolbarVisibility?.set(false)
            viewModel.state.successHeader =
                getString(Strings.screen_international_funds_transfer_display_text_success_title)
            viewModel.state.transferAmountHeading =
                getString(Strings.screen_international_funds_transfer_display_text_amount_heading)
            viewModel.state.buttonTitle =
                getString(Strings.screen_international_funds_transfer_back_to_dashboard)

    }

    override fun onClick(viewId: Int, data: Any) {
        if (data is String)
            when (viewId) {
                R.id.tvChooseEmail -> inviteViaEmail(data)
                R.id.tvChooseSMS -> inviteViaSms(data)
                R.id.tvChooseWhatsapp -> inviteViaWhatsapp(data)
            }

    }

    private fun inviteViaWhatsapp(referenceNumber: String) {
        val url =
            "https://wa.me/?text=${referenceNumber})}"
        val i = Intent(Intent.ACTION_SEND, Uri.fromParts("", "", null))
        i.data = Uri.parse(url)
        if (canHandleIntent(intent = i, activity = activity))
            startActivity(i)
    }

    private fun inviteViaEmail(referenceNumber: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("mailto", "", null))
        intent.putExtra(Intent.EXTRA_TEXT, referenceNumber)
        startActivity(Intent.createChooser(intent, "Send mail..."))
    }

    private fun inviteViaSms(referenceNumber: String) {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:")
        sendIntent.putExtra("sms_body", referenceNumber)
        if (canHandleIntent(intent = sendIntent, activity = activity))
            startActivity(sendIntent)
    }

    private fun canHandleIntent(intent: Intent, activity: Activity?): Boolean {
        val packageManager = activity?.packageManager
        packageManager?.let {
            return if (intent.resolveActivity(packageManager) != null) {
                true
            } else {
                showToast("No app available to handle action")
                false
            }
        }
        return false
    }

    private fun getBindings(): FragmentTransferSuccessBinding {
        return viewDataBinding as FragmentTransferSuccessBinding
    }

}