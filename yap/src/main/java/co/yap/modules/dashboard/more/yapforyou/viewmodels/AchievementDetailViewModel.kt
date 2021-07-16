package co.yap.modules.dashboard.more.yapforyou.viewmodels

import android.app.Application
import co.yap.R
import co.yap.modules.dashboard.more.yapforyou.adapters.AchievementTaskAdaptor
import co.yap.modules.dashboard.more.yapforyou.interfaces.IAchievementDetail
import co.yap.modules.dashboard.more.yapforyou.states.AchievementDetailState
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class AchievementDetailViewModel(application: Application) :
    YapForYouBaseViewModel<IAchievementDetail.State>(application),
    IAchievementDetail.ViewModel {

    override val state: AchievementDetailState = AchievementDetailState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override val adapter: AchievementTaskAdaptor = AchievementTaskAdaptor(mutableListOf())
    override fun handlePressOnButton(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        setupToolbar()
        parentViewModel?.achievement?.features?.let {
            adapter.setList(it)
        }
    }

    private fun setupToolbar() {
        setToolBarTitle(getString(Strings.screen_yap_for_you_display_text_title))
        setLeftIcon(R.drawable.ic_close_primary)
        setLeftIconVisibility(true)
        toggleToolBarVisibility(true)
    }
}