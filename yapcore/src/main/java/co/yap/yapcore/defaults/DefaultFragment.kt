package co.yap.yapcore.defaults

import androidx.lifecycle.ViewModelProviders
import co.yap.yapcore.BaseFragment

open class DefaultFragment : BaseFragment<IDefault.ViewModel>() {
    override val viewModel: IDefault.ViewModel
        get() = ViewModelProviders.of(this).get(DefaultViewModel::class.java)
}