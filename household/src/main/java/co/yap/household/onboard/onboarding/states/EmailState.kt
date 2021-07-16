package co.yap.household.onboard.onboarding.states

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.Bindable
import co.yap.household.BR
import co.yap.household.R
import co.yap.household.onboard.onboarding.interfaces.IEmail
import co.yap.yapcore.BaseState
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils

class EmailState (application: Application) : BaseState(), IEmail.State {

    override var verificationCompleted: Boolean = false
    val context: Context = application.applicationContext

    override fun reset() {
        super.reset()
        verificationCompleted = false
    }

    @get:Bindable
    override var deactivateField: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.deactivateField)
        }


    @get:Bindable
    override var emailBtnTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailBtnTitle)
        }

    @get:Bindable
    override var emailVerificationTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailVerificationTitle)
        }


    @get:Bindable
    override var emailTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailTitle)
        }

    @get:Bindable
    override var twoWayTextWatcher: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.twoWayTextWatcher)
            settwoWayTextWatcher()
        }

    @get:Bindable
    override var emailHint: String =
        context.getString(R.string.screen_enter_email_display_text_email_address)
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailHint)
        }

    @get:Bindable
    override var email: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    override var emailError: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailError)
        }


    @get:Bindable
    override var drawbleRight: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawbleRight)

        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }


    @get:Bindable
    override var cursorPlacement: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.cursorPlacement)

        }

    @get:Bindable
    override var setSelection: Int = email.length
        set(value) {
            field = value
            notifyPropertyChanged(BR.setSelection)

        }

    @get:Bindable
    override var refreshField: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.refreshField)

        }
    @get:Bindable
    override var handleBackPress: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.handleBackPress)
        }



    private fun setSuccessUI() {
        refreshField = true
        valid = true
        emailError = ""
        setDrawableTint()
    }

    private fun setDefaultUI() {
        refreshField = true
        valid = false
        drawbleRight = null
    }

    fun settwoWayTextWatcher() {

        if (!twoWayTextWatcher.isNullOrEmpty() && twoWayTextWatcher.length >= 5) {
            if (Utils.validateEmail(twoWayTextWatcher.toString())) {
                setSuccessUI()
            } else {
                setDefaultUI()
            }
        }
    }


    @SuppressLint("ResourceType")
    fun setDrawableTint() {
        drawbleRight = context.getDrawable(co.yap.yapcore.R.drawable.path)?.let {
            DrawableCompat.wrap(
                it
            )
        }
        if (SharedPreferenceManager(context).getThemeValue().equals(Constants.THEME_HOUSEHOLD)) {
            DrawableCompat.setTint(drawbleRight!!, Color.RED);
        }
    }
}