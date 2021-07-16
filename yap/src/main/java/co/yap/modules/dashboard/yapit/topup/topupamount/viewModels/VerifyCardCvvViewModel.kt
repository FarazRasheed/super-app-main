package co.yap.modules.dashboard.yapit.topup.topupamount.viewModels

import android.app.Application
import co.yap.modules.dashboard.yapit.topup.topupamount.interfaces.IVerifyCardCvv
import co.yap.modules.dashboard.yapit.topup.topupamount.states.VerifyCardCvvState
import co.yap.networking.customers.responsedtos.beneficiary.TopUpTransactionModel
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.Order
import co.yap.networking.transactions.requestdtos.TopUpTransactionRequest
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants


class VerifyCardCvvViewModel(application: Application) :
    BaseViewModel<IVerifyCardCvv.State>(application), IVerifyCardCvv.ViewModel {
    private val transactionsRepository: TransactionsRepository = TransactionsRepository
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: VerifyCardCvvState =
        VerifyCardCvvState()

    override fun buttonClickEvent(id: Int) {
        clickEvent.postValue(id)
    }

    override fun topUpTransactionRequest(model: TopUpTransactionModel?) {

        launch {
            state.loading = true
            when (val response = transactionsRepository.cardTopUpTransactionRequest(
                model?.orderId.toString(),
                TopUpTransactionRequest(
                    model?.secureId,
                    model?.cardId,
                    Order(
                        model?.currency,
                        model?.amount?.toDouble().toString()
                    ), state.cardCvv
                )
            )) {
                is RetroApiResponse.Success -> {
                    // state.toast = response.data.data.secure3dId
                    clickEvent.postValue(Constants.TOP_UP_TRANSACTION_SUCCESS)
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.cardCvv=""
                    //clickEvent.postValue(Constants.TOP_UP_TRANSACTION_SUCCESS)
                }
            }
            state.loading = false
        }

    }
}