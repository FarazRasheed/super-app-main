package co.yap.modules.location.viewmodels

import android.app.Application
import co.yap.modules.location.interfaces.ILocation
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class LocationSelectionBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: ILocation.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolbarVisibility?.set(visibility)
    }
}