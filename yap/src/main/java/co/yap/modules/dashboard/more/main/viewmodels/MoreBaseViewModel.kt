package co.yap.modules.dashboard.more.main.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.main.interfaces.IMore
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class MoreBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IMore.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        val VISIBLE: Int = 0x00000000
        val GONE: Int = 0x00000008
        if (visibility) {
            parentViewModel?.state?.tootlBarVisibility = VISIBLE

        } else {
            parentViewModel?.state?.tootlBarVisibility = GONE

        }
    }

    fun toggleAchievementsBadgeVisibility(visibility: Boolean) {
        parentViewModel?.state?.tootlBarBadgeVisibility = visibility
    }
}