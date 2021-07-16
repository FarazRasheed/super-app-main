package co.yap.modules.setcardpin.pinflow

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.Bindable
import co.yap.yapcore.BR
import co.yap.yapcore.BaseState

class PinState : BaseState(), IPin.State {

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
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)

        }

    @get:Bindable
    override var pincode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.pincode)

        }
    @get:Bindable
    override var dialerError: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.dialerError)
        }

    @get:Bindable
    override var titleSetPin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.titleSetPin)
        }

    @get:Bindable
    override var buttonTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }


    @get:Bindable
    override var oldPin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.oldPin)
        }
    @get:Bindable
    override var newPin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.newPin)
        }
    @get:Bindable
    override var confirmPin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.confirmPin)
        }

    @get:Bindable
    override var cardSerialNumber: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardSerialNumber)
        }

    @get:Bindable
    override var forgotTextVisibility: Boolean=false
        set(value) {
            field=value
            notifyPropertyChanged(BR.forgotTextVisibility)
        }

    @get:Bindable
    override var flowType: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.flowType)
        }


    fun validate() {
        if (pincode.length >= 4) {
            valid = true
        } else {
            dialerError = ""
            valid = false
        }
    }

    override fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                pincode = p0.toString()
                validate()
            }
        }
    }
}