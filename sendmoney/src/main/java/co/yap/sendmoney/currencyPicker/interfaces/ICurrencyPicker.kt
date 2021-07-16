package co.yap.sendmoney.currencyPicker.interfaces

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.sendmoney.currencyPicker.adapters.CurrencyAdapter
import co.yap.sendmoney.currencyPicker.model.MultiCurrencyWallet
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ICurrencyPicker {

    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun removeObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val currencyAdapter: CurrencyAdapter
        var availableCurrenciesList: MutableList<MultiCurrencyWallet>
        var searchQuery: MutableLiveData<String>
        fun handlePressOnView(id: Int)
    }

    interface State : IBase.State{
          var currencyDialogChecker:ObservableField<Boolean>
    }
}