package co.yap.sendmoney.fundtransfer.interfaces

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.requestdtos.SMCoolingPeriodRequest
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.networking.customers.responsedtos.sendmoney.SMCoolingPeriod
import co.yap.networking.transactions.responsedtos.TransactionThresholdModel
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.sendmoney.fundtransfer.models.TransferFundData
import co.yap.yapcore.IBase

interface IBeneficiaryFundTransfer {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        var errorEvent: MutableLiveData<String>
        var beneficiary: MutableLiveData<Beneficiary>
        var transferData: MutableLiveData<TransferFundData>
        var transactionThreshold: MutableLiveData<TransactionThresholdModel>
        var transactionWillHold: Boolean
        var selectedPop: PurposeOfPayment?
        var isCutOffTimeStarted: Boolean
        var isSameCurrency: Boolean
        fun getCoolingPeriod(smCoolingPeriodRequest: SMCoolingPeriodRequest)
        fun isInCoolingPeriod(): Boolean
        fun isCPAmountConsumed(inputAmount: String): Boolean
        fun showCoolingPeriodLimitError():String?
        var smCoolingPeriod: SMCoolingPeriod?

    }

    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
        var rightIcon: ObservableBoolean
        var leftIcon: ObservableBoolean
        var rightButtonText: String?
        var position: Int
    }
}