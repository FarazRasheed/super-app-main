package co.yap.modules.dashboard.more.changepasscode.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.modules.dashboard.more.changepasscode.interfaces.IChangePassCode
import co.yap.modules.dashboard.more.changepasscode.models.PassCodeData
import co.yap.modules.dashboard.more.changepasscode.viewmodels.ChangePassCodeViewModel
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.IBaseNavigator

class ChangePasscodeActivity : BaseBindingActivity<IChangePassCode.ViewModel>(), IFragmentHolder,
    INavigator,IChangePassCode.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_change_passcode
    override val passCodeData: PassCodeData = PassCodeData()
    override val viewModel: IChangePassCode.ViewModel
        get() = ViewModelProviders.of(this).get(ChangePassCodeViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(
            this@ChangePasscodeActivity,
            R.id.change_passcode_nav_host_fragment
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.tbIvClose -> {
                    onBackPressed()
                }
            }
        })
    }

    override fun onBackPressed() {
//        val fragment =
//            supportFragmentManager.findFragmentById(R.id.change_passcode_nav_host_fragment)
//        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
//        }
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }
    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                super.onBackPressed()
            }
        }
    }

}