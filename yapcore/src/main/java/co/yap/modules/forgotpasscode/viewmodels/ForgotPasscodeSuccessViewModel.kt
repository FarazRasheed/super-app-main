package co.yap.modules.forgotpasscode.viewmodels

import android.app.Application
import co.yap.modules.forgotpasscode.interfaces.IForgotPasscodeSuccess
import co.yap.modules.forgotpasscode.states.ForgotPasscodeSuccessState
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.managers.SessionManager

class ForgotPasscodeSuccessViewModel(application: Application) :
    BaseViewModel<IForgotPasscodeSuccess.State>(application),
    IForgotPasscodeSuccess.ViewModel {
    override val state: ForgotPasscodeSuccessState =
        ForgotPasscodeSuccessState()
    override val handlePressOnButtonEvent: SingleClickEvent = SingleClickEvent()
    override fun handlePressOnGoToDashboardButton(id: Int) {
        handlePressOnButtonEvent.setValue(id)
    }

    override fun onCreate() {
        if (SessionManager.user?.currentCustomer?.firstName.isNullOrBlank()){
            state.title =
                getString(Strings.screen_passcode_success_heading)
        }else  state.title =
            getString(Strings.screen_passcode_success_display_text_heading_for_yap_core).format(SessionManager.user?.currentCustomer?.firstName)

        state.subTitle = getString(Strings.screen_passcode_success_display_text_sub_heading)
        state.buttonTitle = getString(Strings.screen_passcode_success_button_sign_in)
    }
}