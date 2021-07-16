package co.yap.sendmoney.fundtransfer.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.networking.transactions.responsedtos.transaction.FxRateResponse
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IInternationalFundsTransfer {

    interface State : IBase.State {
        var transactionNote: ObservableField<String>
        var sourceCurrency: ObservableField<String>
        var destinationCurrency: ObservableField<String>
        var transferFeeSpannable: CharSequence?
        var etInputAmount: String?
        var etOutputAmount: String?
        var fromFxRate: String?
        var toFxRate: String?
        var valid: Boolean
        var maxLimit: Double?
        var minLimit: Double?
        var errorDescription: String
        var availableBalanceString: CharSequence?
        fun clearError()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        var isAPIFailed: MutableLiveData<Boolean>
        var reasonPosition: Int
        var fxRateResponse: MutableLiveData<FxRateResponse.Data>
        var purposeOfPaymentList: MutableLiveData<ArrayList<PurposeOfPayment>>
        fun handlePressOnButton(id: Int)
        fun getReasonList(productCode: String)
        fun getTransactionInternationalfxList(productCode: String?)
        fun getMoneyTransferLimits(productCode: String?)
        fun getCountryLimits()
        fun getTransactionThresholds()
        fun processPurposeList(list: ArrayList<PurposeOfPayment>)
        fun getCutOffTimeConfiguration()
        fun checkCoolingPeriodRequest(
            beneficiaryId: String?,
            beneficiaryCreationDate: String?,
            beneficiaryName: String?,
            amount: String?,
            success: () -> Unit
        )
    }

    interface View : IBase.View<ViewModel>
}
