package co.yap.modules.dashboard.more.notification.viewmodels

import android.app.Application
import co.yap.modules.dashboard.more.notification.interfaces.INotifications
import co.yap.modules.dashboard.more.notification.states.NotificationState
import co.yap.modules.yapnotification.models.Notification
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class NotificationsViewModel(application: Application) :
    BaseViewModel<INotifications.State>(application),
    INotifications.ViewModel {
    override val state: NotificationState = NotificationState()
    override var notification: Notification? = null
    override var clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressButton(id: Int) {
        clickEvent.setValue(id)
    }
}