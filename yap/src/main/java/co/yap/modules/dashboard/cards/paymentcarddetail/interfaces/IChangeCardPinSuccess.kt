package co.yap.modules.dashboard.cards.paymentcarddetail.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IChangeCardPinSuccess {

    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnButtonClick(id: Int)
    }

    interface State : IBase.State
}