package co.yap.modules.dashboard.yapit.y2y.transfer.interfaces

import android.graphics.drawable.Drawable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.requestdtos.SMCoolingPeriodRequest
import co.yap.networking.customers.responsedtos.sendmoney.SMCoolingPeriod
import co.yap.networking.transactions.responsedtos.TransactionThresholdModel
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

class IY2YFundsTransfer {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun getBinding(): ViewDataBinding
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val transactionThreshold: MutableLiveData<TransactionThresholdModel>
        var receiverUUID: String
        var smCoolingPeriod: SMCoolingPeriod?
        //        val transferFundSuccess: MutableLiveData<Boolean>
        fun handlePressOnView(id: Int)
        fun getTransactionThresholds()
        fun proceedToTransferAmount(success: () -> Unit)
        fun checkCoolingPeriodRequest(
            beneficiaryId: String?,
            beneficiaryCreationDate: String?,
            beneficiaryName: String?,
            amount: String?, success: () -> Unit
        )

        fun getTransactionLimits()
        fun getCoolingPeriod(smCoolingPeriodRequest: SMCoolingPeriodRequest)
        fun isInCoolingPeriod(): Boolean
        fun isCPAmountConsumed(inputAmount: String): Boolean
        fun showCoolingPeriodLimitError():String?
    }

    interface State : IBase.State {
        var amount: String
        var amountBackground: Drawable?
        var valid: Boolean
        var minLimit: Double
        var availableBalance: String?
        var errorDescription: String
        var currencyType: String
        var maxLimit: Double
        var availableBalanceText: String
        var availableBalanceGuide: String
        var fullName: String
        var noteValue: String
        var imageUrl: String
        var transferFee: CharSequence?
    }
}