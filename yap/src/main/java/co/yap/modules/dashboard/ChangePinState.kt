package co.yap.modules.dashboard

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.R
import co.yap.yapcore.BaseState

class ChangePinState(application: Application) : BaseState(), IChangePin.State {

    val context = application
    override var pinFieldBackground: ObservableField<Drawable?> = ObservableField()
    override var pinFieldErrorIcon: ObservableField<Drawable?> = ObservableField()
    override var pinFieldBackgroundForNew: ObservableField<Drawable?> = ObservableField()
    override var pinFieldErrorIconForNew: ObservableField<Drawable?> = ObservableField()
    override var pinFieldBackgroundForConfirmNew: ObservableField<Drawable?> = ObservableField()
    override var pinFieldErrorIconConfirmNew: ObservableField<Drawable?> = ObservableField()
    override var errorMessageForPrevious: ObservableField<String> = ObservableField()
    override var errorMessageForNewPin: ObservableField<String> = ObservableField()
    override var errorMessageForNewConfiem: ObservableField<String> = ObservableField()
    override var isButtonEnabled: ObservableField<Boolean> = ObservableField()

    @get:Bindable
    override var oldPin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.oldPin)
            if (oldPin.isNotEmpty()) {
                hideErrorOldPin()
            }
            oldPinEmptyChecker()
            checkButtonValidity()
        }

    @get:Bindable
    override var newPin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.oldPin)
            checkButtonValidity()
            compareCodes()
            newPinEmptyChecker()
            confirmPinEmptyChecker()
        }

    @get:Bindable
    override var confirmNewPin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.oldPin)
            compareCodes()
            checkButtonValidity()
            confirmPinEmptyChecker()
            newPinEmptyChecker()
        }


    private fun compareCodes() {

        if (confirmNewPin == newPin) {
            hideErrorsOfNewCodes()

        } else if (confirmNewPin.isNotEmpty()) {
            showErrorOnConfirmPinField()
        }
    }


    private fun checkButtonValidity() {
        if ((confirmNewPin == newPin) && newPin.isNotEmpty() && oldPin.isNotEmpty() && newPin.length == 4 && oldPin.length == 4)
            isButtonEnabled.set(true)
        else
            isButtonEnabled.set(false)

    }

   private fun hideErrorsOfNewCodes() {
        pinFieldBackgroundForNew.set(
            context.resources.getDrawable(
                R.drawable.bg_edit_text_under_line_card_change_pin,
                null
            )
        )
        pinFieldErrorIconForNew.set(null)
        pinFieldBackgroundForConfirmNew.set(
            context.resources.getDrawable(
                R.drawable.bg_edit_text_under_line_card_change_pin,
                null
            )
        )
        pinFieldErrorIconConfirmNew.set(null)
        errorMessageForNewConfiem.set("")
    }

   private fun showErrorOnConfirmPinField() {
        pinFieldBackgroundForConfirmNew.set(
            context.resources.getDrawable(
                R.drawable.bg_edit_text_red_under_line_card_change_pin,
                null
            )
        )
        errorMessageForNewConfiem.set(context.getString(R.string.screen_change_card_pin_codes_unmatch_error))
        pinFieldErrorIconConfirmNew.set(context.getDrawable(R.drawable.ic_error))
    }

    private fun hideErrorOldPin() {
        pinFieldBackground.set(
            context.resources.getDrawable(
                co.yap.yapcore.R.drawable.bg_edit_text_under_line_card_change_pin,
                null
            )
        )
        pinFieldErrorIcon.set(null)
        errorMessageForPrevious.set("")
    }

    private fun confirmPinEmptyChecker()
    {
        if(confirmNewPin.isNotEmpty()&& errorMessageForNewConfiem.get().toString().isEmpty())
        {
            pinFieldBackgroundForConfirmNew.set(
                context.resources.getDrawable(
                    co.yap.yapcore.R.drawable.bg_edit_text_field_not_empty_card_change_pin,
                    null
                )
            )
            pinFieldErrorIconConfirmNew.set(null)
            errorMessageForNewConfiem.set("")
        }
       else if(confirmNewPin.isEmpty())
        {
            pinFieldBackgroundForConfirmNew.set(
                context.resources.getDrawable(
                    R.drawable.bg_edit_text_under_line_card_change_pin,
                    null
                )
            )
            pinFieldErrorIconConfirmNew.set(null)
        }
    }


    private fun newPinEmptyChecker()
    {
        if(newPin.isEmpty())
        {
            pinFieldBackgroundForNew.set(
                context.resources.getDrawable(
                    R.drawable.bg_edit_text_under_line_card_change_pin,
                    null
                )
            )
            pinFieldErrorIconForNew.set(null)
        }
       else if(newPin.isNotEmpty())
        {
            pinFieldBackgroundForNew.set(
                context.resources.getDrawable(
                    co.yap.yapcore.R.drawable.bg_edit_text_field_not_empty_card_change_pin,
                    null
                )
            )
            pinFieldErrorIconForNew.set(null)
        }
    }

    private fun oldPinEmptyChecker()
    {
        if (oldPin.isNotEmpty()&& errorMessageForPrevious.get().toString().isEmpty())
        {
            pinFieldBackground.set(
                context.resources.getDrawable(
                    co.yap.yapcore.R.drawable.bg_edit_text_field_not_empty_card_change_pin,
                    null
                )
            )
            pinFieldErrorIcon.set(null)
            errorMessageForPrevious.set("")
        }


       else if(oldPin.isEmpty())
        {
            pinFieldBackground.set(
                context.resources.getDrawable(
                    R.drawable.bg_edit_text_under_line_card_change_pin,
                    null
                )
            )
            pinFieldErrorIcon.set(null)
        }
    }

}