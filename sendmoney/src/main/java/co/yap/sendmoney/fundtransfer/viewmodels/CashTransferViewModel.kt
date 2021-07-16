package co.yap.sendmoney.fundtransfer.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.SendMoneyTransferRequest
import co.yap.networking.transactions.responsedtos.InternationalFundsTransferReasonList
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.sendmoney.R
import co.yap.sendmoney.fundtransfer.interfaces.ICashTransfer
import co.yap.sendmoney.fundtransfer.states.CashTransferState
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.FeeType
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import co.yap.yapcore.managers.SessionManager

class CashTransferViewModel(application: Application) :
    BeneficiaryFundTransferBaseViewModel<ICashTransfer.State>(application),
    ICashTransfer.ViewModel {

    private val transactionRepository: TransactionsRepository = TransactionsRepository
    private val customersRepository: CustomersRepository = CustomersRepository
    override val state: CashTransferState =
        CashTransferState(application)
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val errorEvent: SingleClickEvent = SingleClickEvent()
    override var transactionData: MutableLiveData<ArrayList<InternationalFundsTransferReasonList.ReasonList>> =
        MutableLiveData()
    override var receiverUUID: String = ""
    override var purposeOfPaymentList: MutableLiveData<ArrayList<PurposeOfPayment>> =
        MutableLiveData()
    val transactionMightGetHeld: MutableLiveData<Boolean> = MutableLiveData(false)

    var purposeCategories: Map<String?, List<PurposeOfPayment>>? = HashMap()
    override var reasonPosition: Int = 0

    override fun onCreate() {
        super.onCreate()
        getTransactionThresholds()
        state.availableBalanceString = context.resources.getText(
            getString(Strings.screen_cash_transfer_display_text_available_balance),
            context.color(
                R.color.colorPrimaryDark,
                SessionManager.cardBalance.value?.availableBalance?.toFormattedCurrency(showCurrency = true) ?: ""
            )
        )
    }

    override fun onResume() {
        super.onResume()
        toggleToolBarVisibility(true)
        setToolBarTitle(getString(Strings.screen_y2y_funds_transfer_display_text_title))
        setToolbarData()
    }

    private fun setToolbarData() {
        parentViewModel?.state?.leftIcon?.set(false)
        parentViewModel?.state?.rightIcon?.set(true)
        parentViewModel?.beneficiary?.value?.let { beneficiary ->
            if (beneficiary.beneficiaryType == SendMoneyBeneficiaryType.CASHPAYOUT.type) {
                parentViewModel?.state?.toolbarTitle =
                    getString(Strings.screen_cash_pickup_funds_display_text_header)
            } else {
                parentViewModel?.state?.toolbarTitle =
                    getString(Strings.screen_funds_local_toolbar_header)
            }
        }
    }

    fun updateFees() {
        updateFees(state.amount)
    }

    override fun handlePressOnView(id: Int) {
        if (R.id.btnConfirm == id) {
            if (!isUaeftsBeneficiary()) {
                when {
                    state.amount.parseToDouble() < state.minLimit -> showUpperLowerLimitError()
                    isOtpRequired() -> createOtp(id = id)
                    else -> proceedToTransferAmount()
                }
            } else {
                clickEvent.setValue(id)
            }
        } else {
            clickEvent.setValue(id)
        }
    }

    private fun isOtpRequired(): Boolean {
        parentViewModel?.transactionThreshold?.value?.let {
            it.totalDebitAmountRemittance?.let { totalSMConsumedAmount ->
                state.amount.toDoubleOrNull()?.let { enteredAmount ->
                    return if (trxWillHold() && transactionMightGetHeld.value == true) {
                        val totalHoldAmount =
                            (it.holdSwiftAmount ?: 0.0).plus(it.holdUAEFTSAmount ?: 0.0)
                        val remainingOtpLimit = it.otpLimit?.minus(totalHoldAmount)
                        enteredAmount > (remainingOtpLimit ?: 0.0)
                    } else {
                        val remainingOtpLimit = it.otpLimit?.minus(totalSMConsumedAmount)
                        enteredAmount > (remainingOtpLimit ?: 0.0)
                    }
                } ?: return false
            } ?: return false
        } ?: return false
    }

    override fun proceedToTransferAmount() {
        parentViewModel?.beneficiary?.value?.let { beneficiary ->
            beneficiary.beneficiaryType?.let {
                if (beneficiary.beneficiaryType?.isNotEmpty() == true)
                    when (SendMoneyBeneficiaryType.valueOf(beneficiary.beneficiaryType ?: "")) {
                        SendMoneyBeneficiaryType.CASHPAYOUT -> {
                            beneficiary.id?.let { beneficiaryId ->
                                cashPayoutTransferRequest(beneficiaryId)
                            }
                        }
                        else -> {
                        }
                    }
            }

        }
    }

    private fun createOtp(id: Int = 0) {
        clickEvent.postValue(id) // TODO:update this clickEvent with live data it creates debounce
    }

    override fun getPurposeOfPayment(productCode: String) {
        launch {
            state.loading = true
            when (val response =
                transactionRepository.getPurposeOfPayment(productCode)) {
                is RetroApiResponse.Success -> {
                    if (!response.data.data.isNullOrEmpty()) {
                        purposeOfPaymentList.value =
                            response.data.data as? ArrayList<PurposeOfPayment>?
                    }
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG_WITH_FINISH.name}"
                }
            }
        }
    }

    override fun cashPayoutTransferRequest(beneficiaryId: Int?) {
        launch {
            state.loading = true
            when (val response =
                transactionRepository.cashPayoutTransferRequest(
                    SendMoneyTransferRequest(
                        beneficiaryId = beneficiaryId,
                        amount = state.amount,
                        currency = SessionManager.getDefaultCurrency(),
                        purposeCode = "8",
                        remarks = state.noteValue?.trim()
                    )
                )
                ) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.transferData?.value?.referenceNumber = response.data.data
                    clickEvent.postValue(Constants.ADD_CASH_PICK_UP_SUCCESS)
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    showToast(response.error.message)
                }
            }
        }
    }

    override fun getMoneyTransferLimits(productCode: String?) {
        launch {
            when (val response = transactionRepository.getFundTransferLimits(productCode)) {
                is RetroApiResponse.Success -> {
                    state.maxLimit = response.data.data?.maxLimit?.toDouble() ?: 0.00
                    state.minLimit = response.data.data?.minLimit?.toDouble() ?: 0.00
                    getCountryLimit()
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG_WITH_FINISH.name}"
                }
            }
        }
    }

    override fun getCountryLimit() {
        launch {
            when (val response = customersRepository.getCountryTransactionLimits(
                parentViewModel?.beneficiary?.value?.country ?: "",
                parentViewModel?.beneficiary?.value?.currency ?: ""
            )) {
                is RetroApiResponse.Success -> {
                    setMaxMinLimits(response.data.data?.toDoubleOrNull())
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG_WITH_FINISH.name}"
                }
            }
        }
    }

    override fun getTransactionThresholds() {
        launch {
            when (val response = transactionRepository.getTransactionThresholds()) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.transactionThreshold?.value = response.data.data
                    if (parentViewModel?.beneficiary?.value?.beneficiaryType == SendMoneyBeneficiaryType.UAEFTS.type) {
                        getCutOffTimeConfiguration()
                    }
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG_WITH_FINISH.name}"
                }
            }
        }
    }

    private fun setMaxMinLimits(limit: Double?) {
        limit?.let {
            if (it > 0.0) {
                state.maxLimit = it
                if (it < state.minLimit) {
                    state.minLimit = 1.0
                }
            }
        }
    }

    fun getTotalAmountWithFee(): Double {
        return if (shouldFeeApply()) {
            return (when (feeType) {
                FeeType.TIER.name -> {
                    val transferFee = getFeeFromTier(state.amount)
                    state.amount.toDoubleOrNull() ?: 0.0.plus(
                        transferFee?.toDoubleOrNull() ?: 0.0
                    )
                }
                FeeType.FLAT.name -> {
                    state.amount.parseToDouble().plus(getFlatFee(state.amount).parseToDouble())
                }
                else -> {
                    state.amount.parseToDouble()
                }
            })
        } else {
            state.amount.parseToDouble()
        }
    }

    override fun processPurposeList(list: ArrayList<PurposeOfPayment>) {
        purposeCategories = list.groupBy { item ->
            item.purposeCategory
        }
    }

    fun isUaeftsBeneficiary(): Boolean {
        parentViewModel?.beneficiary?.value?.beneficiaryType?.let {
            return (it == SendMoneyBeneficiaryType.UAEFTS.type || it == SendMoneyBeneficiaryType.DOMESTIC.type)
        } ?: return false
    }

    private fun isOnlyUAEFTS(): Boolean {
        parentViewModel?.beneficiary?.value?.beneficiaryType?.let {
            return (it == SendMoneyBeneficiaryType.UAEFTS.type)
        } ?: return false
    }

    fun shouldFeeApply(): Boolean {
        return if (!isOnlyUAEFTS()) return true else
            parentViewModel?.selectedPop?.let { pop ->
                return@let if (pop.nonChargeable == false) {
                    return (when {
                        parentViewModel?.beneficiary?.value?.cbwsicompliant == true &&
                                pop.cbwsi == true &&
                                pop.cbwsiFee == false -> state.amount.parseToDouble() > parentViewModel?.transactionThreshold?.value?.cbwsiPaymentLimit ?: 0.0
                        else -> true
                    })
                } else
                    false
            } ?: true
    }

    fun trxWillHold(): Boolean {
        // todo: cuttoff time , uaefts , AED , cbwsi , bank cbwsi complaintent ,less than equal to cbwsi limit
        return if (!isOnlyUAEFTS()) return false else
            parentViewModel?.selectedPop?.let { pop ->
                return (when {
                    parentViewModel?.beneficiary?.value?.cbwsicompliant == true &&
                            pop.cbwsi == true -> state.amount.parseToDouble() > parentViewModel?.transactionThreshold?.value?.cbwsiPaymentLimit ?: 0.0
                    else -> true
                })

            } ?: state.amount.parseToDouble() > parentViewModel?.transactionThreshold?.value?.cbwsiPaymentLimit ?: 0.0
    }

    override fun getCutOffTimeConfiguration() {
        launch {
            when (val response =
                transactionRepository.getCutOffTimeConfiguration(
                    productCode = getProductCode(),
                    currency = SessionManager.getDefaultCurrency(),
                    amount = parentViewModel?.transactionThreshold?.value?.cbwsiPaymentLimit?.plus(1)
                        .toString(),
                    isCbwsi = if (parentViewModel?.beneficiary?.value?.cbwsicompliant == true) parentViewModel?.selectedPop?.cbwsi
                        ?: false else parentViewModel?.beneficiary?.value?.cbwsicompliant
                )) {
                is RetroApiResponse.Success -> {
                    response.data.data?.errorMsg?.let {
                        transactionMightGetHeld.value = true
                        parentViewModel?.isCutOffTimeStarted = true
                        parentViewModel?.transferData?.value?.cutOffTimeMsg = it
                    }
                }
                is RetroApiResponse.Error -> {
                    transactionMightGetHeld.value = false
                }
            }
        }

    }

    private fun getProductCode(): String {
        return (when (parentViewModel?.beneficiary?.value?.beneficiaryType) {
            SendMoneyBeneficiaryType.UAEFTS.type -> TransactionProductCode.UAEFTS.pCode
            SendMoneyBeneficiaryType.DOMESTIC.type -> TransactionProductCode.DOMESTIC.pCode
            else -> ""
        })
    }

    fun showUpperLowerLimitError() {
        state.errorDescription = Translator.getString(
            context,
            Strings.common_display_text_min_max_limit_error_transaction,
            state.minLimit.toString().toFormattedCurrency(),
            state.maxLimit.toString().toFormattedCurrency()
        )
        parentViewModel?.errorEvent?.value = state.errorDescription

    }

    override fun checkCoolingPeriodRequest(
        beneficiaryId: String?,
        beneficiaryCreationDate: String?,
        beneficiaryName: String?,
        amount: String?,
        success: () -> Unit
    ) {
        launch {
            state.loading = true
            when (val response =
                transactionRepository.checkCoolingPeriodRequest(
                    beneficiaryId = beneficiaryId,
                    beneficiaryCreationDate =beneficiaryCreationDate,
                    beneficiaryName =beneficiaryName,
                    amount = state.amount
                )) {
                is RetroApiResponse.Success -> {
                    success.invoke()
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.errorDescription = response.error.message
                    parentViewModel?.errorEvent?.value = state.errorDescription
                }
            }
            state.loading = false
        }
    }
}