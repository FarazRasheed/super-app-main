package co.yap.sendmoney.fundtransfer.interfaces

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import co.yap.networking.transactions.responsedtos.InternationalFundsTransferReasonList
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ICashTransfer {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        var isAPIFailed: MutableLiveData<Boolean>
        var reasonPosition: Int
        val clickEvent: SingleClickEvent
        val errorEvent: SingleClickEvent
        var transactionData: MutableLiveData<ArrayList<InternationalFundsTransferReasonList.ReasonList>>
        var receiverUUID: String
        var purposeOfPaymentList: MutableLiveData<ArrayList<PurposeOfPayment>>
        fun handlePressOnView(id: Int)
        fun cashPayoutTransferRequest(beneficiaryId: Int?)
        fun getMoneyTransferLimits(productCode: String?)
        fun getPurposeOfPayment(productCode: String)
        fun getCountryLimit()
        fun getTransactionThresholds()
        fun proceedToTransferAmount()
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

    interface State : IBase.State {
        var amountBackground: Drawable?
        var feeAmountSpannableString: CharSequence?
        var availableBalanceString: CharSequence?
        var amount: String
        var valid: Boolean
        var minLimit: Double
        var errorDescription: String
        var maxLimit: Double
        var noteValue: String?
        fun clearError()
        var transactionData: ArrayList<InternationalFundsTransferReasonList.ReasonList>
        val populateSpinnerData: MutableLiveData<List<InternationalFundsTransferReasonList.ReasonList>>
        var produceCode: String?
    }
}