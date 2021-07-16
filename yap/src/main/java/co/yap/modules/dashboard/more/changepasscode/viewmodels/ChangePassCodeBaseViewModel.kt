package co.yap.modules.dashboard.more.changepasscode.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.changepasscode.interfaces.IChangePassCode
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class ChangePassCodeBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    var parentViewModel: IChangePassCode.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolbarVisibility?.set(visibility)
    }
}