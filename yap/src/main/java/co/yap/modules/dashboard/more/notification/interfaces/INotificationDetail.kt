package co.yap.modules.dashboard.more.notification.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface INotificationDetail {

    interface State : IBase.State

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnDeleteNotification(id: Int)
    }
}