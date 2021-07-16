package co.yap.modules.kyc.viewmodels

import android.app.Application
import co.yap.modules.onboarding.interfaces.IInformationError
import co.yap.modules.onboarding.states.InformationErrorState
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class InformationErrorViewModel(application: Application) :
    KYCChildViewModel<IInformationError.State>(application),
    IInformationError.ViewModel {
    override val state: InformationErrorState = InformationErrorState(application)
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var countryName: String = " "

    override fun onCreate() {
        super.onCreate()
        state.buttonTitle = getString(Strings.screen_kyc_information_error_button_go_to_dashboard)
    }

    override fun onResume() {
        super.onResume()
        parentViewModel?.identity?.let {
            if (it.nationality.equals("USA", true) || it.isoCountryCode2Digit.equals("US", true))
                state.isUSACitizen.set(true)
        }
    }

    override fun handlePressOnGoToDashboard() {
        clickEvent.call()
    }
}