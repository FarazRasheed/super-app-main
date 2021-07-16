package co.yap.modules.dashboard.more.main.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.dashboard.more.main.viewmodels.MoreBaseViewModel
import co.yap.modules.dashboard.more.main.viewmodels.MoreViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class MoreBaseFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is MoreBaseViewModel<*>) {
            (viewModel as MoreBaseViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(MoreViewModel::class.java)
        }
    }

    override fun onBackPressed(): Boolean {

        return super.onBackPressed()


    }

}
