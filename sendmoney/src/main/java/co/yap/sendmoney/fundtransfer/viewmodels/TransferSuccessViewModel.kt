package co.yap.sendmoney.fundtransfer.viewmodels

import android.app.Application
import co.yap.networking.cards.responsedtos.CardBalance
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.sendmoney.fundtransfer.interfaces.ITransferSuccess
import co.yap.sendmoney.fundtransfer.states.TransferSuccessState
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.managers.SessionManager

class TransferSuccessViewModel(application: Application) :
    BeneficiaryFundTransferBaseViewModel<ITransferSuccess.State>(application), ITransferSuccess.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val repository: CustomersRepository = CustomersRepository

    override val state: TransferSuccessState =
        TransferSuccessState()

    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var updatedCardBalanceEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnButtonClick(id: Int) {
        clickEvent.setValue(id)
    }

    override fun getAccountBalanceRequest() {
        launch {
            state.loading = true
            when (val response = SessionManager.repository.getAccountBalanceRequest()) {
                is RetroApiResponse.Success -> {
                    SessionManager.cardBalance.value =
                        (CardBalance(availableBalance = response.data.data?.availableBalance.toString()))
                    updatedCardBalanceEvent.call()
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                }
            }
        }
    }

    override val backButtonPressEvent: SingleClickEvent = SingleClickEvent()

    override fun onResume() {
        super.onResume()
        toggleToolBarVisibility(false)
    }
}