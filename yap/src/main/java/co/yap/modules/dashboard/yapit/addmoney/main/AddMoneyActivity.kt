package co.yap.modules.dashboard.yapit.addmoney.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.IBaseNavigator

class AddMoneyActivity : BaseBindingActivity<IAddMoney.ViewModel>(), INavigator,
    IFragmentHolder, IAddMoney.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_add_money
    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this@AddMoneyActivity, R.id.add_money_nav_host_fragment)

    override val viewModel: IAddMoney.ViewModel
        get() = ViewModelProviders.of(this).get(AddMoneyViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                finish()
            }
        }
    }
}
