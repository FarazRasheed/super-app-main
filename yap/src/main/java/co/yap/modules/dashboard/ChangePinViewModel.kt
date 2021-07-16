package co.yap.modules.dashboard

import android.app.Application
import co.yap.modules.others.helper.Constants
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.requestdtos.ChangeCardPinRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.R
import co.yap.yapcore.SingleClickEvent

class ChangePinViewModel(application: Application) : BaseViewModel<IChangePin.State>(application),
    IChangePin.ViewModel, IRepositoryHolder<CardsRepository> {
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val repository: CardsRepository = CardsRepository
    override val state: IChangePin.State = ChangePinState(application)


    override fun handlePressView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun changePinRequest(request: ChangeCardPinRequest, success: () -> Unit) {
        launch {
            state.loading = true
            when (val response = repository.changeCardPinRequest(request)) {
                is RetroApiResponse.Success -> success()
                is RetroApiResponse.Error ->
                    when (response.error.actualCode) {
                        Constants.INVALID_OLD_PIN -> {
                            showErrorOldPin()
                        }
                        else -> {
                            showToast(response.error.message)
                            hideErrorOldPin()
                        }

                    }
            }
            state.loading = false
        }
    }

    private fun showErrorOldPin() {
        state.pinFieldBackground.set(
            context.resources.getDrawable(
                co.yap.R.drawable.bg_edit_text_red_under_line_card_change_pin,
                null
            )
        )
        state.pinFieldErrorIcon.set(context.getDrawable(co.yap.R.drawable.ic_error))
        state.errorMessageForPrevious.set(context.getString(R.string.screen_change_card_pin_old_pin_error_message))
    }

    private fun hideErrorOldPin() {
        state.pinFieldBackground.set(
            context.resources.getDrawable(
                R.drawable.bg_edit_text_under_line_card_change_pin,
                null
            )
        )
        state.pinFieldErrorIcon.set(null)
        state.errorMessageForPrevious.set("")
    }

    override fun onCreate() {
        super.onResume()
        initiateFieldsWithBackgrounds()

    }

    private fun initiateFieldsWithBackgrounds() {
        state.pinFieldBackground.set(
            context.resources.getDrawable(
                R.drawable.bg_edit_text_under_line_card_change_pin,
                null
            )
        )
        state.pinFieldBackgroundForNew.set(
            context.resources.getDrawable(
                R.drawable.bg_edit_text_under_line_card_change_pin,
                null
            )
        )
        state.pinFieldBackgroundForConfirmNew.set(
            context.resources.getDrawable(
                R.drawable.bg_edit_text_under_line_card_change_pin,
                null
            )
        )
    }
}