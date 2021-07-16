package co.yap.modules.setcardpin.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ISetCardPinSuccess {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnTopUp(id: Int)
        fun handlePressOnTopUpLater(id: Int)
    }

    interface State : IBase.State
}