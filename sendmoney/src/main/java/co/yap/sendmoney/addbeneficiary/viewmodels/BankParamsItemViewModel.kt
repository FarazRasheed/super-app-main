package co.yap.sendmoney.addbeneficiary.viewmodels

import android.view.View
import co.yap.networking.customers.responsedtos.beneficiary.BankParams
import co.yap.yapcore.interfaces.OnItemClickListener

class BankParamsItemViewModel(
    val bankParams: BankParams?,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {

    fun onViewClicked(view: View) {
        bankParams?.let {
            onItemClickListener?.onItemClick(view, it, position)
        }
    }

}