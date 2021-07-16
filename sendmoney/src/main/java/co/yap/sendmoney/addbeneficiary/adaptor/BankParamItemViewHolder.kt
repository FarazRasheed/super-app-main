package co.yap.sendmoney.addbeneficiary.adaptor

import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import co.yap.sendmoney.databinding.ItemBankParamsBinding
import co.yap.networking.customers.responsedtos.beneficiary.BankParams
import co.yap.sendmoney.addbeneficiary.viewmodels.BankParamsItemViewModel

class BankParamItemViewHolder(private val itemBankParamsBinding: ItemBankParamsBinding) :
    RecyclerView.ViewHolder(itemBankParamsBinding.root) {

    fun onBind(bankParams: BankParams, isLastIndex: Boolean, watcher: TextWatcher) {
        bankParams.name = bankParams.name?.toLowerCase()?.capitalize()
        itemBankParamsBinding.viewModel = BankParamsItemViewModel(bankParams, 0, null)
        itemBankParamsBinding.executePendingBindings()
        itemBankParamsBinding.etBankName.addTextChangedListener(watcher)
        itemBankParamsBinding.etBankName.imeOptions =
            if (isLastIndex) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT
    }
}
