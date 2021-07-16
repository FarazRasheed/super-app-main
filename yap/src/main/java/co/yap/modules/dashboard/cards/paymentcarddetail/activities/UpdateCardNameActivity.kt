package co.yap.modules.dashboard.cards.paymentcarddetail.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.interfaces.IUpdateCardName
import co.yap.modules.dashboard.cards.paymentcarddetail.viewmodels.UpdateCardNameViewModel
import co.yap.modules.others.helper.Constants
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseBindingActivity
import kotlinx.android.synthetic.main.activity_update_card_name.*


class UpdateCardNameActivity : BaseBindingActivity<IUpdateCardName.ViewModel>(),
    IUpdateCardName.View {

    companion object {
        private const val CARD = "card"
        fun newIntent(context: Context, card: Card): Intent {
            val intent = Intent(context, UpdateCardNameActivity::class.java)
            intent.putExtra(CARD, card)
            return intent
        }
    }

    override val viewModel: IUpdateCardName.ViewModel
        get() = ViewModelProviders.of(this).get(UpdateCardNameViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_update_card_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        setupView()
    }


    override fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.ivCross -> finish()
                R.id.btnConfirm -> viewModel.updateCardName()
                viewModel.EVENT_UPDATE_CARD_NAME -> {
                    showToast("Card name successfully updated!")
                    val returnIntent = Intent()
                    returnIntent.putExtra("name", viewModel.state.cardName)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        })
    }

    private fun setupView() {
        viewModel.card = intent.getParcelableExtra(CARD)
        etName.append(viewModel.card.cardName?.trim())
        etName.requestFocus()

        if (Constants.CARD_TYPE_DEBIT == viewModel.card.cardType) {
            tvCardType.text = "Primary card"
        } else {
            if (viewModel.card.physical) {
                tvCardType.text = Constants.TEXT_SPARE_CARD_PHYSICAL
            } else {
                tvCardType.text = Constants.TEXT_SPARE_CARD_VIRTUAL
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
    }

}