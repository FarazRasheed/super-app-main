package co.yap.modules.dashboard.yapit.topup.carddetail

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.BaseState

class TopUpCardDetailState : BaseState(),
    ITopUpCardDetail.State {
    override val title: ObservableField<String> = ObservableField("")
    override val cardFormattedExpiry: ObservableField<String> = ObservableField("")
    override val isCardDeleted: MutableLiveData<Boolean> = MutableLiveData(false)
    override val cardInfo: ObservableField<TopUpCard> =
        ObservableField(TopUpCard())
}