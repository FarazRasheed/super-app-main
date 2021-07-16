package co.yap.sendmoney.helper

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import co.yap.networking.customers.responsedtos.beneficiary.BankParams
import co.yap.sendmoney.addbeneficiary.adaptor.AddBeneficiariesAdaptor

object SendMoneyBindingHelper {
    @BindingAdapter("adaptorListBankParams")
    @JvmStatic
    fun setAdaptorParam(recycleview: RecyclerView, list: ObservableField<List<BankParams>>) {
        if (!list.get().isNullOrEmpty())
            if (recycleview.adapter is AddBeneficiariesAdaptor)
                (recycleview.adapter as AddBeneficiariesAdaptor).setList(list.get() as List<BankParams>)
    }
}