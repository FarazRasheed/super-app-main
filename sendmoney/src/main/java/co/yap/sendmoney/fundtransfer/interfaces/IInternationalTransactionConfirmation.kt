package co.yap.sendmoney.fundtransfer.interfaces

import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IInternationalTransactionConfirmation {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun setData()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val isOtpRequired: MutableLiveData<Boolean>
        fun handlePressOnButtonClick(id: Int)
        fun rmtTransferRequest(beneficiaryId: String?)
        fun swiftTransferRequest(beneficiaryId: String?)
        fun requestForTransfer()
        fun proceedToTransferAmount()
        fun getCutOffTimeConfiguration()
    }

    interface State : IBase.State {
        var transferDescription: CharSequence?
        var confirmHeading: String?
        var receivingAmountDescription: CharSequence?
        var transferFeeDescription: CharSequence?
        var cutOffTimeMsg: String?
    }
}