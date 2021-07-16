package co.yap.modules.forgotpasscode.activities

import android.os.Bundle
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.R
import co.yap.yapcore.defaults.DefaultActivity
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.IBaseNavigator

class ForgotPasscodeActivity : DefaultActivity(), INavigator, IFragmentHolder {
    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this@ForgotPasscodeActivity, R.id.main_nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_passcode)
    }
}