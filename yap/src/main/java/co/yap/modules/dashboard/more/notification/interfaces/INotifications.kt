package co.yap.modules.dashboard.more.notification.interfaces

import android.database.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import co.yap.modules.yapnotification.models.Notification
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface INotifications {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        var notification: Notification?
        var clickEvent: SingleClickEvent
        fun handlePressButton(id: Int)
    }

    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
        var rightIcon: ObservableField<Int>
        var leftIcon: ObservableField<Int>
    }
}