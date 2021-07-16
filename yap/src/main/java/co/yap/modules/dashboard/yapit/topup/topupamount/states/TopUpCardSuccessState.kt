package co.yap.modules.dashboard.yapit.topup.topupamount.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.yapit.topup.topupamount.interfaces.ITopUpCardSuccess
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.BaseState

class TopUpCardSuccessState : BaseState(), ITopUpCardSuccess.State {
    @get:Bindable
    override var buttonTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }

    @get:Bindable
    override var topUpSuccess: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.topUpSuccess)
        }

    @get:Bindable
    override var currencyType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencyType)
        }

    @get:Bindable
    override var amount: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.amount)
        }

    override var cardInfo: ObservableField<TopUpCard> = ObservableField(TopUpCard())
    override var formattedCardNo: ObservableField<String> = ObservableField()
    override var availableBalanceSpanable: ObservableField<String> = ObservableField()
}