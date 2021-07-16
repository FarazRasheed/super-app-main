package co.yap.sendmoney.adapters

import android.content.Context
import androidx.databinding.ViewDataBinding
import co.yap.networking.transactions.responsedtos.InternationalFundsTransferReasonList
import co.yap.sendmoney.BR
import co.yap.yapcore.BaseBindingArrayAdapter
import co.yap.yapcore.BaseBindingHolder

class ReasonListAdapter(
    context: Context,
    resource: Int,
    objects: List<InternationalFundsTransferReasonList.ReasonList>
) :
    BaseBindingArrayAdapter<InternationalFundsTransferReasonList.ReasonList, ReasonListAdapter.ViewHolder>(
        context,
        resource,
        objects
    ) {


    override fun createViewHolder(binding: ViewDataBinding): ViewHolder {
        return ViewHolder(binding)
    }


    inner class ViewHolder(binding: ViewDataBinding) : BaseBindingHolder(binding) {

        override fun bind(obj: Any, binding: ViewDataBinding?) {
            binding?.setVariable(getBindingVariable(), obj)
            binding?.executePendingBindings()
        }

        override fun getBindingVariable(): Int = BR.reasonList
        private fun onBind(binding: Object) {
        }
    }
}