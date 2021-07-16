package co.yap.modules.dashboard.yapit.y2y.transfer.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.yapit.y2y.main.viewmodels.Y2YBaseViewModel
import co.yap.modules.dashboard.yapit.y2y.transfer.interfaces.IY2YFundsTransfer
import co.yap.modules.dashboard.yapit.y2y.transfer.states.Y2YFundsTransferState
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.SMCoolingPeriodRequest
import co.yap.networking.customers.responsedtos.sendmoney.SMCoolingPeriod
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.Y2YFundsTransferRequest
import co.yap.networking.transactions.responsedtos.TransactionThresholdModel
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.FeeType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager

class Y2YFundsTransferViewModel(application: Application) :
    Y2YBaseViewModel<IY2YFundsTransfer.State>(application),
    IY2YFundsTransfer.ViewModel, IRepositoryHolder<CustomersRepository> {
    override val state: Y2YFundsTransferState = Y2YFundsTransferState(application)
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val transactionThreshold: MutableLiveData<TransactionThresholdModel> =
        MutableLiveData()
    private val transactionsRepository: TransactionsRepository = TransactionsRepository
    override val repository: CustomersRepository = CustomersRepository
    override var receiverUUID: String = ""
    override var smCoolingPeriod: SMCoolingPeriod? = null

    override fun onCreate() {
        super.onCreate()
        state.availableBalanceGuide =
            getString(Strings.screen_add_funds_display_text_available_balance)
        state.currencyType = SessionManager.getDefaultCurrency()
        getTransactionThresholds()
        getTransactionLimits()
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun proceedToTransferAmount(success: () -> Unit) {
        val y2yFundsTransfer = Y2YFundsTransferRequest(
            receiverUUID = receiverUUID,
            beneficiaryName = state.fullName,
            amount = state.amount,
            otpVerificationReq = false,
            transactionNote = if (state.noteValue.isBlank()) null else state.noteValue.trim(),
            remarks = if (state.noteValue.isBlank()) null else state.noteValue.trim()
        )
        launch {
            state.loading = true
            when (val response = transactionsRepository.y2yFundsTransferRequest(y2yFundsTransfer)) {
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
                transactionsRepository.checkCoolingPeriodRequest(
                    beneficiaryId,
                    beneficiaryCreationDate,
                    beneficiaryName,
                    amount
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

    override fun getTransactionThresholds() {
        launch {
            state.loading = true
            when (val response = transactionsRepository.getTransactionThresholds()) {
                is RetroApiResponse.Success -> {
                    transactionThreshold.value = response.data.data
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun getTransactionLimits() {
        launch {
            when (val response =
                transactionsRepository.getFundTransferLimits(TransactionProductCode.Y2Y_TRANSFER.pCode)) {
                is RetroApiResponse.Success -> {
                    state.maxLimit = response.data.data?.maxLimit?.toDouble() ?: 0.00
                    state.minLimit = response.data.data?.minLimit?.toDouble() ?: 0.00
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                }
            }
        }
    }

    fun getTotalAmountWithFee(): Double {
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
    }

    override fun isInCoolingPeriod(): Boolean {
        smCoolingPeriod?.let { period ->
            val coolingPeriodDurationInSeconds =
                period.coolingPeriodDuration.parseToDouble().times(3600).toLong()
            return period.difference ?: 0 < coolingPeriodDurationInSeconds
        } ?: return false
    }

    override fun isCPAmountConsumed(inputAmount: String): Boolean {
        smCoolingPeriod?.let { period ->
            val remainingLimit = period.maxAllowedCoolingPeriodAmount.parseToDouble()
                .minus(period.consumedAmount ?: 0.0)
            return inputAmount.parseToDouble() > remainingLimit
        } ?: return false
    }

    override fun getCoolingPeriod(smCoolingPeriodRequest: SMCoolingPeriodRequest) {
        launch {
            when (val response = repository.getCoolingPeriod(smCoolingPeriodRequest)) {
                is RetroApiResponse.Success -> {
                    smCoolingPeriod = response.data.data
                }
                is RetroApiResponse.Error -> {
                    state.toast = "${response.error.message}^${AlertType.DIALOG_WITH_FINISH.name}"
                }
            }
        }
    }

    override fun showCoolingPeriodLimitError(): String? {
        if (smCoolingPeriod?.consumedAmount ?: 0.0 >= smCoolingPeriod?.maxAllowedCoolingPeriodAmount.parseToDouble()) {
            return Translator.getString(
                context,
                Strings.common_display_text_cooling_period_limit_consumed_error,
                smCoolingPeriod?.coolingPeriodDuration.toString() + getCoolingHoursLabel(),
                state.fullName
            )
        } else {
            return Translator.getString(
                context,
                Strings.common_display_text_cooling_period_limit_error,
                smCoolingPeriod?.maxAllowedCoolingPeriodAmount.parseToDouble()
                    .minus(smCoolingPeriod?.consumedAmount ?: 0.0)
                    .toString().toFormattedCurrency(),
                smCoolingPeriod?.coolingPeriodDuration.toString() + getCoolingHoursLabel(),
                state.fullName
            )
        }
    }

    private fun getCoolingHoursLabel(): String {
        return smCoolingPeriod?.coolingPeriodDuration?.parseToDouble()?.let { coolingHours ->
            return@let if (coolingHours > 1) " hours" else " hour"
        } ?: " hour"
    }

    override fun onResume() {
        super.onResume()
        toggleToolBarVisibility(true)
        setToolBarTitle(getString(Strings.screen_y2y_funds_transfer_display_text_title))
        setRightButtonVisibility(false)
        setLeftButtonVisibility(true)
    }
}