package co.yap.sendmoney.activities

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.interfaces.ISendMoney
import co.yap.sendmoney.viewmodels.SendMoneyViewModel
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.helpers.showAlertDialogAndExitApp
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator

class SendMoneyHomeActivity : BaseBindingActivity<ISendMoney.ViewModel>(), INavigator,
    IFragmentHolder {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_send_money_home

    override val viewModel: ISendMoney.ViewModel
        get() = ViewModelProviders.of(this).get(SendMoneyViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this@SendMoneyHomeActivity, R.id.send_money_nav_host_fragment)

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.send_money_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            when (requestCode) {
                RequestCodes.REQUEST_TRANSFER_MONEY -> {
                    if (resultCode == Activity.RESULT_OK) {
                        if (data.getBooleanExtra(Constants.MONEY_TRANSFERED, false)) {
                            val intent = Intent()
                            intent.putExtra(Constants.MONEY_TRANSFERED, true)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        } else if (data.getBooleanExtra(Constants.BENEFICIARY_CHANGE, false)) {
                            val intent = Intent()
                            intent.putExtra(Constants.BENEFICIARY_CHANGE, true)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> onBackPressed()
            R.id.tvRightText -> handleCancel()
        }
    }

    private fun handleCancel() {
        showAlertDialogAndExitApp(
            dialogTitle = "Are you sure you want to exit?",
            message = "The information you've entered will be lost.",
            leftButtonText = "Confirm",
            callback = {
                finish()
            },
            titleVisibility = true,
            isTwoButton = true
        )
    }

}