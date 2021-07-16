package co.yap.modules.setcardpin.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.setcardpin.interfaces.ISetCardPinWelcomeActivity
import co.yap.modules.setcardpin.viewmodels.SetCardPinActivityViewModel
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.R
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.firebase.FirebaseEvents
import co.yap.yapcore.firebase.FirebaseTagManagerModel
import co.yap.yapcore.firebase.firebaseTagManagerEvent
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator

class SetCardPinWelcomeActivity : BaseBindingActivity<ISetCardPinWelcomeActivity.ViewModel>(),
    INavigator, IFragmentHolder {

    companion object {
        private const val CARD = "card"
        private const val skipWelcome = "skipWelcome"
        fun newIntent(context: Context, card: Card, skipWelcomeScreen: Boolean = false): Intent {
            val intent = Intent(context, SetCardPinWelcomeActivity::class.java)
            intent.putExtra(CARD, card)
            intent.putExtra(skipWelcome, skipWelcomeScreen)
            return intent
        }
    }

    override val viewModel: ISetCardPinWelcomeActivity.ViewModel
        get() = ViewModelProviders.of(this).get(SetCardPinActivityViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_set_card_pin_welcome

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(this@SetCardPinWelcomeActivity, R.id.main_nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseTagManagerEvent(FirebaseTagManagerModel(action = FirebaseEvents.DELIVERY_CONFIRMED.event))
        setupData()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }

    private fun setupData() {
        (intent?.getValue(CARD, ExtraType.PARCEABLE.name) as? Card)?.let {
            viewModel.card = it
        } ?: invalidCardMessage()
        (intent?.getValue(skipWelcome, ExtraType.BOOLEAN.name) as? Boolean)?.let { skip ->
            viewModel.skipWelcome = skip
        }
    }

    private fun invalidCardMessage() {
        showToast("Invalid card serial number")
        finish()
    }
}