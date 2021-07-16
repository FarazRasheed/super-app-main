package co.yap.modules.dashboard.cards.paymentcarddetail.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.cards.paymentcarddetail.interfaces.IPaymentCardDetail
import co.yap.yapcore.BaseState
import co.yap.yapcore.managers.SessionManager

class PaymentCardDetailState : BaseState(), IPaymentCardDetail.State {

    override var cardStatus: ObservableField<String> = ObservableField("")

    @get:Bindable
    override var accountType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.accountType)
        }

    @get:Bindable
    override var cardType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardType)
        }

    @get:Bindable
    override var cardTypeText: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardTypeText)
        }

    @get:Bindable
    override var cardPanNumber: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardPanNumber)
        }

    @get:Bindable
    override var cardBalance: String = "${SessionManager.getDefaultCurrency()}0.00"
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardBalance)
        }

    @get:Bindable
    override var cardName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardName)
        }

    @get:Bindable
    override var blocked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.blocked)
        }

    @get:Bindable
    override var physical: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.physical)
        }

    @get:Bindable
    override var balanceLoading: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.balanceLoading)
        }

    override var filterCount: ObservableField<Int> = ObservableField(0)
    override var isTxnsEmpty: ObservableField<Boolean> = ObservableField(false)
}