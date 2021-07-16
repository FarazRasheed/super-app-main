package co.yap.modules.dashboard.more.notification.viewmodels

import android.app.Application
import android.widget.CompoundButton
import co.yap.R
import co.yap.modules.dashboard.more.notification.interfaces.INotificationSettings
import co.yap.modules.dashboard.more.notification.states.NotificationSettingsState
import co.yap.translation.Strings

class NotificationSettingsViewModel(application: Application) :
    NotificationsBaseViewModel<INotificationSettings.State>(application = application),
    INotificationSettings.ViewModel {
    override val state: NotificationSettingsState = NotificationSettingsState()

    override fun onCreate() {
        super.onCreate()
        parentViewModel?.state?.toolbarTitle =
            getString(Strings.screen_notification_settings_display_text_toolbar_title)
        parentViewModel?.state?.leftIcon?.set(R.drawable.ic_close_primery)
        parentViewModel?.state?.rightIcon?.set(-1)
    }

    fun onSwitchChanged(switch: CompoundButton, isChecked: Boolean) {
        if (switch.isPressed) {
            when (switch.id) {
                R.id.swWithdrawal -> {
                    //updateAllowAtm()
                }
                R.id.swOnlineTra -> {
//                    updateOnlineTransaction()
                }
                R.id.swAbroad -> {
//                    updateAbroadPayment()
                }
                R.id.swRetail -> {
//                    updateRetailPayment()
                }
            }
        }
    }
}