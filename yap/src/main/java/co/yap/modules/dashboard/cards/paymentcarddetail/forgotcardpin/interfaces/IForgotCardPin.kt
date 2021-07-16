package co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface IForgotCardPin {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnBackButton()
        val backButtonPressEvent: SingleClickEvent
    }

    interface State : IBase.State {
        var toolBarVisibility: Boolean
        var toolBarTitle: String
        var currentScreen: String
    }
}