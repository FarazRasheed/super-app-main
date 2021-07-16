package co.yap.household.onboard.onboarding.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.household.onboard.onboarding.interfaces.IHouseHoldCreatePassCode
import co.yap.household.onboard.onboarding.states.HouseHoldCreatePassCodeState
import co.yap.household.onboard.onboarding.main.viewmodels.OnboardingChildViewModel
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.CreatePassCodeRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.StringUtils

class HouseHoldCreatePassCodeViewModel(application: Application) :
    OnboardingChildViewModel<IHouseHoldCreatePassCode.State>(application),
    IHouseHoldCreatePassCode.ViewModel, IRepositoryHolder<CustomersRepository> {
    override val repository: CustomersRepository = CustomersRepository
    override val state: HouseHoldCreatePassCodeState = HouseHoldCreatePassCodeState()
    override val clickEvent: SingleClickEvent? = SingleClickEvent()
    override var onPasscodeSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun handlePressOnCreatePasscodeButton(id: Int) {
        if (validateAggressively()) {
            clickEvent?.setValue(id)
        }
    }

    override fun onResume() {
        super.onResume()
        setProgress(50)
    }

    private fun validateAggressively(): Boolean {
        val isSame = StringUtils.hasAllSameChars(state.passcode)
        val isSequenced = StringUtils.isSequenced(state.passcode)
        if (isSequenced) state.dialerError =
            getString(Strings.screen_create_passcode_display_text_error_sequence)
        if (isSame) state.dialerError =
            getString(Strings.screen_create_passcode_display_text_error_same_digits)
        return !isSame && !isSequenced
    }

    override fun createPassCodeRequest() {
        launch {
            state.loading = true
            when (val response =
                repository.createHouseholdPasscode(CreatePassCodeRequest(passcode = state.passcode))) {
                is RetroApiResponse.Success -> {
                    onPasscodeSuccess.value = true
                }
                is RetroApiResponse.Error -> {
                    onPasscodeSuccess.value = false
                    state.toast = response.error.message
                }
            }
            state.loading = false
        }
    }
}