package co.yap.modules.dashboard.yapit.topup.carddetail

import android.app.Application
import co.yap.R
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class TopUpCardDetailViewModel(application: Application) :
    BaseViewModel<ITopUpCardDetail.State>(application),
    ITopUpCardDetail.ViewModel, IRepositoryHolder<CustomersRepository> {
    override val repository: CustomersRepository = CustomersRepository
    override val state: ITopUpCardDetail.State =
        TopUpCardDetailState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    init {
        state.title.set(getString(R.string.screen_topup_card_detail_display_text_title))
    }


    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnBackButton(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onRemoveCard(cardId: String) {
        launch {
            state.loading = true
            when (val response = repository.deleteBeneficiary(cardId)) {
                is RetroApiResponse.Success -> state.isCardDeleted.value = true
                is RetroApiResponse.Error -> {
                    state.isCardDeleted.value = false
                    state.toast = response.error.message
                }
            }
            state.loading = false
        }
    }

}