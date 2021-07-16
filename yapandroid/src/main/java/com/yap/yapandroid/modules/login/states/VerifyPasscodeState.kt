package com.yap.yapandroid.modules.login.states

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.yap.yapandroid.BR
import com.yap.yapandroid.modules.login.fragments.VerifyPassCodeEnum
import com.yap.yapandroid.modules.login.interfaces.IVerifyPasscode
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BaseState

class VerifyPasscodeState(application: Application) : BaseState(), IVerifyPasscode.State {

    @get:Bindable
    override var deviceId: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.deviceId)
        }

    @get:Bindable
    override var username: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.username)
        }

    @get:Bindable
    override var dialerError: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.dialerError)
        }
    @get:Bindable
    override var btnVerifyPassCodeText: String? = Translator.getString(application , Strings.screen_verify_passcode_button_sign_in )
        set(value) {
            field = value
            notifyPropertyChanged(BR.btnVerifyPassCodeText)
        }
    @get:Bindable
    override var passcode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.passcode)

        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    override fun validationPasscode(passcodeText: String) {
        if (passcodeText.equals("1234")) {
            passcode = "length can not b in sequence"
        }
    }

    @get:Bindable
    override var sequence: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.sequence)
        }

    @get:Bindable
    override var similar: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.similar)
        }
    @get:Bindable
    override var verifyPassCodeEnum: String = VerifyPassCodeEnum.ACCESS_ACCOUNT.name
        set(value) {
            field = value
            notifyPropertyChanged(BR.verifyPassCodeEnum)
        }

    override var isScreenLocked: ObservableField<Boolean> = ObservableField()
    override var isAccountLocked: ObservableField<Boolean> = ObservableField(false)
    override var isAccountFreeze: ObservableField<Boolean> = ObservableField(false)

    fun validate(text: String) {
        valid = text.length in 7 downTo 4
    }

    override fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate(p0.toString())
            }
        }
    }

}