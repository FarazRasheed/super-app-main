package co.yap.modules.dashboard.more.notification.states

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.more.notification.interfaces.INotificationHome
import co.yap.yapcore.BaseState

class NotificationsHomeState : BaseState(), INotificationHome.State {
    override var hasRecords: ObservableField<Boolean> = ObservableField()
}
