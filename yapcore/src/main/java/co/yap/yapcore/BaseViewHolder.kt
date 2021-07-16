package co.yap.yapcore

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<ITEM : Any, VM : BaseListItemViewModel<ITEM>>
    (view: View, viewModel: VM, private val mDataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(view) {
    private val mViewModel: VM = viewModel
    fun setItem(item: ITEM, position: Int) {
        mViewModel.setItem(item, position)
        mDataBinding.executePendingBindings()
    }
}