package co.yap.household.onboard.cardselection

import androidx.databinding.Bindable
import co.yap.household.BR
import co.yap.household.onboard.onboarding.interfaces.IHouseHoldCardsSelection
import co.yap.networking.cards.responsedtos.Address
import co.yap.yapcore.BaseState

class HouseHoldCardsSelectionState : BaseState(), IHouseHoldCardsSelection.State {

    @get:Bindable
    override var cardsHeading: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardsHeading)
        }
    @get:Bindable
    override var locationVisibility: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.locationVisibility)
        }
    @get:Bindable
    override var designCode: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.designCode)
        }
    @get:Bindable
    override var address: Address? = Address()
        set(value) {
            field = value
            notifyPropertyChanged(BR.address)
        }
    /*  @get:Bindable
      override var cardAddressTitle: String = ""
          set(value) {
              field = value
              notifyPropertyChanged(BR.cardAddressTitle)
          }
      @get:Bindable
      override var cardAddressSubTitle: String = ""
          set(value) {
              field = value
              notifyPropertyChanged(BR.cardAddressSubTitle)
          }*/
    @get:Bindable
    override var position: Int? = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.position)
        }
    @get:Bindable
    override var buttonVisibility: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonVisibility)
        }
}