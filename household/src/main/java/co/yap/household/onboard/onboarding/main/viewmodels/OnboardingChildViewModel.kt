package co.yap.household.onboard.onboarding.main.viewmodels

import android.app.Application
import co.yap.household.onboard.onboarding.main.interfaces.IOnboarding
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase


abstract class OnboardingChildViewModel<S : IBase.State>(application: Application) : BaseViewModel<S>(application) {

    var parentViewModel: IOnboarding.ViewModel? = null

    fun setProgress(percent: Int) {
        parentViewModel?.state?.currentProgress = percent
    }

    fun updateBackground(color : Int) {
        parentViewModel?.state?.currentBackground = color
    }
}