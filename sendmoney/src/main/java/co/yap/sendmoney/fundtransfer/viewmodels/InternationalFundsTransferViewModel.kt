package co.yap.sendmoney.fundtransfer.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.RxListRequest
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.networking.transactions.responsedtos.transaction.FxRateResponse
import co.yap.sendmoney.R
import co.yap.sendmoney.fundtransfer.interfaces.IInternationalFundsTransfer
import co.yap.sendmoney.fundtransfer.states.InternationalFundsTransferState
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.FeeType
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.roundVal
import co.yap.yapcore.helpers.extentions.roundValHalfEven
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import co.yap.yapcore.managers.SessionManager
import java.util.*

class InternationalFundsTransferViewModel(application: Application) :
    BeneficiaryFundTransferBaseViewModel<IInternationalFundsTransfer.State>(application),
    IInternationalFundsTransfer.ViewModel,
    IRepositoryHolder<CustomersRepository> {
    private var mTransactionsRepository: TransactionsRepository = TransactionsRepository
    override val repository: CustomersRepository = CustomersRepository
    override val state: InternationalFundsTransferState =
        InternationalFundsTransferState(
            application
        )
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var purposeOfPaymentList: MutableLiveData<ArrayList<PurposeOfPayment>> =
        MutableLiveData()
    override var reasonPosition: Int = 0
    override var fxRateResponse: MutableLiveData<FxRateResponse.Data> = MutableLiveData()
    val transactionMightGetHeld: MutableLiveData<Boolean> = MutableLiveData(false)
    var purposeCategories: Map<String?, List<PurposeOfPayment>>? = HashMap()

    override fun handlePressOnButton(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onCreate() {
        super.onCreate()
        parentViewModel?.state?.toolbarTitle = getString(Strings.screen_funds_toolbar_header)
        state.availableBalanceString =
            context.resources.getText(
                getString(Strings.screen_cash_transfer_display_text_available_balance),
                context.color(
                    R.color.colorPrimaryDark,
                    SessionManager.cardBalance.value?.availableBalance?.toFormattedCurrency(
                        showCurrency = true
                    ) ?: ""
                )
            )
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_international_funds_transfer_display_text_title))
    }

    override fun getTransactionInternationalfxList(productCode: String?) {
        launch {
            state.loading = true
            val rxListBody = RxListRequest(parentViewModel?.beneficiary?.value?.id.toString())

            when (val response =
                mTransactionsRepository.getTransactionInternationalRXList(
                    productCode,
                    rxListBody
                )) {
                is RetroApiResponse.Success -> {
                    fxRateResponse.value = response.data.data
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    if (parentViewModel?.isSameCurrency == true) {
                        state.sourceCurrency.set(SessionManager.getDefaultCurrency())
                        state.destinationCurrency.set(SessionManager.getDefaultCurrency())
                        parentViewModel?.transferData?.value?.rate = "1.0"
                    } else {
                        isAPIFailed.value = true
                        state.toast = response.error.message
                    }
                }
            }
        }
    }

    override fun getReasonList(productCode: String) {
        launch {
//            state.loading = true
            when (val response =
                mTransactionsRepository.getPurposeOfPayment(productCode)) {
                is RetroApiResponse.Success -> {
                    if (!response.data.data.isNullOrEmpty()) {
                        purposeOfPaymentList.value =
                            response.data.data as? ArrayList<PurposeOfPayment>?
                    }
//                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message
                    isAPIFailed.value = true
                }
            }
        }
    }

    override fun getMoneyTransferLimits(productCode: String?) {
        launch {
            when (val response = mTransactionsRepository.getFundTransferLimits(productCode)) {
                is RetroApiResponse.Success -> {
                    state.maxLimit = response.data.data?.maxLimit?.toDouble() ?: 0.00
                    state.minLimit = response.data.data?.minLimit?.toDouble() ?: 0.00
                    getCountryLimits()
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    isAPIFailed.value = true
                }
            }
        }

    }

    override fun getCountryLimits() {
        launch {
            when (val response = repository.getCountryTransactionLimits(
                parentViewModel?.beneficiary?.value?.country ?: "",
                parentViewModel?.beneficiary?.value?.currency ?: ""
            )) {
                is RetroApiResponse.Success -> {
                    if (response.data.data?.toDouble() ?: 0.0 > 0.0) {
                        state.maxLimit = response.data.data?.toDouble()
                        if (response.data.data?.toDouble() ?: 0.0 < state.minLimit?.toDouble() ?: 0.0) {
                            state.minLimit = 1.0
                        }
                    }
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    isAPIFailed.value = true
                }
            }
        }

    }

    override fun getTransactionThresholds() {
        launch {
            when (val response = mTransactionsRepository.getTransactionThresholds()) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.transactionThreshold?.value = response.data.data
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    isAPIFailed.value = true
                }
            }
        }
    }

    override fun processPurposeList(list: ArrayList<PurposeOfPayment>) {
        purposeCategories = list.groupBy { item ->
            item.purposeCategory
        }
    }

    fun updateFees() {
        updateFees(
            enterAmount = if (feeCurrency.equals(
                    parentViewModel?.beneficiary?.value?.currency,
                    true
                )
            ) state.etInputAmount.toString() else state.etOutputAmount.toString()
        )
    }

    fun getTotalAmountWithFee(): Double {
        return (when (feeType) {
            FeeType.TIER.name -> {
                val transferFee = getFeeFromTier(
                    enterAmount = if (feeCurrency.equals(
                            parentViewModel?.beneficiary?.value?.currency,
                            true
                        )
                    ) state.etInputAmount.toString() else state.etOutputAmount.toString()
                )
                state.etOutputAmount.parseToDouble().plus(transferFee.parseToDouble())
            }
            FeeType.FLAT.name -> {
                val transferFee = getFlatFee(
                    enterAmount = if (feeCurrency.equals(
                            parentViewModel?.beneficiary?.value?.currency,
                            true
                        )
                    ) state.etInputAmount.toString() else state.etOutputAmount.toString()
                )
                state.etOutputAmount.parseToDouble().plus(transferFee.parseToDouble())
            }
            else -> {
                0.00
            }
        })
    }

    fun setDestinationAmount() {
        if (!state.etInputAmount.isNullOrBlank()) {
            val totalDestinationAmount = state.etInputAmount?.toDoubleOrNull()
                ?.times(parentViewModel?.transferData?.value?.rate?.toDoubleOrNull() ?: 0.0)
            totalDestinationAmount?.let {
                state.etOutputAmount =
                    if (parentViewModel?.beneficiary?.value?.beneficiaryType == SendMoneyBeneficiaryType.RMT.type) {
                        it.roundValHalfEven().toString()
                            .toFormattedCurrency(false, state.sourceCurrency.get(), false)
                    } else {
                        it.roundVal().toString()
                            .toFormattedCurrency(false, state.sourceCurrency.get(), false)
                    }
            }
        } else {
            state.etOutputAmount = ""
        }
    }

    override fun checkCoolingPeriodRequest(
        beneficiaryId: String?,
        beneficiaryCreationDate: String?,
        beneficiaryName: String?,
        amount: String?,
        success: () -> Unit
    ) {
        launch {
            state.loading = true
            when (val response =
                mTransactionsRepository.checkCoolingPeriodRequest(
                    beneficiaryId = beneficiaryId,
                    beneficiaryCreationDate =beneficiaryCreationDate,
                    beneficiaryName =beneficiaryName,
                    amount = state.etOutputAmount
                )) {
                is RetroApiResponse.Success -> {
                    success.invoke()
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.errorDescription = response.error.message
                    parentViewModel?.errorEvent?.value = state.errorDescription
                }
            }
            state.loading = false
        }
    }

    override fun getCutOffTimeConfiguration() {
        parentViewModel?.beneficiary?.value?.run {
            beneficiaryType?.let { beneficiaryType ->
                if (beneficiaryType.isNotEmpty())
                    when (SendMoneyBeneficiaryType.valueOf(beneficiaryType)) {
                        SendMoneyBeneficiaryType.SWIFT, SendMoneyBeneficiaryType.UAEFTS -> {
                            launch {
                                when (val response =
                                    mTransactionsRepository.getCutOffTimeConfiguration(
                                        getProductCode(),
                                        currency,
                                        if (state.etInputAmount.toString()
                                                .isEmpty()
                                        ) "0.0" else state.etInputAmount.toString(),
                                        parentViewModel?.selectedPop?.cbwsi ?: false
                                    )) {
                                    is RetroApiResponse.Success -> {
                                        response.data.data?.let {
                                            transactionMightGetHeld.value = true
                                        }
                                    }
                                    is RetroApiResponse.Error -> {
                                        transactionMightGetHeld.value = false
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun getProductCode(): String? {
        parentViewModel?.beneficiary?.value?.beneficiaryType?.let {
            when (SendMoneyBeneficiaryType.valueOf(it)) {
                SendMoneyBeneficiaryType.SWIFT -> {
                    return TransactionProductCode.SWIFT.pCode
                }
                SendMoneyBeneficiaryType.UAEFTS -> {
                    TransactionProductCode.UAEFTS.pCode
                }
                else -> null
            }
        }
        return null
    }

}
