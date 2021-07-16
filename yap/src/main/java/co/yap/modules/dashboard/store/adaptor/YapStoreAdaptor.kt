package co.yap.modules.dashboard.store.adaptor

import androidx.databinding.ViewDataBinding
import co.yap.R
import co.yap.databinding.ItemYapStoreBinding
import co.yap.modules.dashboard.store.viewholder.YapStoreItemViewHolder
import co.yap.networking.store.responsedtos.Store
import co.yap.yapcore.BaseBindingRecyclerAdapter

class YapStoreAdaptor(val data: MutableList<Store>) :
    BaseBindingRecyclerAdapter<Store, YapStoreItemViewHolder>(data) {

    override fun onCreateViewHolder(binding: ViewDataBinding): YapStoreItemViewHolder {
        return YapStoreItemViewHolder(binding as ItemYapStoreBinding)
    }

    override fun onBindViewHolder(holder: YapStoreItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(getDataForPosition(position))
    }

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_yap_store

}
