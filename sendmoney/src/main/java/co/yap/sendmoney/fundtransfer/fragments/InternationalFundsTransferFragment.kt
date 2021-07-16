package co.yap.sendmoney.fundtransfer.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.networking.transactions.requestdtos.RemittanceFeeRequest
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.networking.transactions.responsedtos.transaction.FxRateResponse
import co.yap.sendmoney.BR
import co.yap.sendmoney.PopListBottomSheet
import co.yap.sendmoney.R
import co.yap.sendmoney.databinding.FragmentInternationalFundsTransferBinding
import co.yap.sendmoney.fundtransfer.interfaces.IInternationalFundsTransfer
import co.yap.sendmoney.fundtransfer.viewmodels.InternationalFundsTransferViewModel
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.cancelAllSnackBar
import co.yap.yapcore.helpers.extentions.afterTextChanged
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.roundVal
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.showAlertCustomDialog
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_international_funds_transfer.*


class InternationalFundsTransferFragment :
    BeneficiaryFundTransferBaseFragment<IInternationalFundsTransfer.ViewModel>(),
    IInternationalFundsTransfer.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_international_funds_transfer
    override val viewModel: InternationalFundsTransferViewModel
        get() = ViewModelProviders.of(this).get(InternationalFundsTransferViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productCode = getProductCode()
        viewModel.getMoneyTransferLimits(productCode)
        viewModel.getTransferFees(
            productCode,
            RemittanceFeeRequest(
                country = viewModel.parentViewModel?.beneficiary?.value?.country,
                currency = viewModel.parentViewModel?.beneficiary?.value?.currency
            )
        )
        viewModel.getReasonList(productCode)
        viewModel.getTransactionInternationalfxList(productCode)
        viewModel.getTransactionThresholds()
        viewModel.getCutOffTimeConfiguration()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.parentViewModel?.selectedPop != null) {
            getBindings().tvSelectReason.text =
                viewModel.parentViewModel?.selectedPop?.purposeDescription
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setEditTextWatcher()
    }

    private fun setObservers() {
        viewModel.clickEvent.observe(this, clickEvent)
        viewModel.purposeOfPaymentList.observe(this, Observer {
            it?.let {
                viewModel.processPurposeList(it)
            }
        })
        viewModel.updatedFee.observe(this, Observer {
            if (!it.isNullOrBlank())
                setSpannableFee(it)
        })
        viewModel.isFeeReceived.observe(this, Observer {
            if (it) viewModel.updateFees()
        })
        viewModel.fxRateResponse.observe(this, Observer {
            handleFxRateResponse(it)
        })
        viewModel.isAPIFailed.observe(this, Observer {
            if (it) {
                requireActivity().finish()
            }
        })
        viewModel.transactionMightGetHeld.observe(this, Observer {
            if (it) {
                //moveToConfirmTransferScreen()
            }
        })
    }

    private fun handleFxRateResponse(it: FxRateResponse.Data?) {
        it?.let { fxRate ->
            viewModel.state.fromFxRate =
                "${fxRate.fromCurrencyCode} ${fxRate.fxRates?.get(0)?.rate}"
            viewModel.state.toFxRate =
                fxRate.value?.amount?.toFormattedCurrency(
                    showCurrency = true,
                    currency = fxRate.toCurrencyCode ?: SessionManager.getDefaultCurrency()
                )
            viewModel.state.sourceCurrency.set(fxRate.fromCurrencyCode)
            viewModel.state.destinationCurrency.set(fxRate.toCurrencyCode)
            viewModel.parentViewModel?.transferData?.value?.rate = fxRate.fxRates?.get(0)?.rate
        }
    }

    private fun setSpannableFee(feeAmount: String?) {
        viewModel.parentViewModel?.transferData?.value?.transferFee = feeAmount
        viewModel.state.transferFeeSpannable = resources.getText(
            getString(Strings.screen_international_funds_transfer_display_text_fee),
            requireContext().color(R.color.colorPrimaryDark, SessionManager.getDefaultCurrency()),
            requireContext().color(
                R.color.colorPrimaryDark,
                if (feeAmount.isNullOrBlank()) "0".toFormattedCurrency(
                    showCurrency = false
                ) else feeAmount.toFormattedCurrency(
                    showCurrency = false
                )
            )
        )
    }

    val clickEvent = Observer<Int> {
        when (it) {
            R.id.btnNext -> {
                if (SessionManager.user?.otpBlocked == true) {
                    showToast(Utils.getOtpBlockedMessage(requireContext()))
                } else {
                    when {
                        viewModel.state.etOutputAmount.parseToDouble() < viewModel.state.minLimit ?: 0.0 -> {
                            showLowerAndUpperLimitError()
                        }
                        viewModel.parentViewModel?.isInCoolingPeriod() == true && viewModel.parentViewModel?.isCPAmountConsumed(
                            viewModel.state.etOutputAmount ?: "0.0"
                        ) == true -> {
                            viewModel.checkCoolingPeriodRequest(
                                beneficiaryId = viewModel.parentViewModel?.beneficiary?.value?.id.toString(),
                                beneficiaryCreationDate = viewModel.parentViewModel?.beneficiary?.value?.beneficiaryCreationDate,
                                beneficiaryName = viewModel.parentViewModel?.beneficiary?.value?.fullName(),
                                amount = viewModel.state.etOutputAmount
                            ) {
                                activity?.showAlertCustomDialog(
                                    title = "Psst...",
                                    message = viewModel.parentViewModel?.showCoolingPeriodLimitError(),
                                    buttonText = "OK, got it!"
                                )
                            }
                        }
                        viewModel.parentViewModel?.selectedPop != null -> moveToConfirmTransferScreen()
                        else -> showToast("Select a reason ^${AlertType.DIALOG.name}")
                    }
                }
            }
            R.id.tvSelectReason, R.id.ivSelector -> setupPOP(viewModel.purposeCategories)
        }
    }

    private fun setupPOP(purposeCategories: Map<String?, List<PurposeOfPayment>>?) {
        etNote.clearFocus()
        var inviteFriendBottomSheet: BottomSheetDialogFragment? = null
        this.fragmentManager?.let {
            inviteFriendBottomSheet = PopListBottomSheet(object :
                OnItemClickListener {
                override fun onItemClick(view: View, data: Any, pos: Int) {
                    inviteFriendBottomSheet?.dismiss()
                    viewModel.parentViewModel?.selectedPop = data as PurposeOfPayment
                    viewModel.updateFees()
                    getBindings().tvSelectReason.text =
                        viewModel.parentViewModel?.selectedPop?.purposeDescription
                    getBindings().tvSelectReason.alpha = 1.0f
                    getBindings().tvReasonLbl.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyDark
                        )
                    )
                }

            }, purposeCategories)
            inviteFriendBottomSheet?.show(it, "")
        }
    }

    private fun isBalanceAvailable(): Boolean {
        val availableBalance =
            SessionManager.cardBalance.value?.availableBalance?.toDoubleOrNull()
        return if (availableBalance != null) {
            (availableBalance >= viewModel.getTotalAmountWithFee())
        } else
            false
    }

    private fun showLowerAndUpperLimitError() {
        viewModel.state.errorDescription = getString(
            if (viewModel.parentViewModel?.isSameCurrency == true) Strings.common_display_text_min_max_limit_error_transaction else Strings.sm_display_text_min_max_limit_error_transaction
        ).format(
            viewModel.state.minLimit.toString().toFormattedCurrency(),
            viewModel.state.maxLimit.toString().toFormattedCurrency()
        )
        viewModel.parentViewModel?.errorEvent?.value = viewModel.state.errorDescription
    }

    private fun showBalanceNotAvailableError() {
        val des = Translator.getString(
            requireContext(),
            Strings.common_display_text_available_balance_error
        ).format(viewModel.state.etOutputAmount?.toFormattedCurrency())
        viewModel.parentViewModel?.errorEvent?.value = des
    }

    private fun isDailyLimitReached(): Boolean {
        viewModel.parentViewModel?.transactionThreshold?.value?.let {
            it.dailyLimit?.let { dailyLimit ->
                it.totalDebitAmount?.let { totalConsumedAmount ->
                    viewModel.state.etOutputAmount.parseToDouble().let { enteredAmount ->
                        if (viewModel.transactionMightGetHeld.value == true && it.holdAmountIsIncludedInTotalDebitAmount == false) {
                            val totalHoldAmount =
                                (it.holdSwiftAmount ?: 0.0).plus(it.holdUAEFTSAmount ?: 0.0)
                            val remainingDailyLimit =
                                if ((dailyLimit - totalHoldAmount) < 0.0) 0.0 else (dailyLimit - totalHoldAmount)
                            viewModel.state.errorDescription =
                                when (dailyLimit) {
                                    totalHoldAmount -> getString(Strings.common_display_text_daily_limit_error)
                                    else -> Translator.getString(
                                        requireContext(),
                                        Strings.common_display_text_on_hold_limit_error
                                    ).format(
                                        remainingDailyLimit.roundVal().toString()
                                            .toFormattedCurrency()
                                    )
                                }

                            return (enteredAmount > remainingDailyLimit.roundVal())

                        } else {
                            val remainingDailyLimit =
                                if ((dailyLimit - totalConsumedAmount) < 0.0) 0.0 else (dailyLimit - totalConsumedAmount)
                            viewModel.state.errorDescription =
                                when {
                                    dailyLimit == totalConsumedAmount -> getString(Strings.common_display_text_daily_limit_error)
                                    enteredAmount > dailyLimit && totalConsumedAmount == 0.0 -> {
                                        getString(Strings.common_display_text_daily_limit_error_single_transaction)
                                    }
                                    else -> getString(Strings.common_display_text_daily_limit_error_multiple_transactions)
                                }
                            return (enteredAmount > remainingDailyLimit.roundVal())
                        }
                    }
                } ?: return false
            } ?: return false
        } ?: return false

    }

    private fun moveToConfirmTransferScreen() {
        viewModel.parentViewModel?.transferData?.value?.feeAmount = viewModel.feeAmount
        viewModel.parentViewModel?.transferData?.value?.vat = viewModel.vat
        viewModel.parentViewModel?.transferData?.value?.sourceCurrency =
            viewModel.state.sourceCurrency.get().toString()
        viewModel.parentViewModel?.transferData?.value?.sourceAmount =
            viewModel.state.etOutputAmount.toString()
        viewModel.parentViewModel?.transferData?.value?.destinationCurrency =
            viewModel.state.destinationCurrency.get().toString()
        viewModel.parentViewModel?.transferData?.value?.destinationAmount =
            viewModel.state.etInputAmount.toString()
        viewModel.parentViewModel?.transferData?.value?.toFxRate = viewModel.state.toFxRate
        viewModel.parentViewModel?.transferData?.value?.fromFxRate = viewModel.state.fromFxRate
        viewModel.parentViewModel?.transferData?.value?.noteValue =
            viewModel.state.transactionNote.get()
        viewModel.parentViewModel?.transferData?.value?.transferAmount =
            viewModel.state.etInputAmount
        val action =
            InternationalFundsTransferFragmentDirections.actionInternationalFundsTransferFragmentToInternationalTransactionConfirmationFragment()
        findNavController().navigate(action)
    }

    private fun getProductCode(): String {
        viewModel.parentViewModel?.beneficiary?.value?.let { beneficiary ->
            return when (beneficiary.beneficiaryType) {
                SendMoneyBeneficiaryType.RMT.type -> {
                    viewModel.parentViewModel?.transferData?.value?.otpAction =
                        SendMoneyBeneficiaryType.RMT.name
                    TransactionProductCode.RMT.pCode
                }
                SendMoneyBeneficiaryType.SWIFT.type -> {
                    viewModel.parentViewModel?.transferData?.value?.otpAction =
                        SendMoneyBeneficiaryType.SWIFT.name
                    TransactionProductCode.SWIFT.pCode
                }
                else -> ""
            }
        } ?: return ""
    }

    private fun setEditTextWatcher() {
        etSenderAmount.afterTextChanged {
            viewModel.state.etInputAmount = it
            viewModel.state.clearError()
            viewModel.setDestinationAmount()
            if (it.isNotBlank() && viewModel.state.etInputAmount.parseToDouble() > 0.0)
                checkOnTextChangeValidation()
            else {
                viewModel.state.valid = false
                cancelAllSnackBar()
            }
            viewModel.updateFees()
        }
    }


    private fun checkOnTextChangeValidation() {
        when {
            !isBalanceAvailable() -> {
                viewModel.state.valid = false
                showBalanceNotAvailableError()
            }
            isDailyLimitReached() -> {
                viewModel.parentViewModel?.errorEvent?.value = viewModel.state.errorDescription
                viewModel.state.valid = false
            }

            viewModel.state.etOutputAmount.parseToDouble() < viewModel.state.minLimit ?: 0.0 -> {
                viewModel.state.valid = true
            }
            viewModel.state.etOutputAmount.parseToDouble() > viewModel.state.maxLimit ?: 0.0 -> {
                showLowerAndUpperLimitError()
                viewModel.state.valid = false
            }
            else -> {
                cancelAllSnackBar()
                viewModel.state.valid = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setObservers()
    }

    override fun onPause() {
        super.onPause()
        viewModel.clickEvent.removeObservers(this)
        viewModel.isFeeReceived.removeObservers(this)
        viewModel.updatedFee.removeObservers(this)
        viewModel.fxRateResponse.removeObservers(this)
        viewModel.isAPIFailed.removeObservers(this)
        viewModel.purposeOfPaymentList.removeObservers(this)

    }

    fun getBindings(): FragmentInternationalFundsTransferBinding {
        return viewDataBinding as FragmentInternationalFundsTransferBinding
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelAllSnackBar()
    }
}