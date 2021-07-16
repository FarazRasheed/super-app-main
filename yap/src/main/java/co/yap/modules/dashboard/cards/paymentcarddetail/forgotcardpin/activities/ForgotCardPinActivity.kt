package co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.interfaces.IForgotCardPin
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.viewmodels.ForgotCardPinViewModel
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator

class ForgotCardPinActivity : BaseBindingActivity<IForgotCardPin.ViewModel>(), INavigator,
    IFragmentHolder {
    companion object {
        fun newIntent(context: Context, cardSerialNumber: String): Intent {
            val intent = Intent(context, ForgotCardPinActivity::class.java)
            intent.putExtra(Constants.CARD_SERIAL_NUMBER, cardSerialNumber)
            return intent
        }
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_forgot_card_pin

    override val viewModel: IForgotCardPin.ViewModel
        get() = ViewModelProviders.of(this).get(ForgotCardPinViewModel::class.java)
    override val navigator: IBaseNavigator
        get() = DefaultNavigator(
            this@ForgotCardPinActivity,
            R.id.main_forgot_card_pin_nav_host_fragment
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.backButtonPressEvent.observe(this, Observer {
            onBackPressed()
        })
    }

    fun getCardSerialNumber(): String {
        return intent.getStringExtra(Constants.CARD_SERIAL_NUMBER)
    }

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.main_forgot_card_pin_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }

}