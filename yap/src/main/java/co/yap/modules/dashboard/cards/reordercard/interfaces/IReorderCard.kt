package co.yap.modules.dashboard.cards.reordercard.interfaces

import androidx.databinding.ObservableBoolean
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IReorderCard {

    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
        var card: Card?
    }

    interface View : IBase.View<ViewModel>
}