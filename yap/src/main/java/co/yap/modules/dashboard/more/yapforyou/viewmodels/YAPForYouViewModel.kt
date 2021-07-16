package co.yap.modules.dashboard.more.yapforyou.viewmodels

import android.app.Application
import co.yap.R
import co.yap.modules.dashboard.more.yapforyou.adapters.YAPForYouAdapter
import co.yap.modules.dashboard.more.yapforyou.interfaces.IYAPForYou
import co.yap.modules.dashboard.more.yapforyou.states.YAPForYouState
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.responsedtos.achievement.Achievement
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class YAPForYouViewModel(application: Application) :
    YapForYouBaseViewModel<IYAPForYou.State>(application), IYAPForYou.ViewModel,
    IRepositoryHolder<TransactionsRepository> {

    override val repository: TransactionsRepository = TransactionsRepository
    override val state: YAPForYouState =
        YAPForYouState()

    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var adaptor: YAPForYouAdapter = YAPForYouAdapter(mutableListOf())

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        setToolbarData()
    }

    private fun setToolbarData() {
        state.toolbarTitle = getString(Strings.screen_yap_for_you_display_text_title)
        toggleToolBarVisibility(false)
        state.toolbarVisibility.set(true)
    }

    private fun setInitialAchievement() {
        parentViewModel?.achievement = parentViewModel?.achievements?.get(0)
        state.selectedAchievementPercentage =
            getString(Strings.screen_yap_for_you_display_text_completed_percentage).format("${parentViewModel?.achievement?.percentage}%")
        state.selectedAchievementTitle = parentViewModel?.achievement?.name ?: ""
    }

    override fun getAchievements() {
        launch {
            state.loading = true
            when (val response = repository.getAchievements()) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.achievements =
                        response.data.data as MutableList<Achievement>
                    achievementDataFactory()
                    adaptor.setList(parentViewModel?.achievements ?: mutableListOf())
                    if (!response.data.data.isNullOrEmpty())
                        setInitialAchievement()

                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    showDialogWithCancel(response.error.message)
                }
            }
        }
    }

    private fun achievementDataFactory() {
        var position = 0
        for (achievement in parentViewModel?.achievements ?: mutableListOf()) {
            achievement.also {
                it.icon = getAchievementIcon(position)
                position++
            }
        }
    }

    override fun getAchievementIcon(position: Int, isWithBadged: Boolean): Int {
        return when (position) {
            0 -> if (!isWithBadged) R.drawable.ic_round_badge_light_purple else R.drawable.ic_badge_light_purple
            1 -> if (!isWithBadged) R.drawable.ic_round_badge_light_blue else R.drawable.ic_badge_dark_blue
            2 -> if (!isWithBadged) R.drawable.ic_round_badge_light_peach else R.drawable.ic_badge_light_peach
            3 -> if (!isWithBadged) R.drawable.ic_y4y_rounded_locked_2 else R.drawable.ic_y4y_rounded_locked_2
            4 -> if (!isWithBadged) R.drawable.ic_y4y_rounded_locked_3 else R.drawable.ic_y4y_rounded_locked_3
            5 -> if (!isWithBadged) R.drawable.ic_y4y_rounded_locked_1 else R.drawable.ic_y4y_rounded_locked_1
            else -> R.drawable.ic_round_badge_dark_grey
        }
    }
}
