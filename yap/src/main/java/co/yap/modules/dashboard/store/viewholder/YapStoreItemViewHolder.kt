package co.yap.modules.dashboard.store.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemYapStoreBinding
import co.yap.modules.dashboard.store.viewmodels.YapStoreItemViewModel
import co.yap.networking.store.responsedtos.Store


class YapStoreItemViewHolder(private val itemYapStoreBinding: ItemYapStoreBinding) :
    RecyclerView.ViewHolder(itemYapStoreBinding.root) {

    fun onBind(store: Store?) {
        itemYapStoreBinding.viewModel = YapStoreItemViewModel(store)
        /*if (store?.name == "YAP Household") {
            itemYapStoreBinding.labelCardType.visibility = View.GONE
        }*/
        itemYapStoreBinding.executePendingBindings()
    }
}