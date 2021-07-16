package co.yap.modules.dashboard.more.notification.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.notification.interfaces.INotifications
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class NotificationsBaseViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {

    var parentViewModel: INotifications.ViewModel? = null

    fun setToolBarTitle(title: String) {
        parentViewModel?.state?.toolbarTitle = title
    }

    fun toggleToolBarVisibility(visibility: Boolean) {
        parentViewModel?.state?.toolbarVisibility?.set(visibility)
    }
}