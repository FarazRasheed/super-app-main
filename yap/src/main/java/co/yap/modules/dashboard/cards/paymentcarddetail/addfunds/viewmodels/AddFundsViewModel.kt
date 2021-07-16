package co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.viewmodels

import android.app.Application
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.interfaces.IAddFunds
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.states.AddFundsState
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.AddFundsRequest
import co.yap.networking.transactions.responsedtos.FundTransferDenominations
import co.yap.networking.transactions.responsedtos.TransactionThresholdModel
import co.yap.sendmoney.R
import co.yap.sendmoney.base.SMFeeViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import co.yap.yapcore.managers.SessionManager

class AddFundsViewModel(application: Application) : SMFeeViewModel<IAddFunds.State>(application),
    IAddFunds.ViewModel, IRepositoryHolder<TransactionsRepository> {
    override val repository: TransactionsRepository = TransactionsRepository
    override val state: IAddFunds.State = AddFundsState()

    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override var transactionThreshold: TransactionThresholdModel? = null
    override var errorDescription: String = ""

    override fun onCreate() {
        super.onCreate()
        showDebitCardAvailableBalance()
        getFundTransferDenominations()
        getFundTransferLimits()
        getTransferFees(TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode)
        getTransactionThresholds()
    }

    override fun handleOnPressView(id: Int) {
        clickEvent.setValue(id)
    }

    private fun showDebitCardAvailableBalance() {
        state.availableBalance.set(
            context.resources.getText(
                getString(Strings.common_display_text_available_balance)
                ,
                context.color(
                    R.color.colorPrimaryDark,
                    SessionManager.cardBalance.value?.availableBalance.toString()
                        .toFormattedCurrency()
                )
            )
        )
    }

    override fun addFunds(success: () -> Unit) {
        launch {
            state.loading = true
            when (val response = repository.addFunds(
                AddFundsRequest(
                    state.amount,
                    state.card.get()?.cardSerialNumber ?: ""
                )
            )) {
                is RetroApiResponse.Success -> {
                    SessionManager.updateCardBalance {
                        state.loading = false
                        success()
                    }
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    showToast(response.error.message)
                }
            }
        }
    }

    override fun getFundTransferDenominations() {
        launch {
            when (val response =
                repository.getFundTransferDenominations(TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode)) {
                is RetroApiResponse.Success -> {
                    val sortedData =
                        response.data.data?.sortedWith(Comparator { s1: FundTransferDenominations, s2: FundTransferDenominations -> s1.amount.toInt() - s2.amount.toInt() })
                    if (sortedData?.size in 1..3) {
                        state.firstDenomination.set(sortedData?.get(0)?.amount ?: "")
                        state.secondDenomination.set(sortedData?.get(1)?.amount ?: "")
                        state.thirdDenomination.set(sortedData?.get(2)?.amount ?: "")
                    }
                }
                is RetroApiResponse.Error -> {
                    showToast(response.error.message)

                }
            }

        }
    }

    override fun getFundTransferLimits() {
        launch {
            when (val response =
                repository.getFundTransferLimits(TransactionProductCode.TOP_UP_SUPPLEMENTARY_CARD.pCode)) {
                is RetroApiResponse.Success -> {
                    state.maxLimit = response.data.data?.maxLimit?.toDouble() ?: 0.00
                    state.minLimit = response.data.data?.minLimit?.toDouble() ?: 0.00
                }
                is RetroApiResponse.Error -> {
                    showToast(response.error.message)
                }
            }
        }
    }

    override fun getTransactionThresholds() {
        launch {
            state.loading = true
            when (val response = repository.getTransactionThresholds()) {
                is RetroApiResponse.Success -> {
                    transactionThreshold = response.data.data
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG_WITH_FINISH.name}"
                }
            }
        }
    }


    fun getAmountWithFee(): Double {
        return state.amount.parseToDouble().plus(updatedFee.value.parseToDouble())
    }

}