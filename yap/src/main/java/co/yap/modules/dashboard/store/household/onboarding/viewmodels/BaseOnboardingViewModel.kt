package co.yap.modules.dashboard.store.household.onboarding.viewmodels

import android.app.Application
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IBaseOnboarding
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class BaseOnboardingViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IBaseOnboarding.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.tootlBarTitle = title
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
}