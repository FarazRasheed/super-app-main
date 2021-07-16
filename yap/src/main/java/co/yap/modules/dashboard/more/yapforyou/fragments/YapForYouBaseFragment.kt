package co.yap.modules.dashboard.more.yapforyou.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.dashboard.more.yapforyou.viewmodels.YapForYouBaseViewModel
import co.yap.modules.dashboard.more.yapforyou.viewmodels.YapForYouMainViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class YapForYouBaseFragment<V : IBase.ViewModel<*>> :
    BaseBindingFragment<V>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is YapForYouBaseViewModel<*>) {
            (viewModel as YapForYouBaseViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(YapForYouMainViewModel::class.java)
        }
    }

}