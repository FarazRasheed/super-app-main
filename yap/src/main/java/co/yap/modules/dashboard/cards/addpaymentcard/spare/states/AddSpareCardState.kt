package co.yap.modules.dashboard.cards.addpaymentcard.spare.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces.IAddSpareCard
import co.yap.yapcore.BaseState

class AddSpareCardState : BaseState(), IAddSpareCard.State {

    val VISIBLE: Int = 0x00000000
    val GONE: Int = 0x00000008

    @get:Bindable
    override var cardType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardType)
        }

    //add virtual card layout fields
    @get:Bindable
    override var virtualCardFee: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.virtualCardFee)
        }

    //add physical card layout fields
    @get:Bindable
    override var physicalCardFee: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.physicalCardFee)
        }

    @get:Bindable
    override var avaialableCardBalance: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.avaialableCardBalance)
        }

    @get:Bindable
    override var physicalCardAddressTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.physicalCardAddressTitle)
        }

    @get:Bindable
    override var physicalCardAddressSubTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.physicalCardAddressSubTitle)
        }

    @get:Bindable
    override var toggleVisibility: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.toggleVisibility)
            if (!field) {
                physicalCardAddressButtonsVisibility = VISIBLE
                physicalCardAddressCheckVisibility = GONE
            } else {
                physicalCardAddressButtonsVisibility = GONE
                physicalCardAddressCheckVisibility = VISIBLE
            }

        }

    @get:Bindable
    override var onChangeLocationClick: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.onChangeLocationClick)

        }


    @get:Bindable
    override var physicalCardAddressCheckVisibility: Int = GONE
        set(value) {
            field = value
            notifyPropertyChanged(BR.physicalCardAddressCheckVisibility)
        }


    @get:Bindable
    override var physicalCardAddressButtonsVisibility: Int = VISIBLE
        set(value) {
            field = value
            notifyPropertyChanged(BR.physicalCardAddressButtonsVisibility)

        }

    @get:Bindable
    override var enableConfirmLocation: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.enableConfirmLocation)

        }


}