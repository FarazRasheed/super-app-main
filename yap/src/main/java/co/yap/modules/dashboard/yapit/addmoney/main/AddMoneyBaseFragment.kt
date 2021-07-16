package co.yap.modules.dashboard.yapit.addmoney.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class AddMoneyBaseFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is AddMoneyBaseViewModel<*>) {
            (viewModel as AddMoneyBaseViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(AddMoneyViewModel::class.java)
        }
    }
}
