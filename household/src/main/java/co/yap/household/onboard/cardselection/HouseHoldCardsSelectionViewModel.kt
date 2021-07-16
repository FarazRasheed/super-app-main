package co.yap.household.onboard.cardselection

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import co.yap.household.onboard.cardselection.adaptor.HouseHoldCardSelectionAdapter
import co.yap.household.onboard.onboarding.interfaces.IHouseHoldCardsSelection
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.responsedtos.Address
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.interfaces.OnItemClickListener

class HouseHoldCardsSelectionViewModel(application: Application) :
    BaseViewModel<IHouseHoldCardsSelection.State>(application), IHouseHoldCardsSelection.ViewModel {

    override val state: HouseHoldCardsSelectionState =
        HouseHoldCardsSelectionState()
    override var adapter: HouseHoldCardSelectionAdapter =
        HouseHoldCardSelectionAdapter(
            context,
            mutableListOf()
        )
    private val cardsRepository: CardsRepository = CardsRepository
    override val changedPosition: MutableLiveData<Int> = MutableLiveData()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override var orderCardRequestSuccess: MutableLiveData<Boolean> = MutableLiveData()


    override fun onCreate() {
        super.onCreate()
        initViews()
        setUpItemClickListener()
    }

    override fun initViews() {
        getCardsDesignListRequest("B2C_HOUSEHOLD")
    }


    override fun handlePressOnButton(id: Int) {
        clickEvent.setValue(id)
    }

    /* override fun getCardsColorList(list: MutableList<HouseHoldCardsDesign?>?): MutableList<HouseHoldCardsDesign?>? {
         return list
     }*/

    override fun getCardsDesignListRequest(accountType: String) {
        launch {
            state.loading = true
            when (val response =
                cardsRepository.getHouseHoldCardsDesign(accountType = accountType)) {
                is RetroApiResponse.Success -> {
                    if (response.data.data.isNullOrEmpty()) return@launch

                    response.data.data?.let {
                        adapter.setList(it)
                    }

                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message
                }
            }
            state.loading = false
        }
    }


    override fun orderHouseHoldPhysicalCardRequest(address: Address) {
        launch {
            state.loading = true
            when (val response =
                cardsRepository.orderCard(
                    address
                )) {
                is RetroApiResponse.Success -> {
                    orderCardRequestSuccess.value = true
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
        }
    }


    private fun setUpItemClickListener() {
        adapter.setItemListener(object : OnItemClickListener {
            override fun onItemClick(view: View, data: Any, pos: Int) {
                changedPosition.value = pos
            }
        })
    }

}