package co.yap.modules.dashboard.cards.reordercard.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.cards.responsedtos.Address
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IRenewCard {
    interface State : IBase.State {
        var valid: ObservableField<Boolean>
        var cardType: ObservableField<String>
        var cardFee: ObservableField<String>
        var availableCardBalance: ObservableField<String>
        var cardAddressTitle: ObservableField<String>
        var cardAddressSubTitle: ObservableField<String>
        var isAddressConfirm: ObservableField<Boolean>
    }

    interface ViewModel : IBase.ViewModel<State> {
        var fee: String
        var address: Address
        val clickEvent: SingleClickEvent
        var reorderCardSuccess: MutableLiveData<Boolean>
        fun handlePressOnView(id: Int)
        fun requestGetAddressForPhysicalCard()
        fun requestReorderDebitCard(address: Address)
        fun requestReorderCard()
        fun requestReorderSupplementaryCard(address: Address)
        fun requestReorderCardFee(cardType: String?)
        fun requestReorderDebitCardFee()
        fun requestReorderSupplementaryCardFee()

    }

    interface View : IBase.View<ViewModel>
}