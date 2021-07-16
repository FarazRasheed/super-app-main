package co.yap.modules.dashboard.yapit.topup.carddetail

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITopUpCardDetail {

    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun onRemoveCard(cardId:String)
        fun handlePressOnView(id: Int)
        fun handlePressOnBackButton(id: Int)
    }

    interface State : IBase.State {
        val title: ObservableField<String>
        val cardFormattedExpiry: ObservableField<String>
        val cardInfo:ObservableField<TopUpCard>
        val isCardDeleted: MutableLiveData<Boolean>
    }
}