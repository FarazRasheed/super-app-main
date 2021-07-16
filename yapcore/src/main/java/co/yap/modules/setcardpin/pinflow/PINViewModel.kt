package co.yap.modules.setcardpin.pinflow

import android.app.Application
import co.yap.modules.setcardpin.activities.SetPinChildViewModel
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.requestdtos.ChangeCardPinRequest
import co.yap.networking.cards.requestdtos.CreateCardPinRequest
import co.yap.networking.cards.requestdtos.ForgotCardPin
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.StringUtils

class PINViewModel(application: Application) :
    SetPinChildViewModel<IPin.State>(application),
    IPin.ViewModel, IRepositoryHolder<CardsRepository> {
    override val repository: CardsRepository = CardsRepository
    override val forgotPasscodeclickEvent: SingleClickEvent = SingleClickEvent()
    override var mobileNumber: String = ""
    override fun setChangeCardPinFragmentData() {
        state.titleSetPin = getString(Strings.screen_current_card_pin_display_text_heading_pin)
        state.buttonTitle = getString(Strings.screen_current_card_pin_display_button_next)
    }

    override fun setNewCardPinFragmentdata() {
        state.titleSetPin = getString(Strings.screen_create_card_pin_display_text_heading)
        state.buttonTitle = getString(Strings.screen_create_card_pin_display_button_create_pin)
    }

    override fun setCardPinFragmentData() {
        state.titleSetPin = getString(Strings.screen_set_card_pin_display_text_title)
        state.buttonTitle = getString(Strings.screen_set_card_pin_button_create_pin)
    }

    override fun setConfirmNewCardPinFragmentData() {
        state.titleSetPin = getString(Strings.screen_confirm_card_pin_display_text_heading)
        state.buttonTitle = getString(Strings.screen_confirm_card_pin_display_button_confirm_pin)
    }

    override var pincode: String = ""
    override val state: PinState = PinState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override var errorEvent: SingleClickEvent = SingleClickEvent()


    override fun handlePressOnNextButton(id: Int) {
        if (validateAggressively()) {
            clickEvent.setValue(id)
        }
    }

    override fun handlePressOnForgotPasscodeButton(id: Int) {
        //forgotPasscodeclickEvent.postValue(id)
    }

    override fun setCardPin(cardSerialNumber: String) {
        launch {
            state.loading = true
            when (val response = repository.createCardPin(
                CreateCardPinRequest(state.pincode),
                cardSerialNumber
            )) {
                is RetroApiResponse.Success -> {
                    kotlinx.coroutines.delay(600)
                    clickEvent.setValue(EVENT_SET_CARD_PIN_SUCCESS)
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                }
            }
            state.loading = false
        }
    }

    override fun changeCardPinRequest(
        oldPin: String,
        newPin: String,
        confirmPin: String,
        cardSerialNumber: String,
        success: () -> Unit
    ) {
        launch {
            state.loading = true
            when (val response = repository.changeCardPinRequest(
                ChangeCardPinRequest(
                    oldPin,
                    newPin,
                    confirmPin, cardSerialNumber
                )
            )) {
                is RetroApiResponse.Success -> {
                    success()
                }
                is RetroApiResponse.Error ->
                    state.dialerError = response.error.message
            }
            state.loading = false
        }

    }

    override fun forgotCardPinRequest(cardSerialNumber: String, newPin: String, token: String) {
        launch {
            state.loading = true
            when (val response = repository.forgotCardPin(
                cardSerialNumber,
                ForgotCardPin(newPin, token)
            )) {
                is RetroApiResponse.Success -> {
                    clickEvent.postValue(Constants.FORGOT_CARD_PIN_NAVIGATION)
                }
                is RetroApiResponse.Error ->
                    state.dialerError = response.error.message
            }
            state.loading = false
        }
    }

    private fun validateAggressively(): Boolean {
        val isSame = StringUtils.hasAllSameChars(state.pincode)
        val isSequenced = StringUtils.isSequenced(state.pincode)
        if (isSequenced) state.dialerError =
            getString(Strings.screen_confirm_card_pin_display_text_error_sequence)
        if (isSame) state.dialerError =
            getString(Strings.screen_confirm_card_pin_display_text_error_same_digits)
        return !isSame && !isSequenced
    }
}