package co.yap.modules.dashboard.cards.paymentcarddetail.interfaces

import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface IUpdateCardName {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val EVENT_UPDATE_CARD_NAME: Int get() = 1
        val clickEvent: SingleClickEvent
        var card: Card
        fun handlePressOnView(id: Int)
        fun updateCardName()
    }

    interface State : IBase.State {
        var cardName: String
        var valid: Boolean
    }
}