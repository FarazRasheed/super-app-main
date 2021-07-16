package co.yap.modules.dashboard.store.household.onboarding.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.dashboard.store.household.onboarding.viewmodels.BaseOnboardingViewModel
import co.yap.modules.dashboard.store.household.onboarding.viewmodels.HouseHoldOnboardingViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase


abstract class BaseOnBoardingFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is BaseOnboardingViewModel<*>) {
            (viewModel as BaseOnboardingViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(HouseHoldOnboardingViewModel::class.java)
        }
    }

    override fun onBackPressed(): Boolean {

        return super.onBackPressed()


    }

}
