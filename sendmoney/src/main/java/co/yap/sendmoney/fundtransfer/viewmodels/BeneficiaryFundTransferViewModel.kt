package co.yap.sendmoney.fundtransfer.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.SMCoolingPeriodRequest
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.networking.customers.responsedtos.sendmoney.SMCoolingPeriod
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.responsedtos.TransactionThresholdModel
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.sendmoney.fundtransfer.interfaces.IBeneficiaryFundTransfer
import co.yap.sendmoney.fundtransfer.models.TransferFundData
import co.yap.sendmoney.fundtransfer.states.BeneficiaryFundTransferState
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.toFormattedCurrency


class BeneficiaryFundTransferViewModel(application: Application) :
    BaseViewModel<IBeneficiaryFundTransfer.State>(application = application),
    IBeneficiaryFundTransfer.ViewModel, IRepositoryHolder<CustomersRepository> {
    override val state: BeneficiaryFundTransferState =
        BeneficiaryFundTransferState()
    override val repository: CustomersRepository = CustomersRepository
    override var errorEvent: MutableLiveData<String> = MutableLiveData()
    override var beneficiary: MutableLiveData<Beneficiary> = MutableLiveData()
    override var transferData: MutableLiveData<TransferFundData> = MutableLiveData()
    override var transactionThreshold: MutableLiveData<TransactionThresholdModel> =
        MutableLiveData()
    override var selectedPop: PurposeOfPayment? = null
    override var isCutOffTimeStarted: Boolean = false
    override var isSameCurrency: Boolean = false
    override var transactionWillHold: Boolean = false
    override var smCoolingPeriod: SMCoolingPeriod? = null


    override fun onCreate() {
        super.onCreate()
        state.toolbarVisibility.set(true)
        state.toolbarTitle = getString(Strings.screen_cash_pickup_funds_display_text_header)
        state.leftIcon.set(true)
        state.rightIcon.set(true)
        state.rightButtonText = getString(Strings.common_button_cancel)
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
        var errorDescription = ""
        if (smCoolingPeriod?.consumedAmount ?: 0.0 >= smCoolingPeriod?.maxAllowedCoolingPeriodAmount.parseToDouble()) {
            errorDescription = Translator.getString(
                context,
                Strings.common_display_text_cooling_period_limit_consumed_error,
                smCoolingPeriod?.coolingPeriodDuration.toString() + getCoolingHoursLabel(),
                beneficiary.value?.fullName().toString()
            )
        } else {
            errorDescription = Translator.getString(
                context,
                Strings.common_display_text_cooling_period_limit_error,
                smCoolingPeriod?.maxAllowedCoolingPeriodAmount.parseToDouble()
                    .minus(smCoolingPeriod?.consumedAmount ?: 0.0)
                    .toString().toFormattedCurrency(),
                smCoolingPeriod?.coolingPeriodDuration.toString() + getCoolingHoursLabel(),
                beneficiary.value?.fullName().toString()
            )
        }
        return errorDescription
//        errorEvent.value = errorDescription
    }

    private fun getCoolingHoursLabel(): String {
        return smCoolingPeriod?.coolingPeriodDuration?.parseToDouble()?.let { coolingHours ->
            return@let if (coolingHours > 1) " hours" else " hour"
        } ?: " hour"
    }

}
