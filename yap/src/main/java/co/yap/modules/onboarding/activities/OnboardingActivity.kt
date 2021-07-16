package co.yap.modules.onboarding.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.onboarding.enums.AccountType
import co.yap.modules.onboarding.interfaces.IOnboarding
import co.yap.modules.onboarding.viewmodels.OnboardingViewModel
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator

class OnboardingActivity : BaseBindingActivity<IOnboarding.ViewModel>(), INavigator,
    IFragmentHolder {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_onboarding_navigation

    override val viewModel: IOnboarding.ViewModel
        get() = ViewModelProviders.of(this).get(OnboardingViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this@OnboardingActivity, R.id.my_nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onboardingData.accountType = getAccountType()

        viewModel.backButtonPressEvent.observe(this, backButtonObserver)
    }

    override fun onDestroy() {
        viewModel.backButtonPressEvent.removeObservers(this)
        super.onDestroy()
    }

    private fun getAccountType(): AccountType {
        if (intent.getSerializableExtra(getString(R.string.arg_account_type)) == null) {
            return AccountType.B2C_ACCOUNT
        } else {
            return intent.getSerializableExtra(getString(R.string.arg_account_type)) as AccountType
        }
    }

    private val backButtonObserver = Observer<Boolean> { onBackPressed() }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }
}