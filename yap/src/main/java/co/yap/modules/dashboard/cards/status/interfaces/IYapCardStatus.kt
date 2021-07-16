package co.yap.modules.dashboard.cards.status.interfaces

import androidx.databinding.ObservableField
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface IYapCardStatus {

    interface State : IBase.State {
        var valid: Boolean
        var totalProgress: Int
        var buildingProgress: Int
        var shippingProgress: Int
        val message: ObservableField<String>
        val title: ObservableField<String>
        val cardType: ObservableField<String>
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
    }

    interface View : IBase.View<ViewModel>
}