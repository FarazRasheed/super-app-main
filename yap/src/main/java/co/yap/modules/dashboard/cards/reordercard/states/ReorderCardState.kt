package co.yap.modules.dashboard.cards.reordercard.states

import androidx.databinding.ObservableBoolean
import co.yap.modules.dashboard.cards.reordercard.interfaces.IReorderCard
import co.yap.yapcore.BaseState

class ReorderCardState : BaseState(), IReorderCard.State {

    override var toolbarVisibility: ObservableBoolean = ObservableBoolean(true)
}