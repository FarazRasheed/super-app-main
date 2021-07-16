package co.yap.sendmoney.currencyPicker.interfaces

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IMCLanding {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val addWalletClickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
        fun handlePressOnAddWalletView(id: Int)

    }

    interface State : IBase.State {
        var title: ObservableField<String>
        var toolbarBackIcon: ObservableBoolean
        var toolbarRateIcon: ObservableBoolean
        var toolbarAddWalletIcon: ObservableBoolean
        var isPrimaryWallet:MutableLiveData<Boolean>
    }
}