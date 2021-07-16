package co.yap.modules.dashboard.more.bankdetails.interfaces

import androidx.databinding.ObservableField
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent

interface IBankDetail {
    interface State : IBase.State {
        var name: ObservableField<String>
        var swift: ObservableField<String>
        var iban: ObservableField<String>
        var account: ObservableField<String>
        var bank: ObservableField<String>
        var addresse: ObservableField<String>
        var image: ObservableField<String>
        var initials: ObservableField<String>
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnView(id: Int)
        val clickEvent: SingleClickEvent
    }

    interface View : IBase.View<ViewModel>
}