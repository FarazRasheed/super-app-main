package co.yap.modules.dashboard.more.changepasscode.interfaces

import androidx.databinding.ObservableBoolean
import co.yap.modules.dashboard.more.changepasscode.models.PassCodeData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IChangePassCode {
    interface View : IBase.View<ViewModel> {
        val passCodeData: PassCodeData
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnView(id: Int)
        val clickEvent: SingleClickEvent

    }

    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
        var rightIcon: ObservableBoolean
        var leftIcon: ObservableBoolean
        var toolBarTitle: String?
    }
}