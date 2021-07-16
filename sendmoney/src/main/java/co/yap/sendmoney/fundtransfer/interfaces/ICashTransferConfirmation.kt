package co.yap.sendmoney.fundtransfer.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.networking.transactions.responsedtos.TransactionThresholdModel
import co.yap.sendmoney.databinding.FragmentCashTransferConfirmationBinding
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ICashTransferConfirmation {

    interface View : IBase.View<ViewModel> {
        fun addObservers()
        fun removeObservers()
        fun getViewBinding(): FragmentCashTransferConfirmationBinding
    }

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
        fun proceedToTransferAmount()
        fun getCutOffTimeConfiguration()
        fun uaeftsTransferRequest(beneficiaryId: String?)
        fun domesticTransferRequest(beneficiaryId: String?)
    }

    interface State : IBase.State {
        var description: ObservableField<CharSequence>
        var cutOffTimeMsg: ObservableField<String>
        var productCode: ObservableField<String>
        var transferFeeDescription:ObservableField<CharSequence>
    }
}