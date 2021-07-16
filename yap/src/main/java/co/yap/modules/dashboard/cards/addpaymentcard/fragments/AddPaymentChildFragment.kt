package co.yap.modules.dashboard.cards.addpaymentcard.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.modules.dashboard.cards.addpaymentcard.viewmodels.AddPaymentCardViewModel
import co.yap.modules.dashboard.cards.addpaymentcard.viewmodels.AddPaymentChildViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase


abstract class AddPaymentChildFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is AddPaymentChildViewModel<*>) {
            (viewModel as AddPaymentChildViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(AddPaymentCardViewModel::class.java)
        }
    }

    override fun onBackPressed(): Boolean {

            return super.onBackPressed()
    }

}