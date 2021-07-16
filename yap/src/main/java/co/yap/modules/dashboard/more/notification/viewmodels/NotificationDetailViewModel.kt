package co.yap.modules.dashboard.more.notification.viewmodels

import android.app.Application
import co.yap.R
import co.yap.modules.dashboard.more.notification.interfaces.INotificationDetail
import co.yap.modules.dashboard.more.notification.states.NotificationDetailState
import co.yap.yapcore.SingleClickEvent

class NotificationDetailViewModel(application: Application) :
    NotificationsBaseViewModel<INotificationDetail.State>(application), INotificationDetail.ViewModel {

    override val state: NotificationDetailState = NotificationDetailState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnDeleteNotification(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        parentViewModel?.state?.toolbarVisibility?.set(true)
        parentViewModel?.state?.toolbarTitle = parentViewModel?.notification?.title ?: ""
        parentViewModel?.state?.rightIcon?.set(-1)
        parentViewModel?.state?.leftIcon?.set(R.drawable.ic_back_arrow_left)
    }
}