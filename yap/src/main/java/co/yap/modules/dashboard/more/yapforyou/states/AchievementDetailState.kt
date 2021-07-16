package co.yap.modules.dashboard.more.yapforyou.states

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.more.yapforyou.interfaces.IAchievementDetail
import co.yap.yapcore.BaseState

class AchievementDetailState : BaseState(), IAchievementDetail.State {
    override var achievementIcon: ObservableField<Int> = ObservableField(-1)


}