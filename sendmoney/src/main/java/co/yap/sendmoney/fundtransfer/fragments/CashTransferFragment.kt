package co.yap.sendmoney.fundtransfer.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.yap.modules.otp.GenericOtpFragment
import co.yap.modules.otp.LogoData
import co.yap.modules.otp.OtpDataModel
import co.yap.networking.transactions.requestdtos.RemittanceFeeRequest
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.sendmoney.PopListBottomSheet
import co.yap.sendmoney.R
import co.yap.sendmoney.databinding.FragmentCashTransferBinding
import co.yap.sendmoney.fundtransfer.interfaces.ICashTransfer
import co.yap.sendmoney.fundtransfer.viewmodels.CashTransferViewModel
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BR
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.cancelAllSnackBar
import co.yap.yapcore.helpers.extentions.*
import co.yap.yapcore.helpers.showAlertCustomDialog
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_international_funds_transfer.*

class CashTransferFragment : BeneficiaryFundTransferBaseFragment<ICashTransfer.ViewModel>(),
    ICashTransfer.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_cash_transfer

    override val viewModel: CashTransferViewModel
        get() = ViewModelProviders.of(this).get(CashTransferViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFlows(getProductCode())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updatedFee.value = "0.0"
        if (viewModel.parentViewModel?.selectedPop != null) {
            getBindings().tvSelectReason.text =
                viewModel.parentViewModel?.selectedPop?.purposeDescription
        }
        setEditTextWatcher()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickEvent)
        viewModel.errorEvent.observe(this, Observer {
            viewModel.parentViewModel?.errorEvent?.value = viewModel.state.errorDescription
        })

        viewModel.isAPIFailed.observe(this, Observer {
            //if (it) requireActivity().finish()
        })

        viewModel.isFeeReceived.observe(this, Observer {
            if (it) viewModel.updateFees()
        })
        viewModel.updatedFee.observe(this, Observer {
            if (!it.isNullOrBlank())
                setSpannableFee(if (viewModel.shouldFeeApply()) it else "0.0")
        })

        viewModel.purposeOfPaymentList.observe(this, Observer {
            it?.let {
                viewModel.processPurposeList(it)
            }
        })
    }


    private fun setSpannableFee(totalFeeAmount: String?) {
        viewModel.parentViewModel?.transferData?.value?.transferFee = totalFeeAmount
        viewModel.state.feeAmountSpannableString = resources.getText(
            getString(Strings.screen_cash_pickup_funds_display_text_fee),
            requireContext().color(R.color.colorPrimaryDark, SessionManager.getDefaultCurrency()),
            requireContext().color(
                R.color.colorPrimaryDark,
                if (totalFeeAmount.isNullOrBlank()) "0.00" else totalFeeAmount.toFormattedCurrency(
                    showCurrency = false,
                    currency = SessionManager.getDefaultCurrency()
                )
            )
        )
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
                    getBindings().tvLabelSpinner.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greyDark
                        )
                    )
                    checkOnTextChangeValidation()
                }

            }, purposeCategories)
            inviteFriendBottomSheet?.show(it, "")
        }
    }

    val clickEvent = Observer<Int> {
        when (it) {
            R.id.btnConfirm -> {
                if (SessionManager.user?.otpBlocked == true) {
                    showToast(Utils.getOtpBlockedMessage(requireContext()))
                } else {
                    when {
                        viewModel.state.amount.parseToDouble() < viewModel.state.minLimit -> {
                            viewModel.showUpperLowerLimitError()
                        }
                        viewModel.parentViewModel?.isInCoolingPeriod() == true && viewModel.parentViewModel?.isCPAmountConsumed(
                            viewModel.state.amount
                        ) == true -> {
                            viewModel.checkCoolingPeriodRequest(
                                beneficiaryId = viewModel.parentViewModel?.beneficiary?.value?.id.toString(),
                                beneficiaryCreationDate = viewModel.parentViewModel?.beneficiary?.value?.beneficiaryCreationDate,
                                beneficiaryName = viewModel.parentViewModel?.beneficiary?.value?.fullName(),
                                amount = viewModel.state.amount
                            ) {
                                requireActivity().showAlertCustomDialog(
                                    title = "Psst...",
                                    message = viewModel.parentViewModel?.showCoolingPeriodLimitError(),
                                    buttonText = "OK, got it!"
                                )
                            }
                        }
                        viewModel.isUaeftsBeneficiary() -> {
                            if (viewModel.parentViewModel?.selectedPop != null) moveToConfirmationScreen() else showToast(
                                "Select a reason ^${AlertType.DIALOG.name}"
                            )
                        }
                        else -> startOtpFragment()
                    }
                }
            }
            R.id.tvSelectReason, R.id.ivSelector -> setupPOP(viewModel.purposeCategories)
            Constants.ADD_CASH_PICK_UP_SUCCESS -> {
                // Send Broadcast for updating transactions list in `Home Fragment`
                val intent = Intent(Constants.BROADCAST_UPDATE_TRANSACTION)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                viewModel.parentViewModel?.transferData?.value?.sourceCurrency = SessionManager.getDefaultCurrency()
                viewModel.parentViewModel?.transferData?.value?.destinationCurrency = SessionManager.getDefaultCurrency()
                viewModel.parentViewModel?.transferData?.value?.transferAmount =
                    viewModel.state.amount
                val action =
                    CashTransferFragmentDirections.actionCashTransferFragmentToTransferSuccessFragment2()
                findNavController().navigate(action)

            }
        }
    }

    private fun startOtpFragment() {
        startFragmentForResult<GenericOtpFragment>(
            GenericOtpFragment::class.java.name,
            bundleOf(
                OtpDataModel::class.java.name to OtpDataModel(
                    viewModel.parentViewModel?.transferData?.value?.otpAction,//action,
                    SessionManager.user?.currentCustomer?.getFormattedPhoneNumber(requireContext())
                        ?: "",
                    username = viewModel.parentViewModel?.beneficiary?.value?.fullName(),
                    amount = viewModel.state.amount,
                    logoData = LogoData(
                        position = viewModel.parentViewModel?.transferData?.value?.position
                    )
                )
            )
        ) { resultCode, _ ->
            if (resultCode == Activity.RESULT_OK) {
                viewModel.proceedToTransferAmount()
            }
        }
    }

    private fun moveToConfirmationScreen() {
        viewModel.parentViewModel?.transferData?.value?.transferAmount = viewModel.state.amount
        viewModel.parentViewModel?.transferData?.value?.noteValue = viewModel.state.noteValue
        viewModel.parentViewModel?.transferData?.value?.sourceCurrency = SessionManager.getDefaultCurrency()
        viewModel.parentViewModel?.transferData?.value?.destinationCurrency = SessionManager.getDefaultCurrency()
        viewModel.parentViewModel?.transferData?.value?.feeAmount =
            if (viewModel.shouldFeeApply()) viewModel.feeAmount else "0.0"
        viewModel.parentViewModel?.transferData?.value?.vat =
            if (viewModel.shouldFeeApply()) viewModel.vat else "0.0"

        val action =
            CashTransferFragmentDirections.actionCashTransferFragmentToCashTransferConfirmationFragment()
        navigate(
            action,
            screenType = if (!viewModel.trxWillHold()) FeatureSet.CBWSI_TRANSFER else FeatureSet.NONE
        )
    }

    private fun showBalanceNotAvailableError() {
        val des = Translator.getString(
            requireContext(),
            Strings.common_display_text_available_balance_error
        ).format(viewModel.state.amount.toFormattedCurrency())
        viewModel.parentViewModel?.errorEvent?.value = des
    }


    private fun isBalanceAvailable(): Boolean {
        val availableBalance =
            SessionManager.cardBalance.value?.availableBalance?.toDoubleOrNull()
        return if (availableBalance != null) {
            (availableBalance > viewModel.getTotalAmountWithFee())
        } else
            false
    }

    private fun isDailyLimitReached(): Boolean {
        viewModel.parentViewModel?.transactionThreshold?.value?.let {
            it.dailyLimit?.let { dailyLimit ->
                it.totalDebitAmount?.let { totalConsumedAmount ->
                    viewModel.state.amount.toDoubleOrNull()?.let { enteredAmount ->
                        if (viewModel.trxWillHold() && viewModel.transactionMightGetHeld.value == true && it.holdAmountIsIncludedInTotalDebitAmount == false) {
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
                    } ?: return false
                } ?: return false
            } ?: return false
        } ?: return false
    }

    private fun startFlows(productCode: String) {
        viewModel.parentViewModel?.beneficiary?.value?.beneficiaryType?.let { beneficiaryType ->
            when (beneficiaryType) {
                SendMoneyBeneficiaryType.RMT.type, SendMoneyBeneficiaryType.SWIFT.type -> skipCashTransferFragment()
                else -> {
                    viewModel.getMoneyTransferLimits(productCode)
                    viewModel.getTransferFees(
                        productCode,
                        RemittanceFeeRequest(country = viewModel.parentViewModel?.beneficiary?.value?.country)
                    )
                    viewModel.getPurposeOfPayment(productCode)
                    setObservers()
                }
            }
        }
    }

    private fun getProductCode(): String {
        viewModel.parentViewModel?.beneficiary?.value?.let { beneficiary ->
            when (beneficiary.beneficiaryType) {
                SendMoneyBeneficiaryType.CASHPAYOUT.type -> {
                    viewModel.parentViewModel?.transferData?.value?.otpAction =
                        SendMoneyBeneficiaryType.CASHPAYOUT.type
                    return TransactionProductCode.CASH_PAYOUT.pCode
                }
                SendMoneyBeneficiaryType.DOMESTIC.type -> {
                    viewModel.parentViewModel?.transferData?.value?.otpAction =
                        SendMoneyBeneficiaryType.DOMESTIC_TRANSFER.type
                    return TransactionProductCode.DOMESTIC.pCode
                }
                SendMoneyBeneficiaryType.UAEFTS.type -> {
                    viewModel.parentViewModel?.transferData?.value?.otpAction =
                        SendMoneyBeneficiaryType.UAEFTS.type
                    return TransactionProductCode.UAEFTS.pCode
                }
                else -> {
                    return ""
                }
            }
        } ?: return ""
    }

    override fun onPause() {
        super.onPause()
        viewModel.isAPIFailed.removeObservers(this)
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        viewModel.isAPIFailed.removeObservers(this)
        viewModel.updatedFee.removeObservers(this)
        viewModel.purposeOfPaymentList.removeObservers(this)
        viewModel.transactionData.removeObservers(this)
        super.onDestroy()
    }

    private fun skipCashTransferFragment() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.cashTransferFragment, true) // starting destination skiped
            .build()

        findNavController().navigate(
            R.id.action_cashTransferFragment_to_internationalFundsTransferFragment,
            null,
            navOptions
        )
    }

    private fun setEditTextWatcher() {
        getBindings().etAmount.afterTextChanged {
            viewModel.state.amount = it
            viewModel.state.clearError()
            if (viewModel.state.amount.isNotEmpty() && viewModel.state.amount.parseToDouble() > 0.0) {
                checkOnTextChangeValidation()
            } else {
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
            viewModel.state.amount.parseToDouble() < viewModel.state.minLimit -> {
                viewModel.state.valid = true
            }
            viewModel.state.amount.parseToDouble() > viewModel.state.maxLimit -> {
                viewModel.showUpperLowerLimitError()
                viewModel.state.valid = false
            }
            else -> {
                cancelAllSnackBar()
                viewModel.state.valid = true
            }
        }
    }

   private fun getBindings(): FragmentCashTransferBinding {
        return viewDataBinding as FragmentCashTransferBinding
    }
}