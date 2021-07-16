package co.yap.modules.dashboard.unverifiedemail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator
import kotlinx.android.synthetic.main.activity_add_payment_cards.*

class UnVerifiedEmailActivity : BaseBindingActivity<IUnverifiedEmail.ViewModel>(), INavigator,
    IFragmentHolder {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, UnVerifiedEmailActivity::class.java)
            return intent
        }

        var onBackPressCheck: Boolean = true
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_unverified_email

    override val viewModel: IUnverifiedEmail.ViewModel
        get() = ViewModelProviders.of(this).get(UnverifiedEmailViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(
            this@UnVerifiedEmailActivity,
            R.id.unverified_email_nav_host_fragment
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.backButtonPressEvent.observe(this, backButtonObserver)
    }

    override fun onDestroy() {
        viewModel.backButtonPressEvent.removeObservers(this)
        super.onDestroy()
    }

    private val backButtonObserver = Observer<Boolean> { onBackPressed() }

    fun hideToolbar() {
        toolbar.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.unverified_email_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            if (onBackPressCheck) {
                super.onBackPressed()
            }
        }
    }
}