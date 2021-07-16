package co.yap.modules.dashboard.cards.reordercard.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.reordercard.interfaces.IReorderCard
import co.yap.modules.dashboard.cards.reordercard.viewmodels.ReorderCardViewModel
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.interfaces.IBaseNavigator

class ReorderCardActivity : BaseBindingActivity<IReorderCard.ViewModel>(),
    INavigator,
    IFragmentHolder {

    companion object {
        const val CARD = "card"
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_reorder_card

    override val viewModel: IReorderCard.ViewModel
        get() = ViewModelProviders.of(this).get(ReorderCardViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(
            this@ReorderCardActivity,
            R.id.main_reorder_card_nav_host_fragment
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clickEvent.observe(this, clickObserver)
        if (intent != null && intent.hasExtra(CARD)) {
            val card: Card? = intent.getParcelableExtra(CARD)
            if (card != null) {
                viewModel.card = card
            } else {
                showToast("Invalid Card")
                finish()
            }
        } else {
            showToast("Invalid Card")
            finish()
        }
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    private val clickObserver = Observer<Int> {
        finish()
    }
}