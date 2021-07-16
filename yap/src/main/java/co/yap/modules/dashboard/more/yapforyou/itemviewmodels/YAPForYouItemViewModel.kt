package co.yap.modules.dashboard.more.yapforyou.itemviewmodels

import android.view.View
import co.yap.networking.transactions.responsedtos.achievement.Achievement
import co.yap.yapcore.interfaces.OnItemClickListener

class YAPForYouItemViewModel(
    val achievement: Achievement,
    val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {
    fun handlePressOnView(view: View) {
        onItemClickListener?.onItemClick(view, achievement, position)
    }
}