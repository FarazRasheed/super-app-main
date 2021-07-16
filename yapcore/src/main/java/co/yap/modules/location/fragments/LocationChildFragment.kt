package co.yap.modules.location.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.location.viewmodels.LocationChildViewModel
import co.yap.modules.location.viewmodels.LocationViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class LocationChildFragment<V : IBase.ViewModel<*>> : BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is LocationChildViewModel<*>) {
            (viewModel as LocationChildViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(LocationViewModel::class.java)
        }
    }
}