package co.yap.sendmoney.currencyPicker.state

import androidx.databinding.ObservableField
import co.yap.sendmoney.currencyPicker.interfaces.ICurrencyPicker
import co.yap.yapcore.BaseState

class CurrencyPickerState : BaseState(),
    ICurrencyPicker.State {
     override var currencyDialogChecker: ObservableField<Boolean> = ObservableField()


}