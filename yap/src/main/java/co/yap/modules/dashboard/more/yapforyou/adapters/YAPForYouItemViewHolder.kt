package co.yap.modules.dashboard.more.yapforyou.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.yap.databinding.ItemYapForYouBinding
import co.yap.modules.dashboard.more.yapforyou.itemviewmodels.YAPForYouItemViewModel
import co.yap.networking.transactions.responsedtos.achievement.Achievement
import co.yap.yapcore.interfaces.OnItemClickListener

class YAPForYouItemViewHolder(private val itemYapForYouBinding: ItemYapForYouBinding) :
    RecyclerView.ViewHolder(itemYapForYouBinding.root) {

    fun onBind(
        position: Int,
        achievements: Achievement,
        onItemClickListener: OnItemClickListener?
    ) {
        itemYapForYouBinding.tvLocked.visibility =
            if (position == 0 || position == 1 || position == 2) View.GONE else View.VISIBLE

        itemYapForYouBinding.viewModel =
            YAPForYouItemViewModel(
                achievement = achievements,
                position = position,
                onItemClickListener = onItemClickListener
            )
        itemYapForYouBinding.executePendingBindings()

    }
}