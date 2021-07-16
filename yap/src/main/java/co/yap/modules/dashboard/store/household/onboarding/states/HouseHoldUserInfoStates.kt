package co.yap.modules.dashboard.store.household.onboarding.states

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IHouseHoldUserInfo
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.StringUtils
import co.yap.yapcore.helpers.Utils

class HouseHoldUserInfoStates(var application: Application) : BaseState(),
    IHouseHoldUserInfo.State {

    var ctx: Context = application.applicationContext
    override var verificationCompleted: Boolean = false

    @get:Bindable
    override var firstName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
            notifyPropertyChanged(BR.valid)
            setFirstNameTextWatcher(firstName)
            validate()
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var firstNameError: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstNameError)
            notifyPropertyChanged(BR.valid)

        }

    @get:Bindable
    override var lastName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
            notifyPropertyChanged(BR.valid)
            setLastNameTextWatcher(lastName)
            validate()
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var lastNameError: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastNameError)
            notifyPropertyChanged(BR.valid)
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
    override var drawbleRightFirstName: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawbleRightFirstName)

        }
        get() {
            return field
        }


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
    override var twoWayTextWatcher: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.twoWayTextWatcher)
            settwoWayTextWatcher()
            validate()
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var cursorPlacement: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.cursorPlacement)

        }

    @get:Bindable
    override var setSelection: Int = 0
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


    @get:Bindable
    override var valid: Boolean = false
        get() = validate()

    private fun validate(): Boolean {
        return StringUtils.validateName(firstName) && StringUtils.validateName(lastName) /*&& Utils.validateEmail(twoWayTextWatcher)*/ &&
                firstNameError.isNullOrEmpty() && lastNameError.isNullOrEmpty() /*&& emailError.isNullOrEmpty()*/
    }


    private fun setFirstNameTextWatcher(value: String) {

        if (!value.isNullOrEmpty() && value.length >= 2) {

            if (StringUtils.validateName(value)) {
                firstNameError = ""
                firstNameError = ""
                notifyPropertyChanged(BR.firstNameError)
                validate()
                notifyPropertyChanged(BR.valid)

            } else {
                valid = false
                firstNameError = "error"
                drawbleRightFirstName = null
                notifyPropertyChanged(BR.firstNameError)
                notifyPropertyChanged(BR.valid)
            }
        } else {
            drawbleRightFirstName = null

        }

    }

    private fun setLastNameTextWatcher(value: String) {

        if (!value.isNullOrEmpty() && value.length >= 2) {

            if (StringUtils.validateName(value)) {
                lastNameError = ""
                notifyPropertyChanged(BR.lastNameError)
                drawbleRightLastName =null
                notifyPropertyChanged(BR.valid)
            } else {
                valid = false
                lastNameError = "error"
                notifyPropertyChanged(BR.lastNameError)
                drawbleRightLastName = null
                notifyPropertyChanged(BR.valid)
            }
        } else {
            drawbleRightLastName = null

        }
    }

    private fun setSuccessUI() {
        refreshField = true
        validate()
        notifyPropertyChanged(BR.valid)
    }

    private fun setDefaultUI() {
        refreshField = true
        valid = false
        drawbleRight = null
        validate()
        notifyPropertyChanged(BR.valid)
    }

    fun settwoWayTextWatcher() {

        if (!twoWayTextWatcher.isNullOrEmpty() && twoWayTextWatcher.length >= 5) {
            if (Utils.validateEmail(twoWayTextWatcher.toString())) {
                setSuccessUI()
            } else {
                setDefaultUI()
            }
        }
        validate()
        notifyPropertyChanged(BR.valid)
    }
}