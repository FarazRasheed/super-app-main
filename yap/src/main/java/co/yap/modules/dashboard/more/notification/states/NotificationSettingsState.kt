package co.yap.modules.dashboard.more.notification.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.more.notification.interfaces.INotificationSettings
import co.yap.yapcore.BaseState

class NotificationSettingsState : BaseState(), INotificationSettings.State {
    @get:Bindable
    override var inAppNotificationsAllowed: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.inAppNotificationsAllowed)
        }
    @get:Bindable
    override var smsNotificationsAllowed: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.smsNotificationsAllowed)
        }
    @get:Bindable
    override var emailNotificationsAllowed: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailNotificationsAllowed)
        }
}