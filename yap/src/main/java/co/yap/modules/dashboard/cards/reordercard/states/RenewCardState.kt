package co.yap.modules.dashboard.cards.reordercard.states

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.cards.reordercard.interfaces.IRenewCard
import co.yap.yapcore.BaseState

class RenewCardState : BaseState(), IRenewCard.State {
    override var cardType: ObservableField<String> = ObservableField("")
    override var cardFee: ObservableField<String> = ObservableField("")
    override var availableCardBalance: ObservableField<String> = ObservableField("")
    override var cardAddressTitle: ObservableField<String> = ObservableField("")
    override var cardAddressSubTitle: ObservableField<String> = ObservableField("")
    override var isAddressConfirm: ObservableField<Boolean> = ObservableField(false)
    override var valid: ObservableField<Boolean> = ObservableField(false)
}
