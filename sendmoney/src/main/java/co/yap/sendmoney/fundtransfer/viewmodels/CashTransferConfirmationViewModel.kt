package co.yap.sendmoney.fundtransfer.viewmodels

import android.app.Application
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.SendMoneyTransferRequest
import co.yap.sendmoney.fundtransfer.interfaces.ICashTransferConfirmation
import co.yap.sendmoney.fundtransfer.states.CashTransferConfirmationState
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.managers.SessionManager

class CashTransferConfirmationViewModel(application: Application) :
    BeneficiaryFundTransferBaseViewModel<ICashTransferConfirmation.State>(application),
    ICashTransferConfirmation.ViewModel, IRepositoryHolder<TransactionsRepository> {
    override val repository: TransactionsRepository = TransactionsRepository
    override val state: CashTransferConfirmationState =
        CashTransferConfirmationState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()

    override fun onCreate() {
        super.onCreate()
        parentViewModel?.state?.leftIcon?.set(true)
        parentViewModel?.state?.rightIcon?.set(false)
        parentViewModel?.state?.toolbarTitle = "Confirm transfer"
        getCutOffTimeConfiguration()
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun proceedToTransferAmount() {
        parentViewModel?.beneficiary?.value?.let { beneficiary ->
            beneficiary.id?.let { beneficiaryId ->
                if (beneficiary.beneficiaryType?.isNotEmpty() == true)
                    when (SendMoneyBeneficiaryType.valueOf(beneficiary.beneficiaryType ?: "")) {
                        SendMoneyBeneficiaryType.UAEFTS -> uaeftsTransferRequest(beneficiaryId.toString())
                        SendMoneyBeneficiaryType.DOMESTIC -> domesticTransferRequest(beneficiaryId.toString())
                        else -> state.toast = "Invalid Beneficiary Type"
                    }
            }
        }
    }


    override fun getCutOffTimeConfiguration() {
        launch {
            state.loading = true
            when (val response =
                repository.getCutOffTimeConfiguration(
                    productCode = getProductCode(),
                    currency = SessionManager.getDefaultCurrency(),
                    amount = parentViewModel?.transferData?.value?.transferAmount,
                    isCbwsi = if (parentViewModel?.beneficiary?.value?.cbwsicompliant == true) parentViewModel?.selectedPop?.cbwsi
                        ?: false else parentViewModel?.beneficiary?.value?.cbwsicompliant
                )) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.transferData?.value?.cutOffTimeMsg = null
                    response.data.data?.let {
                        state.cutOffTimeMsg.set(it.errorMsg)
                        parentViewModel?.transferData?.value?.cutOffTimeMsg = it.errorMsg
                        parentViewModel?.transactionWillHold = true
                    }
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    showToast(response.error.message)
                    state.loading = false
                }
            }
        }

    }

    /* don't remove this code will be use later
     feeAmount = if (parentViewModel?.transferData?.value?.feeAmount.isNullOrBlank()) "0.0" else parentViewModel?.transferData?.value?.feeAmount,
                            vat = if (parentViewModel?.transferData?.value?.vat.isNullOrBlank()) "0.0" else parentViewModel?.transferData?.value?.vat,
                            totalCharges = parentViewModel?.transferData?.value?.transferFee,
                            totalAmount = parentViewModel?.transferData?.value?.transferAmount.parseToDouble().plus(
                                parentViewModel?.transferData?.value?.transferFee.parseToDouble()
                            ).toString()
     */
    override fun uaeftsTransferRequest(beneficiaryId: String?) {
        launch {
            state.loading = true
            when (val response =
                repository.uaeftsTransferRequest(
                    SendMoneyTransferRequest(
                        beneficiaryId = beneficiaryId?.toInt(),
                        amount = parentViewModel?.transferData?.value?.transferAmount,
                        settlementAmount = 0.0,
                        purposeCode = parentViewModel?.selectedPop?.purposeCode,
                        purposeReason = parentViewModel?.selectedPop?.purposeDescription,
                        cbwsi = if (parentViewModel?.isCutOffTimeStarted == true) !checkCBWSI() else false,
                        cbwsiFee = parentViewModel?.selectedPop?.cbwsiFee,
                        nonChargeable = parentViewModel?.selectedPop?.nonChargeable,
                        remarks = if (parentViewModel?.transferData?.value?.noteValue.isNullOrBlank()) null else parentViewModel?.transferData?.value?.noteValue?.trim()

                    )
                )
                ) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.transferData?.value?.referenceNumber = response.data.data
                    clickEvent.postValue(Constants.ADD_CASH_PICK_UP_SUCCESS)
                }
                is RetroApiResponse.Error -> {
                    showToast(response.error.message)
                    state.loading = false
                }
            }
            state.loading = false
        }
    }

    override fun domesticTransferRequest(beneficiaryId: String?) {
        launch {
            state.loading = true
            when (val response =
                repository.domesticTransferRequest(
                    SendMoneyTransferRequest(
                        beneficiaryId = beneficiaryId?.toInt(),
                        amount = parentViewModel?.transferData?.value?.transferAmount,
                        settlementAmount = 0.0,
                        purposeCode = parentViewModel?.selectedPop?.purposeCode,
                        purposeReason = parentViewModel?.selectedPop?.purposeDescription,
                        remarks = if (parentViewModel?.transferData?.value?.noteValue.isNullOrBlank()) null else parentViewModel?.transferData?.value?.noteValue?.trim()
                    )

                )
                ) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.transferData?.value?.referenceNumber = response.data.data
                    clickEvent.postValue(Constants.ADD_CASH_PICK_UP_SUCCESS)
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    showToast(response.error.message)
                }
            }
        }
    }

    private fun getProductCode(): String {
        return (when (parentViewModel?.beneficiary?.value?.beneficiaryType) {
            SendMoneyBeneficiaryType.UAEFTS.type -> TransactionProductCode.UAEFTS.pCode

            SendMoneyBeneficiaryType.DOMESTIC.type -> TransactionProductCode.DOMESTIC.pCode

            else -> ""
        })
    }

    private fun checkCBWSI(): Boolean { // true means it is not cbwsi and false mean it is cbwsi transaction
        return parentViewModel?.selectedPop?.let { pop ->
            return (when {
                parentViewModel?.beneficiary?.value?.cbwsicompliant == true &&
                        pop.cbwsi == true -> parentViewModel?.transferData?.value?.transferAmount.parseToDouble() > parentViewModel?.transactionThreshold?.value?.cbwsiPaymentLimit ?: 0.0
                else -> true
            })
        } ?: true
    }
}