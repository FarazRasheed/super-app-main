package co.yap.modules.dashboard.store.household.onboarding.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IHouseHoldSuccess
import co.yap.yapcore.BaseState

class HouseHoldSuccessState : BaseState(), IHouseHoldSuccess.State {

    @get:Bindable
    override var houseHoldUserName: String = "houseHoldUserName"
        set(value) {
            field = value
            notifyPropertyChanged(BR.houseHoldUserName)
        }

    @get:Bindable
    override var houseHoldUserMobile: String = "0123456789"
        set(value) {
            field = value
            notifyPropertyChanged(BR.houseHoldUserMobile)
        }

    @get:Bindable
    override var houseHoldUserEmail: String = "emailz"
        set(value) {
            field = value
            notifyPropertyChanged(BR.houseHoldUserEmail)
        }

    @get:Bindable
    override var houseHoldUserPassCode: String = "houseHoldUserPassCode"
        set(value) {
            field = value
            notifyPropertyChanged(BR.houseHoldUserPassCode)
        }
    @get:Bindable
    override var houseHoldDescription: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.houseHoldDescription)
        }
}