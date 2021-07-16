package co.yap.modules.dashboard.yapit.topup.addtopupcard.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.IBase

interface IAddTopUpCard {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        fun addTopUpCard(sessionId: String, alias: String, color: String, number: String)
        val isCardAdded: MutableLiveData<TopUpCard>?
    }

    interface State : IBase.State {
        var url: String
        var toolbarVisibility: ObservableField<Boolean>
    }
}