package co.yap.sendmoney.fundtransfer.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import co.yap.modules.otp.GenericOtpFragment
import co.yap.modules.otp.LogoData
import co.yap.modules.otp.OtpDataModel
import co.yap.modules.webview.WebViewFragment
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.databinding.FragmentInternationalTransactionConfirmationBinding
import co.yap.sendmoney.fundtransfer.interfaces.IInternationalTransactionConfirmation
import co.yap.sendmoney.fundtransfer.viewmodels.InternationalTransactionConfirmationViewModel
import co.yap.translation.Strings
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.Constants.URL_DISCLAIMER_TERMS
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.startFragment
import co.yap.yapcore.helpers.extentions.startFragmentForResult
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import co.yap.yapcore.managers.SessionManager

class InternationalTransactionConfirmationFragment :
    BeneficiaryFundTransferBaseFragment<IInternationalTransactionConfirmation.ViewModel>(),
    IInternationalTransactionConfirmation.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_international_transaction_confirmation

    override val viewModel: InternationalTransactionConfirmationViewModel
        get() = ViewModelProviders.of(this)
            .get(InternationalTransactionConfirmationViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        setData()
        setDisclaimerText()
        viewModel.parentViewModel?.state?.toolbarVisibility?.set(true)
        viewModel.parentViewModel?.state?.rightIcon?.set(false)
        viewModel.parentViewModel?.state?.leftIcon?.set(true)
        viewModel.parentViewModel?.state?.toolbarTitle = viewModel.state.confirmHeading ?: ""
    }

    override fun setData() {
        viewModel.state.confirmHeading =
            getString(Strings.screen_cash_pickup_funds_display_otp_header)
        if (viewModel.parentViewModel?.isSameCurrency == true) {
            viewModel.state.transferDescription = resources.getText(
                getString(Strings.screen_funds_confirmation_success_description_same_currency),
                viewModel.parentViewModel?.beneficiary?.value?.firstName,
                requireContext().color(
                    R.color.colorPrimaryDark,
                    viewModel.parentViewModel?.transferData?.value?.destinationAmount?.toFormattedCurrency()
                        ?: ""
                )
            )
        } else {
            viewModel.state.transferDescription = resources.getText(
                getString(Strings.screen_funds_confirmation_success_description),
                requireContext().color(
                    R.color.colorPrimaryDark,
                    viewModel.parentViewModel?.transferData?.value?.destinationCurrency ?: ""
                ), requireContext().color(
                    R.color.colorPrimaryDark,
                    viewModel.parentViewModel?.transferData?.value?.destinationAmount?.toFormattedCurrency(
                        false,
                        viewModel.parentViewModel?.transferData?.value?.destinationCurrency ?: SessionManager.getDefaultCurrency()
                    )
                        ?: ""
                ),
                viewModel.parentViewModel?.beneficiary?.value?.firstName,
                requireContext().color(
                    R.color.colorPrimaryDark,
                    "${viewModel.parentViewModel?.transferData?.value?.toFxRate} to ${viewModel.parentViewModel?.transferData?.value?.fromFxRate}"
                )
            )


            viewModel.state.receivingAmountDescription =
                resources.getText(
                    getString(Strings.screen_funds_receive_description),
                    requireContext().color(
                        R.color.colorPrimaryDark,
                        "${viewModel.parentViewModel?.transferData?.value?.sourceCurrency} ${viewModel.parentViewModel?.transferData?.value?.sourceAmount}"
                    )
                )
        }

        viewModel.state.transferFeeDescription =
            resources.getText(
                getString(Strings.screen_funds_transfer_fee_description), requireContext().color(
                    R.color.colorPrimaryDark,
                    viewModel.parentViewModel?.transferData?.value?.transferFee?.toFormattedCurrency(
                        showCurrency = true,
                        currency = SessionManager.getDefaultCurrency()
                    ) ?: ""
                )
            )
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickEvent)
        viewModel.isOtpRequired.observe(this, Observer {
            if (it)
                startOtpFragment()
        })
    }

    private fun startOtpFragment() {
        startFragmentForResult<GenericOtpFragment>(
            GenericOtpFragment::class.java.name,
            bundleOf(
                OtpDataModel::class.java.name to OtpDataModel(
                    otpAction = viewModel.parentViewModel?.transferData?.value?.otpAction,
                    mobileNumber = SessionManager.user?.currentCustomer?.getFormattedPhoneNumber(
                        requireContext()
                    ),
                    amount = viewModel.parentViewModel?.transferData?.value?.sourceAmount,
                    username = viewModel.parentViewModel?.beneficiary?.value?.fullName(),
                    emailOtp = false,
                    logoData = LogoData(
                        imageUrl = viewModel.parentViewModel?.beneficiary?.value?.beneficiaryPictureUrl,
                        position = viewModel.parentViewModel?.transferData?.value?.position,
                        flagVisibility = true,
                        beneficiaryCountry = viewModel.parentViewModel?.beneficiary?.value?.country
                    )
                )
            ),
            showToolBar = true,
            toolBarTitle = getString(Strings.screen_cash_pickup_funds_display_otp_header)
        ) { resultCode, _ ->
            if (resultCode == Activity.RESULT_OK) {
                viewModel.proceedToTransferAmount()
            }
        }
    }

    val clickEvent = Observer<Int> {
        when (it) {
            R.id.confirmButton -> {
                if (SessionManager.user?.otpBlocked == true) {
                    showToast(Utils.getOtpBlockedMessage(requireContext()))
                } else {
                    viewModel.requestForTransfer()
                }
            }

            Constants.ADD_SUCCESS -> {
                // Send Broadcast for updating transactions list in `Home Fragment`
                val intent = Intent(Constants.BROADCAST_UPDATE_TRANSACTION)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                viewModel.parentViewModel?.transferData?.value?.cutOffTimeMsg =
                    viewModel.state.cutOffTimeMsg
                val action =
                    InternationalTransactionConfirmationFragmentDirections.actionInternationalTransactionConfirmationFragmentToTransferSuccessFragment2()
                findNavController().navigate(action)

            }
        }
    }

    private fun setDisclaimerText() {
        val myClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startFragment(
                    fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                        Constants.PAGE_URL to URL_DISCLAIMER_TERMS
                    ), showToolBar = false
                )
            }
        }
        val newValue =
            getString(Strings.scren_send_money_funds_transfer_confirmation_display_text_disclaimer).plus(
                " "
            )
        val clickValue =
            getString(Strings.scren_send_money_funds_transfer_confirmation_display_text_disclaimer_terms)
        val spanStr = SpannableStringBuilder("$newValue $clickValue")
        spanStr.setSpan(
            myClickableSpan,
            (newValue.length + 1),
            (newValue.length + 1) + clickValue.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spanStr.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorPrimary)),
            (newValue.length + 1),
            (newValue.length + 1) + clickValue.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        getBinding().tvDisclaimer.text = spanStr
        getBinding().tvDisclaimer.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onResume() {
        setObservers()
        super.onResume()
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()
    }

    fun getBinding(): FragmentInternationalTransactionConfirmationBinding {
        return viewDataBinding as FragmentInternationalTransactionConfirmationBinding
    }

}
