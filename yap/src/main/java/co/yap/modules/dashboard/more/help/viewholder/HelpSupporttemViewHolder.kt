package co.yap.modules.dashboard.more.help.viewholder

import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemHelpSupportBinding
import co.yap.modules.dashboard.more.help.viewmodels.HelpSupportItemViewModel
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.yapcore.interfaces.OnItemClickListener


class HelpSupporttemViewHolder(private val itemHelpSupportBinding: ItemHelpSupportBinding) :
    RecyclerView.ViewHolder(itemHelpSupportBinding.root) {

    fun onBind(
        position: Int,
        moreOption: MoreOption,
        dimensions: IntArray,
        onItemClickListener: OnItemClickListener?
    ) {

//        val unwrappedDrawable = AppCompatResources.getDrawable(
//            itemHelpSupportBinding.imgIcon.context,
//            R.drawable.bg_round_purple
//        )
//        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
//        DrawableCompat.setTint(wrappedDrawable, moreOption.bgColor)
//
//        itemHelpSupportBinding.imgIcon.background = wrappedDrawable

        itemHelpSupportBinding.viewModel =
            HelpSupportItemViewModel(moreOption, position, onItemClickListener)
        itemHelpSupportBinding.executePendingBindings()

    }
}