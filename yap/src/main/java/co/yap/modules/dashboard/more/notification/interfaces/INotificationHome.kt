package co.yap.modules.dashboard.more.notification.interfaces

import androidx.databinding.ObservableField
import co.yap.modules.dashboard.more.notification.adaptor.NotificationsAdaptor
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface INotificationHome {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val adapter: NotificationsAdaptor
        fun handlePressOnView(id: Int)
        fun loadNotifications()
    }

    interface State : IBase.State {
        var hasRecords: ObservableField<Boolean>
    }
}