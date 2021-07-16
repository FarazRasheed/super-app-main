package co.yap.modules.dashboard.yapit.topup.cardslisting

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.R
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.responsedtos.CardLimits
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Translator
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class TopUpBeneficiariesViewModel(application: Application) :
    BaseViewModel<ITopUpBeneficiaries.State>(application),
    ITopUpBeneficiaries.ViewModel, IRepositoryHolder<CustomersRepository> {

    override var cardLimits: CardLimits? = null
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: ITopUpBeneficiaries.State = TopUpBeneficiariesState()
    override val repository: CustomersRepository = CustomersRepository
    override val topUpCards: MutableLiveData<List<TopUpCard>> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        getPaymentCards()
    }

    override fun onResume() {
        super.onResume()
        getCardsLimit()
    }

    override fun handlePressOnBackButton(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    private fun getCardsLimit() {
        launch {
            when (val response = repository.getCardsLimit()) {
                is RetroApiResponse.Success -> {
                    cardLimits = response.data.cardLimits
                }
                is RetroApiResponse.Error -> state.toast = response.error.message
            }
        }
    }

    override fun getPaymentCards() {
        launch {
            state.loading = true
            when (val response = repository.getTopUpBeneficiaries()) {
                is RetroApiResponse.Success -> {
                    if (state.enableAddCard.get())
                        response.data.data?.add(
                            TopUpCard(
                                alias = "addCard",
                                id = if (response.data.data.isNullOrEmpty()) "+ Add card" else "+ Add a new card"
                            )
                        )
                    response.data.data?.let { topUpCards.value = it }
                    state.responseReceived.set(true)
                    state.loading = false
                }

                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun updateCardCount() {
        topUpCards.value?.size?.let {
            val size = it - if (state.enableAddCard.get()) 1 else 0
            val message = Translator.getString(
                context,
                R.string.screen_cards_display_text_cards_count
            ).replace("%d", size.toString())
            when (size) {
                0 -> {
                    state.noOfCard.set("Start by adding a card")
                    state.message.set("No cards are connected to this account yet")
                }
                1 -> state.noOfCard.set(message.substring(0, message.length - 1))
                else -> state.noOfCard.set(message)
            }
        }
    }

}