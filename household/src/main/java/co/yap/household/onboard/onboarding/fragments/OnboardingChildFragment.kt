package co.yap.household.onboard.onboarding.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.household.onboard.onboarding.main.viewmodels.OnboardingChildViewModel
import co.yap.household.onboard.onboarding.main.viewmodels.OnboardingHouseHoldViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase


abstract class OnboardingChildFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is OnboardingChildViewModel<*>) {
            (viewModel as OnboardingChildViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(OnboardingHouseHoldViewModel::class.java)
        }
    }
}