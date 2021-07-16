package co.yap.modules.dashboard.more.yapforyou.interfaces

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import co.yap.networking.transactions.responsedtos.achievement.Achievement
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IYapForYouMain {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        var achievement: Achievement?
        var selectedPosition: Int
        var achievements: MutableList<Achievement>
        fun handlePressButton(id: Int)
    }

    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
        var leftIcon: ObservableField<Int>
        var leftIconVisibility : ObservableBoolean
    }
}