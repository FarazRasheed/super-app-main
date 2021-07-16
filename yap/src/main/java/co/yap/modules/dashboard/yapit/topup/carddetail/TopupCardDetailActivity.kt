package co.yap.modules.dashboard.yapit.topup.carddetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.helpers.DateUtils

class TopupCardDetailActivity : BaseBindingActivity<ITopUpCardDetail.ViewModel>() {

    companion object {
        const val key = "card"
        fun getIntent(context: Context, card: TopUpCard): Intent {
            val intent = Intent(context, TopupCardDetailActivity::class.java)
            intent.putExtra(key, card)
            return intent
        }
    }

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_topup_card_detail
    override val viewModel: ITopUpCardDetail.ViewModel
        get() = ViewModelProviders.of(this).get(TopUpCardDetailViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        if (intent.hasExtra(key)) {
            val card: Parcelable = intent.getParcelableExtra(key)
            if (card is TopUpCard) {
                viewModel.state.cardInfo.set(card)
                viewModel.state.cardFormattedExpiry.set(DateUtils.convertTopUpDate(card.expiry))
            }
        }
    }


    private fun setObservers() {
        viewModel.clickEvent.observe(this, clickEventObserver)
        viewModel.state.isCardDeleted.observe(this, Observer {
            when (it) {
                true -> onCardDeleted()
            }
        })
    }

    private fun onCardDeleted() {
        setData()
        finish()
    }

    private fun setData() {
        val intent = Intent()
        intent.putExtra("card", viewModel.state.cardInfo.get())
        intent.putExtra("isCardDeleted", true)
        setResult(Activity.RESULT_OK, intent)
    }

    private val clickEventObserver = Observer<Int> {
        when (it) {
            R.id.IvClose -> finish()
            R.id.tvRemoveCard -> {
                viewModel.state.cardInfo.get()?.id?.let { it ->
                    removeCardAlert(it)
                }
            }
        }
    }

    private fun removeCardAlert(id: String) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.screen_topup_card_details_display_text_remove_card_confirmation))
            .setPositiveButton(
                getString(R.string.screen_topup_card_details_display_text_remove_card_confirmation_remove)
            ) { _, _ ->
                viewModel.onRemoveCard(id)
            }
            .setNegativeButton(
                getString(R.string.screen_profile_settings_logout_display_text_alert_cancel),
                null
            )
            .show()
    }
}