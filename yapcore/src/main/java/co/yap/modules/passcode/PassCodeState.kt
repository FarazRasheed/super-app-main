package co.yap.modules.passcode

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import co.yap.yapcore.BR
import co.yap.yapcore.BaseState

class PassCodeState : BaseState(), IPassCode.State {

    @get:Bindable
    override var passCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.passCode)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)

        }

    @get:Bindable
    override var dialerError: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.dialerError)
        }

    @get:Bindable
    override var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    override var needTermsConditions: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.needTermsConditions)
        }

    @get:Bindable
    override var buttonTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }

    @get:Bindable
    override var forgotTextVisibility: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.forgotTextVisibility)
        }

    fun validate() {
        if (passCode.length >= 4) {
            valid = true
        } else {
            dialerError = ""
            valid = false
        }
    }

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
                passCode = p0.toString()
                validate()
            }
        }
    }

    override var toolbarVisibility: ObservableBoolean = ObservableBoolean(false)
}