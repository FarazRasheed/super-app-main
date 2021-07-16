package co.yap.modules.onboarding.viewmodels

import android.app.Application
import co.yap.modules.onboarding.interfaces.IOnboarding
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class OnboardingChildViewModel<S : IBase.State>(application: Application) : BaseViewModel<S>(application) {

    var parentViewModel: IOnboarding.ViewModel? = null

    fun setProgress(percent: Int) {
        parentViewModel?.state?.currentProgress = percent
    }
}