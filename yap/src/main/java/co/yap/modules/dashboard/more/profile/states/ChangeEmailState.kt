package co.yap.modules.dashboard.more.profile.states

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.profile.intefaces.IChangeEmail
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.Utils

class ChangeEmailState(application: Application) : BaseState(), IChangeEmail.State {

    val context = application

    @get:Bindable
    override var newEmail: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.newEmail)
            newEmailValidation()
            setButtonState()
        }

    @get:Bindable
    override var newConfirmEMail: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.newConfirmEMail)
            confirmEmailValidation()
            setButtonState()
            checkRunTimeValidations()

        }

    @get:Bindable
    override var errorMessage: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorMessage)
        }

    @get:Bindable
    override var backgroundNew: Drawable? = context.getDrawable(R.drawable.bg_edit_text_under_line)
        set(value) {
            field = value
            notifyPropertyChanged(BR.backgroundNew)
        }

    @get:Bindable
    override var backgroundConfirm: Drawable? =
        context.getDrawable(R.drawable.bg_edit_text_under_line)
        set(value) {
            field = value
            notifyPropertyChanged(BR.backgroundConfirm)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var drawableConfirm: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawableConfirm)
        }

    @get:Bindable
    override var drawableNew: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawableNew)
        }


    fun newEmailValidation(): Boolean {
        errorMessage = ""
        return if (Utils.validateEmail(newEmail)) {
            drawableNew =
                context.getDrawable(R.drawable.path)
            true
        } else {
            drawableNew = null
            false
        }
    }


    fun confirmEmailValidation(): Boolean {
        errorMessage = ""
        return if (Utils.validateEmail(newConfirmEMail)) {
            drawableConfirm =
                context.getDrawable(R.drawable.path)
            true
        } else {
            drawableConfirm = null
            valid = false
            false
        }
    }

    private fun setButtonState() {
        valid = Utils.validateEmail(newEmail) && Utils.validateEmail(newConfirmEMail)
        backgroundNew =
            context.getDrawable(R.drawable.bg_edit_text_under_line)
        drawableNew =
            if (Utils.validateEmail(newEmail)) context.getDrawable(R.drawable.path) else null

        backgroundConfirm =
            context.getDrawable(R.drawable.bg_edit_text_under_line)
        drawableConfirm =
            if (Utils.validateEmail(newConfirmEMail)) context.getDrawable(R.drawable.path) else null
    }

    private fun checkRunTimeValidations() {
        if (!newEmail.contains(newConfirmEMail)) {
            backgroundConfirm =
                context.getDrawable(R.drawable.bg_edit_text_red_under_line)
            drawableConfirm = context.getDrawable(R.drawable.ic_error)
            errorMessage = "email should match"
        }
    }

    fun setErrors(errorMesage: String) {
        if (errorMesage == "email should match") {
            backgroundConfirm =
                context.getDrawable(R.drawable.bg_edit_text_red_under_line)
            drawableConfirm = context.getDrawable(R.drawable.ic_error)
        } else {
            /*  backgroundNew =
                  context.getDrawable(R.drawable.bg_edit_text_red_under_line)
              drawableNew = context.getDrawable(R.drawable.ic_error)
  */
            backgroundConfirm =
                context.getDrawable(R.drawable.bg_edit_text_red_under_line)
            drawableConfirm = context.getDrawable(R.drawable.ic_error)
        }
        errorMessage = errorMesage

    }

}