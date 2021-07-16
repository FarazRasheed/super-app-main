package co.yap.modules.onboarding.states

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import co.yap.BR
import co.yap.modules.onboarding.interfaces.IName
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.StringUtils

class NameState(application: Application) : BaseState(), IName.State {

    override var dummyStrings: Array<String> = arrayOf("0123")
    val context: Context = application.applicationContext

    override var firstNameError: MutableLiveData<String> = MutableLiveData("")
    override var lastNameError: MutableLiveData<String> = MutableLiveData("")

    @get:Bindable
    override var firstName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
            notifyPropertyChanged(BR.valid)
            setFirstNameTextWatcher(firstName)
            validate()
        }

    @get:Bindable
    override var lastName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
            notifyPropertyChanged(BR.valid)
            setLastNameTextWatcher(lastName)
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
    override var drawbleRightLastName: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawbleRightLastName)

        }
        get() {
            return field
        }

    @get:Bindable
    override var valid: Boolean = false
        get() = validate()


    private fun validate(): Boolean {
        return StringUtils.validateName(firstName)
                && StringUtils.validateLastName(lastName)
                && firstNameError.value.isNullOrEmpty()
                && lastNameError.value.isNullOrEmpty()
    }

    private fun setFirstNameTextWatcher(value: String) {

        if (!value.isNullOrEmpty() && value.length >= 2) {

            if (StringUtils.validateName(value)) {
                valid = true
                firstNameError.value = ""
//                lastNameError.value = ""
                drawbleRight = context.resources.getDrawable(co.yap.yapcore.R.drawable.path)
                validate()
            } else {
                valid = false
                firstNameError.value = "Please enter alphabets only."
//                lastNameError.value = ""
                drawbleRight = null
                notifyPropertyChanged(BR.valid)
            }
        } else {
            drawbleRight = null
            firstNameError.value = ""

        }
    }

    private fun setLastNameTextWatcher(value: String) {

        if (!value.isNullOrEmpty() && value.length >= 1) {

            if (StringUtils.validateLastName(value)) {
                valid = true
                lastNameError.value = ""
                drawbleRightLastName =
                    context.resources.getDrawable(co.yap.yapcore.R.drawable.path)

            } else {
                valid = false
                lastNameError.value = "Please enter alphabets only."
                drawbleRightLastName = null

            }
        } else {
            drawbleRightLastName = null
            lastNameError.value = ""

        }
    }
}