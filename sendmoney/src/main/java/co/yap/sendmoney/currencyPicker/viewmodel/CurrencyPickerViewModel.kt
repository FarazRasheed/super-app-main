package co.yap.sendmoney.currencyPicker.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.sendmoney.currencyPicker.interfaces.ICurrencyPicker
import co.yap.sendmoney.currencyPicker.model.MultiCurrencyWallet
import co.yap.sendmoney.currencyPicker.adapters.CurrencyAdapter
import co.yap.sendmoney.currencyPicker.state.CurrencyPickerState
import co.yap.yapcore.SingleClickEvent

class CurrencyPickerViewModel(application: Application) :
    MCLandingBaseViewModel<ICurrencyPicker.State>(application),
    ICurrencyPicker.ViewModel {

    override val state: ICurrencyPicker.State =
        CurrencyPickerState()

    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override val currencyAdapter: CurrencyAdapter =
        CurrencyAdapter(mutableListOf())

    override var availableCurrenciesList: MutableList<MultiCurrencyWallet> = mutableListOf()

    override var searchQuery: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        //setUpDummyData()

        currencyAdapter.setList(availableCurrenciesList)
    }

 /*   fun setUpDummyData() {

        availableCurrenciesList.add(
            MultiCurrencyWallet(
                "ae",
                "5000",
                SessionManager.getDefaultCurrency(),
                "Dirham",
                "2.5"
            )
        )

        availableCurrenciesList.add(
            MultiCurrencyWallet(
                "CA",
                "1000",
                "CAD",
                "Canadian Dollar",
                "3.5"
            )
        )
        availableCurrenciesList.add(
            MultiCurrencyWallet(
                "pk",
                "5000",
                "PKR",
                "",
                "2.5"
            )
        )

        availableCurrenciesList.add(
            MultiCurrencyWallet(
                "in",
                "1000",
                "INR",
                "",
                "1.5"
            )
        )
        availableCurrenciesList.add(
            MultiCurrencyWallet(
                "ae",
                "5000",
                SessionManager.getDefaultCurrency(),
                "Dirham",
                "4.5"
            )
        )


    }*/

}
