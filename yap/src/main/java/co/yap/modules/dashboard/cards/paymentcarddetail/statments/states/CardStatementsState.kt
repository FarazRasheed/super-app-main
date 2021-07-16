package co.yap.modules.dashboard.cards.paymentcarddetail.statments.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.cards.paymentcarddetail.statments.interfaces.ICardStatments
import co.yap.networking.transactions.responsedtos.CardStatement
import co.yap.yapcore.BaseState

class CardStatementsState : BaseState(), ICardStatments.State {

    override var year: ObservableField<String> = ObservableField()
    override var hasRecords: ObservableField<Boolean> = ObservableField()
    override var statementList: List<CardStatement>? = ArrayList()
    @get:Bindable
    override var previousMonth: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.previousMonth)
        }

    @get:Bindable
    override var nextMonth: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.nextMonth)
        }


}