package co.yap.modules.dashboard.more.notification.states

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import co.yap.modules.dashboard.more.notification.interfaces.INotifications
import co.yap.yapcore.BaseState

class NotificationState : BaseState(), INotifications.State {
    override var rightIcon: ObservableField<Int> = ObservableField(-1)
    override var leftIcon: ObservableField<Int> = ObservableField(-1)
    override var toolbarVisibility: ObservableBoolean = ObservableBoolean(false)
}