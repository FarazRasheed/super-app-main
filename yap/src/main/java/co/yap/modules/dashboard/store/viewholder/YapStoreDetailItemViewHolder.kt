package co.yap.modules.dashboard.store.viewholder

import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemYapStoreDetailBinding
import co.yap.modules.dashboard.store.viewmodels.YapStoreDetailItemViewModel
import co.yap.networking.store.responsedtos.Store


class YapStoreDetailItemViewHolder(private val itemYapStoreDetailBinding: ItemYapStoreDetailBinding) :
    RecyclerView.ViewHolder(itemYapStoreDetailBinding.root) {

    fun onBind(store: Store) {
        itemYapStoreDetailBinding.viewModel = YapStoreDetailItemViewModel(store)
        itemYapStoreDetailBinding.executePendingBindings()
    }
}