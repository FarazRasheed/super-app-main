package co.yap.modules.onboarding.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.yap.R
import co.yap.modules.onboarding.enums.AccountType
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultActivity
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator


class LiteDashboardActivity : DefaultActivity(), INavigator, IFragmentHolder {

    companion object {
        private val ACCOUNT_TYPE = "account_type"
        fun newIntent(context: Context, accountType: AccountType): Intent {
            val intent = Intent(context, LiteDashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra(ACCOUNT_TYPE, accountType)
            return intent
        }
    }

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this@LiteDashboardActivity, R.id.main_nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lite_dashboard)
    }
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById( R.id.main_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }
}
