package co.yap.modules.dashboard.cards.paymentcarddetail.limits.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.cards.paymentcarddetail.limits.interfaces.ICardLimits
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseState

class CardLimitState : BaseState(), ICardLimits.State {

    override var card: ObservableField<Card> = ObservableField()
}