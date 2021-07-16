package co.yap.modules.dashboard.more.home.viewholder

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemYapMoreBinding
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.modules.dashboard.more.home.viewmodels.YapMoreItemViewModel
import co.yap.yapcore.interfaces.OnItemClickListener


class YapMoreItemViewHolder(private val itemYapMoreBinding: ItemYapMoreBinding) :
    RecyclerView.ViewHolder(itemYapMoreBinding.root) {

    fun onBind(
        position: Int,
        moreOption: MoreOption,
        dimensions: IntArray,
        onItemClickListener: OnItemClickListener?
    ) {

        val unwrappedDrawable = AppCompatResources.getDrawable(
            itemYapMoreBinding.imgIcon.context,
            R.drawable.bg_round_purple_more
        )
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, moreOption.bgColor)

        itemYapMoreBinding.imgIcon.background = wrappedDrawable

        itemYapMoreBinding.viewModel =
            YapMoreItemViewModel(moreOption, position, onItemClickListener)
        itemYapMoreBinding.executePendingBindings()

    }
}