package co.yap.household.onboard.onboarding.interfaces

//import co.yap.household.onboarding.onboarding.fragments.CircleColorAdapter
import androidx.lifecycle.MutableLiveData
import co.yap.household.onboard.cardselection.adaptor.HouseHoldCardSelectionAdapter
import co.yap.networking.cards.responsedtos.Address
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseHoldCardsSelection {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
        fun setUpUI()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun initViews()
        val clickEvent: SingleClickEvent
        fun handlePressOnButton(id: Int)
        //        fun getCardsColorList(list: MutableList<HouseHoldCardsDesign?>?): MutableList<HouseHoldCardsDesign?>?
        fun getCardsDesignListRequest(accountType: String)

        var orderCardRequestSuccess: MutableLiveData<Boolean>
        var adapter: HouseHoldCardSelectionAdapter
        fun orderHouseHoldPhysicalCardRequest(address: Address)
        val changedPosition: MutableLiveData<Int>

    }

    interface State : IBase.State {
        var cardsHeading: String
        var locationVisibility: Boolean
        var designCode: String?
        var address: Address?
        var position: Int?
        var buttonVisibility: Boolean
    }
}