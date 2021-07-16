package co.yap.modules.dashboard.yapit.topup.topupamount.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.dashboard.yapit.topup.topupamount.interfaces.IVerifyCardCvv
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.BaseState

class VerifyCardCvvState : BaseState(), IVerifyCardCvv.State {
    @get:Bindable
    override var cardCvv: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardCvv)
        }
    override var cardInfo: ObservableField<TopUpCard> = ObservableField(TopUpCard())
    override var formattedCardNo: ObservableField<String> = ObservableField()
    override var cvvSpanableString: ObservableField<String> = ObservableField()
}