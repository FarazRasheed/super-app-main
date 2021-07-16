package co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces

import co.yap.modules.dashboard.cards.addpaymentcard.spare.helpers.physical.AddSparePhysicalCardLogicHelper
import co.yap.modules.dashboard.cards.addpaymentcard.spare.helpers.virtual.AddSpareVirtualCardLogicHelper
import co.yap.networking.cards.responsedtos.Address
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.SharedPreferenceManager

interface IAddSpareCard {
    interface State : IBase.State {
        var cardType: String

        //add virtual card layout fields
        var virtualCardFee: String

        //add physical card layout fields

        var physicalCardFee: String
        var avaialableCardBalance: String
        var physicalCardAddressTitle: String
        var physicalCardAddressSubTitle: String
        var physicalCardAddressCheckVisibility: Int
        var physicalCardAddressButtonsVisibility: Int
        var toggleVisibility: Boolean
        var onChangeLocationClick: Boolean
        var enableConfirmLocation: Boolean

    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var cardType: String
        val addSparePhysicalCardLogicHelper: AddSparePhysicalCardLogicHelper
        val addSpareVirtualCardLogicHelper: AddSpareVirtualCardLogicHelper
        var latitude: String
        var longitude: String
        var address: Address?
        var paymentCard: Card?

        val CONFIRM_PHYSICAL_PURCHASE: Int
        val CONFIRM_VIRTUAL_PURCHASE: Int
        val ADD_VIRTUAL_SPARE_SUCCESS_EVENT: Int
        val ADD_PHYSICAL_SPARE_SUCCESS_EVENT: Int
        val REORDER_CARD_SUCCESS_EVENT: Int
        var isFromaddressScreen: Boolean
        var isFromBlockCardScreen: Boolean
        var availableBalance: String
        var sharedPreferenceManager: SharedPreferenceManager
        fun handlePressOnView(id: Int)

        //apis
        fun requestAddSpareVirtualCard()
        fun requestAddSparePhysicalCard()
        fun updateAddressForPhysicalCard()
        fun requestGetAddressForPhysicalCard()
        fun requestReorderCard()
        fun requestReorderCardFee(cardType: String)
        fun requestInitialData()
    }

    interface View : IBase.View<ViewModel>
}