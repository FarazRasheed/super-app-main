package co.yap.modules.dashboard.cards.paymentcarddetail.limits.viewmodel

import android.app.Application
import android.widget.CompoundButton
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.limits.interfaces.ICardLimits
import co.yap.modules.dashboard.cards.paymentcarddetail.limits.states.CardLimitState
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.requestdtos.CardLimitConfigRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class CardLimitViewModel(application: Application) :
    BaseViewModel<ICardLimits.State>(application),
    ICardLimits.ViewModel, IRepositoryHolder<CardsRepository> {

    override val state: ICardLimits.State = CardLimitState()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val repository: CardsRepository = CardsRepository

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    fun onSwitchChanged(switch: CompoundButton, isChecked: Boolean) {
        if (switch.isPressed) {
            when (switch.id) {
                R.id.swWithdrawal -> {
                    updateAllowAtm()
                }
                R.id.swOnlineTra -> {
                    updateOnlineTransaction()
                }
                R.id.swAbroad -> {
                    updateAbroadPayment()
                }
                R.id.swRetail -> {
                    updateRetailPayment()
                }
            }
        }
    }

    private fun updateAllowAtm() {
        launch {
            state.loading = true
            when (val response =
                repository.configAllowAtm(CardLimitConfigRequest(state.card.get()!!.cardSerialNumber))) {
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message

                    state.card.get()?.also {
                        it.retailPaymentAllowed = it.retailPaymentAllowed
                        state.card.notifyChange()
                    }
                }

                is RetroApiResponse.Success -> {
                    state.loading = false
                    state.card.get()?.atmAllowed = !state.card.get()!!.atmAllowed
                }
            }
        }
    }

    private fun updateOnlineTransaction() {
        launch {
            state.loading = true
            when (val response =
                repository.configOnlineBanking(CardLimitConfigRequest(state.card.get()!!.cardSerialNumber))) {
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message
                    state.card.get()?.also {
                        it.retailPaymentAllowed = it.retailPaymentAllowed
                        state.card.notifyChange()
                    }

                }
                is RetroApiResponse.Success -> {
                    state.loading = false
                    state.card.get()?.onlineBankingAllowed =
                        !state.card.get()!!.onlineBankingAllowed
                }
            }
        }
    }

    private fun updateRetailPayment() {
        launch {
            state.loading = true
            when (val response =
                repository.configRetailPayment(CardLimitConfigRequest(state.card.get()!!.cardSerialNumber))) {
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message

                    state.card.get()?.also {
                        it.retailPaymentAllowed = it.retailPaymentAllowed
                        state.card.notifyChange()
                    }

                }
                is RetroApiResponse.Success -> {
                    state.loading = false
                    state.card.get()?.retailPaymentAllowed =
                        !state.card.get()!!.retailPaymentAllowed
                }
            }
        }
    }

    private fun updateAbroadPayment() {
        launch {
            state.loading = true
            when (val response =
                repository.configAbroadPayment(CardLimitConfigRequest(state.card.get()!!.cardSerialNumber))) {
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message

                    state.card.get()?.also {
                        it.retailPaymentAllowed = it.retailPaymentAllowed
                        state.card.notifyChange()
                    }
                }
                is RetroApiResponse.Success -> {
                    state.loading = false
                    state.card.get()?.paymentAbroadAllowed =
                        !state.card.get()!!.paymentAbroadAllowed

                }
            }
        }
    }

}