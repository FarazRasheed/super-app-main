package co.yap.modules.onboarding.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ILiteDashboard {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val EVENT_LOGOUT_SUCCESS: Int
            get() = 1
        val EVENT_PRESS_COMPLETE_VERIFICATION: Int
            get() = 2
        val EVENT_PRESS_SET_CARD_PIN: Int
            get() = 3
        val EVENT_GET_ACCOUNT_INFO_SUCCESS: Int
            get() = 4
        val EVENT_GET_DEBIT_CARDS_SUCCESS: Int
            get() = 5

        val clickEvent: SingleClickEvent
        fun handlePressOnLogout()
        fun getDebitCards()
        fun handlePressOnCompleteVerification()
        fun handlePressOnsetCardPin()
        fun logout()
    }

    interface State : IBase.State {
        var name: String
        var email: String
    }
}