package co.yap.sendmoney.currencyPicker.viewmodel

import android.view.View
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.sendmoney.currencyPicker.model.MultiCurrencyWallet
class CurrencyItemViewModel(
    val multiCurrencyWallet: MultiCurrencyWallet?,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {

    fun onViewClicked(view: View) {
        onItemClickListener?.onItemClick(view, multiCurrencyWallet!!, position)
    }

}