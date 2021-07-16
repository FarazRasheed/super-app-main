package co.yap.modules.dashboard.cards.reordercard.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.modules.dashboard.cards.reordercard.interfaces.IReorderCardSuccess
import co.yap.modules.dashboard.cards.reordercard.viewmodels.ReorderCardSuccessViewModel
import co.yap.yapcore.BR

class ReorderCardSuccessFragment : ReorderCardBaseFragment<IReorderCardSuccess.ViewModel>(),
    IReorderCardSuccess.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_reorder_card_success
    override val viewModel: IReorderCardSuccess.ViewModel
        get() = ViewModelProviders.of(this).get(ReorderCardSuccessViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clickEvent.observe(this, clickObserver)
    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.btnDoneSuccess -> {
                setupActionsIntent()
                activity?.let(FragmentActivity::finish)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
    }

    private fun setupActionsIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra("cardReorder", true)
        activity?.setResult(Activity.RESULT_OK, returnIntent)
    }
}