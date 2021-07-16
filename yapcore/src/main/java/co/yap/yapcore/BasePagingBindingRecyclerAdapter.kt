package co.yap.yapcore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.yap.networking.store.responsedtos.Store
import co.yap.yapcore.databinding.ItemListFooterBinding
import co.yap.yapcore.helpers.PagingState
import co.yap.yapcore.interfaces.OnItemClickListener


abstract class BasePagingBindingRecyclerAdapter<T : Any>(
    private val retry: () -> Unit,
    diffCallback: DiffUtil.ItemCallback<T>
) :
    PagedListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    val contentView = 1
    private val footerView = 2

    private var state = PagingState.LOADING
    var onItemClickListener: OnItemClickListener? = null
    private var filteredData: ArrayList<T>? = null

    protected abstract fun onCreateContentViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder

    protected abstract fun onCreateFooterViewHolder(binding: ViewDataBinding): ListFooterViewHolder

    protected abstract fun getLayoutIdForViewType(viewType: Int): Int

    protected abstract fun getLayoutIdForFooterType(viewType: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                if (viewType == contentView) getLayoutIdForViewType(viewType) else getLayoutIdForFooterType(
                    viewType
                ),
                parent, false
            )

        return if (viewType == contentView)
            onCreateContentViewHolder(binding)
        else
            onCreateFooterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(
                it,
                super.getItem(position)!!,
                position
            )
        }
    }

    fun setItemListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) contentView else footerView

    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == PagingState.LOADING || state == PagingState.ERROR)
    }

    fun setState(state: PagingState) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    fun getState(): PagingState {
        return state
    }


    class ListFooterViewHolder(private val itemListFooterBinding: ItemListFooterBinding) :
        RecyclerView.ViewHolder(itemListFooterBinding.root) {

        fun onBind(pagingState: PagingState) {
            //itemListFooterBinding.viewModel = YapStoreItemViewModel(store)
            itemListFooterBinding.executePendingBindings()
        }
    }

    abstract inner class ViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Store) {
        }
    }

}