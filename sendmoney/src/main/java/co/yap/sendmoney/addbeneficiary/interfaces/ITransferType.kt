package co.yap.sendmoney.addbeneficiary.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITransferType {

    interface State : IBase.State {
    }

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        fun handlePressOnTypeBankTransfer(id: Int)
        fun handlePressOnTypeCashPickUp(id: Int)
    }

    interface View : IBase.View<ViewModel>
}