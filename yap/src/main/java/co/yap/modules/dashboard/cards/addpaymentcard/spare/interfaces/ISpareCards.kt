package co.yap.modules.dashboard.cards.addpaymentcard.interfaces

import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface ISpareCards {

    interface View : IBase.View<ViewModel>{
        fun setObservers()
        fun removeObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        val errorEvent: SingleClickEvent
        fun loadJSONDummyList(): ArrayList<BenefitsModel>
        fun handlePressOnAddVirtualCard(id: Int)
        fun handlePressOnAddPhysicalCard(id: Int)
        fun getVirtualCardFee()
        fun getPhysicalCardFee()
    }

    interface State : IBase.State {
        var virtualCardFee: String
        var physicalCardFee: String
    }
}