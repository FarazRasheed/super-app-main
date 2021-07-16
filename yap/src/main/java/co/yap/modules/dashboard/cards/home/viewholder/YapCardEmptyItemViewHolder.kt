package co.yap.modules.dashboard.cards.home.viewholder

import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemYapCardEmptyBinding
import co.yap.modules.dashboard.cards.home.viewmodels.YapCardEmptyItemViewModel
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.interfaces.OnItemClickListener


class YapCardEmptyItemViewHolder(private val itemYapCardEmptyBinding: ItemYapCardEmptyBinding) :
    RecyclerView.ViewHolder(itemYapCardEmptyBinding.root) {

    fun onBind(
        position: Int,
        paymentCard: Card?,
        dimensions: IntArray,
        onItemClickListener: OnItemClickListener?
    ) {

        val params = itemYapCardEmptyBinding.imgCard.layoutParams as FrameLayout.LayoutParams
        params.width = dimensions[0]
        params.height = dimensions[1]
        itemYapCardEmptyBinding.imgCard.layoutParams = params

        itemYapCardEmptyBinding.viewModel =
            YapCardEmptyItemViewModel(paymentCard, position, onItemClickListener)
        itemYapCardEmptyBinding.executePendingBindings()
    }
}