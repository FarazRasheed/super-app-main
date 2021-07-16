package co.yap.modules.location.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.location.viewmodels.LocationSelectionBaseViewModel
import co.yap.modules.location.viewmodels.LocationViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class LocationBaseFragment<V : IBase.ViewModel<*>> :
    BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is LocationSelectionBaseViewModel<*> && activity != null) {
            (viewModel as LocationSelectionBaseViewModel<*>).parentViewModel =
                ViewModelProviders.of(requireActivity())
                    .get(LocationViewModel::class.java)
        }
    }

}