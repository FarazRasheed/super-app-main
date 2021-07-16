package co.yap.modules.setcardpin.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.setcardpin.viewmodels.SetCardPinActivityViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class SetPinChildFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is SetPinChildViewModel<*>) {
            activity?.let {
                (viewModel as SetPinChildViewModel<*>).parentViewModel =
                    ViewModelProviders.of(it).get(SetCardPinActivityViewModel::class.java)
            }
        }
    }
}