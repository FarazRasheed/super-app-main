package co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.interfaces.IAddFunds
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.extentions.getValueWithoutComa

class AddFundsState : BaseState(), IAddFunds.State {
    override var card: ObservableField<Card> = ObservableField()
    override var firstDenomination: ObservableField<String> = ObservableField("")
    override var secondDenomination: ObservableField<String> = ObservableField("")
    override var thirdDenomination: ObservableField<String> = ObservableField("")
    override var transferFee: ObservableField<CharSequence> = ObservableField()
    override var availableBalance: ObservableField<CharSequence> = ObservableField()
    override var topUpSuccessMsg: ObservableField<CharSequence> = ObservableField()
    override var debitCardUpdatedBalance: ObservableField<CharSequence> = ObservableField()
    override var spareCardUpdatedBalance: ObservableField<CharSequence> = ObservableField()

    @get:Bindable
    override var amount: String = ""
        set(value) {
            field = value.getValueWithoutComa()
            notifyPropertyChanged(BR.amount)
        }

    @get:Bindable
    override var maxLimit: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.maxLimit)
        }

    @get:Bindable
    override var minLimit: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.minLimit)
        }

    override var valid: ObservableBoolean = ObservableBoolean(false)
}