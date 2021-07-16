package com.yap.yapandroid.modules.login.states

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import co.yap.BR
import com.yap.yapandroid.modules.login.interfaces.ILogin
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.isValidPhoneNumber
import com.yap.yapandroid.R

class LoginState(application: Application) : BaseState(), ILogin.State {
    var context: Context = application.applicationContext

    @get:Bindable
    override var email: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
            notifyPropertyChanged(BR.valid)
        }

    override var emailError: MutableLiveData<String> = MutableLiveData("")

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }


    fun validate(): Boolean {
        return (email.length > 5 && emailError.value?.isEmpty() ?: false)
    }

    @get:Bindable
    override var twoWayTextWatcher: String = ""
        set(value) {
            field = value

            notifyPropertyChanged(BR.twoWayTextWatcher)
            setTwoWayTextWatcher()
        }


    @get:Bindable
    override var drawbleRight: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawbleRight)
        }
        get() {
            return field
        }

    @get:Bindable
    override var refreshField: Boolean = false
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.refreshField)
        }

    private fun setTwoWayTextWatcher() {
        if ((Utils.isUsernameNumeric(twoWayTextWatcher) && isValidPhoneNumber(
                twoWayTextWatcher,
                "AE"
            )) || Utils.validateEmail(twoWayTextWatcher)
        ) {
            setSuccessUI()
        } else {
            setDefaultUI()
        }
    }

    private fun setDefaultUI() {
        refreshField = true
        valid = false
        drawbleRight = null
    }

    private fun setSuccessUI() {
        refreshField = true
        valid = true
        emailError.value = ""
        drawbleRight = context.resources.getDrawable(R.drawable.path, null)
    }

}