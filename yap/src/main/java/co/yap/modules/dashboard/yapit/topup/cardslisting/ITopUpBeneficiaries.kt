package co.yap.modules.dashboard.yapit.topup.cardslisting

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.CardLimits
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ITopUpBeneficiaries {

    interface State : IBase.State {
        var tootlBarTitle: String
        var tootlBarVisibility: Int
        var rightButtonVisibility: Int
        var leftButtonVisibility: Int
        val valid :ObservableField<Boolean>
        val responseReceived :ObservableField<Boolean>
        val enableAddCard :ObservableBoolean
        var noOfCard: ObservableField<String>
        var alias: ObservableField<String>
        var message: ObservableField<String>
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnBackButton(id: Int)
        fun handlePressOnView(id: Int)
        fun getPaymentCards()
        fun updateCardCount()
        val clickEvent: SingleClickEvent
        val topUpCards: MutableLiveData<List<TopUpCard>>
        var cardLimits : CardLimits?
    }

    interface View : IBase.View<ViewModel>{
        var successButtonLabel: String
    }
}