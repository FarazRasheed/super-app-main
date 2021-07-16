package co.yap.modules.location.interfaces

import androidx.databinding.ObservableBoolean
import co.yap.countryutils.country.Country
import co.yap.networking.cards.responsedtos.Address
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ILocation {
    interface State : IBase.State {
        var toolbarVisibility: ObservableBoolean
        var rightIcon: ObservableBoolean
        var leftIcon: ObservableBoolean
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnView(id: Int)

        var address: Address?
        var isOnBoarding: Boolean
        var defaultHeading: String
        var heading: String
        var subHeading: String
        val clickEvent: SingleClickEvent
        var selectedCountry: Country?
        var countries: ArrayList<Country>

    }

    interface View : IBase.View<ViewModel>

}