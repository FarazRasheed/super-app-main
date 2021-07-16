package co.yap.modules.dashboard.cards.paymentcarddetail.limits.interfaces

import androidx.databinding.ObservableField
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface ICardLimits {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
    }

    interface State : IBase.State {
        var card: ObservableField<Card>
    }
}