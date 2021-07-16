package co.yap.modules.dashboard.more.yapforyou.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemCompletedAchievementBinding
import co.yap.networking.transactions.responsedtos.achievement.Achievement
import co.yap.modules.dashboard.more.yapforyou.itemviewmodels.CompletedAchievementItemViewModel
import co.yap.yapcore.BaseBindingRecyclerAdapter

class CompletedAchievementsAdaptor(val list: MutableList<Achievement>) :
    BaseBindingRecyclerAdapter<Achievement, CompletedAchievementsAdaptor.CompletedItemViewHolder>(
        list
    ) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_completed_achievement

    override fun onCreateViewHolder(binding: ViewDataBinding): CompletedItemViewHolder {
        return CompletedItemViewHolder(binding as ItemCompletedAchievementBinding)
    }

    override fun onBindViewHolder(holder: CompletedItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position])
    }

    class CompletedItemViewHolder(private val itemCompletedAchievementBinding: ItemCompletedAchievementBinding) :
        RecyclerView.ViewHolder(itemCompletedAchievementBinding.root) {

        fun onBind(achievement: Achievement) {
            val item = achievement.copy()
            if (item.percentage ?: 0.0 == 100.00) {
                itemCompletedAchievementBinding.viewModel =
                    CompletedAchievementItemViewModel(item)
                itemCompletedAchievementBinding.executePendingBindings()
            }
        }
    }
}