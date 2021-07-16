package co.yap.household.onboard.onboarding.states

import androidx.databinding.Bindable
import co.yap.household.BR
import co.yap.household.onboard.onboarding.interfaces.IHouseHoldNumberRegistration
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.isValidPhoneNumber

class HouseHoldNumberRegistrationState : BaseState(), IHouseHoldNumberRegistration.State {

    @get:Bindable
    override var welcomeHeading: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.welcomeHeading)
        }
    @get:Bindable
    override var numberConfirmationValue: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.numberConfirmationValue)
        }
    @get:Bindable
    override var parentName: String? = "Joe"
        set(value) {
            field = value
            notifyPropertyChanged(BR.parentName)
        }
    @get:Bindable
    override var userName: String? = "Logan"
        set(value) {
            field = value
            notifyPropertyChanged(BR.userName)
        }
    @get:Bindable
    override var phoneNumber: String? = ""
        set(value) {
            field = value?.replace(" ", "")
            notifyPropertyChanged(BR.phoneNumber)
            buttonValidation = isValidPhoneNumber(phoneNumber!!, countryCode)

        }
    @get:Bindable
    override var countryCode: String = "AE"
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryCode)
        }
    @get:Bindable
    override var buttonTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }
    @get:Bindable
    override var buttonValidation: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonValidation)
        }
    @get:Bindable
    override var showErrorMessage: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.showErrorMessage)
        }
    @get:Bindable
    override var existingYapUser: Boolean? = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.existingYapUser)
        }
}