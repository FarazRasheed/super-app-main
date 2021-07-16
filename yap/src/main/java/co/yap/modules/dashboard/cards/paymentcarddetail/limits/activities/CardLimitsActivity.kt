package co.yap.modules.dashboard.cards.paymentcarddetail.limits.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.limits.interfaces.ICardLimits
import co.yap.modules.dashboard.cards.paymentcarddetail.limits.viewmodel.CardLimitViewModel
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseBindingActivity

class CardLimitsActivity : BaseBindingActivity<ICardLimits.ViewModel>(),
    ICardLimits.View {

    companion object {
        const val key = "card"
        fun getIntent(context: Context, card: Card): Intent {
            val intent = Intent(context, CardLimitsActivity::class.java)
            intent.putExtra(key, card)
            return intent
        }
    }

    override val viewModel: ICardLimits.ViewModel
        get() = ViewModelProviders.of(this).get(CardLimitViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_card_limits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        val card: Card = intent.getParcelableExtra(key)
        viewModel.state.card.set(card)
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.tbBtnBack -> {
                    setData()
                    finish()
                }
            }
        })
    }

    override fun onBackPressed() {
        setData()
        super.onBackPressed()
    }

    private fun setData() {
        val returnIntent = Intent()
        returnIntent.putExtra("card", viewModel.state.card.get())
        setResult(Activity.RESULT_OK, returnIntent)
    }
}