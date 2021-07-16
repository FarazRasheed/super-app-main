package co.yap.sendmoney.fundtransfer.interfaces

import androidx.databinding.ObservableField
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITransferSuccess {
    interface View : IBase.View<ViewModel> {
        fun setObservers()

    }

    interface ViewModel : IBase.ViewModel<State> {
        val backButtonPressEvent: SingleClickEvent
        var clickEvent: SingleClickEvent
        var updatedCardBalanceEvent:SingleClickEvent
        fun handlePressOnButtonClick(id: Int)
        fun getAccountBalanceRequest()
    }

    interface State : IBase.State {
        var successHeader: String?
        var flagLayoutVisibility: Boolean?
        var transferAmountHeading: String?
        var buttonTitle: String?
        var availableBalanceString: CharSequence?
        var cutOffMessage: ObservableField<String>
    }
}