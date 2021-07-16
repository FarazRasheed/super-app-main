package co.yap.modules.dashboard.more.notification.viewmodels

import android.app.Application
import co.yap.R
import co.yap.modules.dashboard.more.notification.adaptor.NotificationsAdaptor
import co.yap.modules.dashboard.more.notification.interfaces.INotificationHome
import co.yap.modules.dashboard.more.notification.states.NotificationsHomeState
import co.yap.modules.yapnotification.models.Notification
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import com.leanplum.Leanplum

class NotificationsHomeViewModel(application: Application) :
    NotificationsBaseViewModel<INotificationHome.State>(application),
    INotificationHome.ViewModel {
    override val state: NotificationsHomeState = NotificationsHomeState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val adapter: NotificationsAdaptor = NotificationsAdaptor(mutableListOf())

    override fun onCreate() {
        super.onCreate()
        parentViewModel?.state?.toolbarVisibility?.set(true)
        parentViewModel?.state?.toolbarTitle =
            getString(Strings.screen_notification_listing_display_text_toolbar_title)
        parentViewModel?.state?.rightIcon?.set(R.drawable.ic_settings)
        parentViewModel?.state?.leftIcon?.set(R.drawable.ic_back_arrow_left)
    }

    override fun loadNotifications() {
        launch {
            state.loading = true
            val notifications = ArrayList<Notification>()
            Leanplum.getInbox().allMessages()
            Leanplum.getInbox().allMessages().forEach {
                notifications.add(
                    Notification(
                        it.title,
                        if (null != it.data && it.data.has("description")) it.data.getString("description") else "",
                        "",
                        "",
                        it.imageFilePath,
                        "",
                        it.deliveryTimestamp.toString(),
                        it.isRead,
                        it.messageId
                    )
                )
            }
            state.hasRecords.set(!notifications.isNullOrEmpty())
            adapter.setList(notifications)
            state.loading = false
        }
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }
}