package co.yap.modules.setcardpin.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ISetCardPinWelcome {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State>{
        val clickEvent: SingleClickEvent
        fun handlePressOnCreatePin(id: Int)
        fun handlePressOnCreatePinLater(id: Int)
    }

    interface State : IBase.State
}