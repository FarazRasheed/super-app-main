package co.yap.sendmoney.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.networking.models.RetroApiResponse
import co.yap.networking.transactions.TransactionsRepository
import co.yap.networking.transactions.requestdtos.RemittanceFeeRequest
import co.yap.networking.transactions.responsedtos.transaction.RemittanceFeeResponse
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase
import co.yap.yapcore.enums.FeeType
import co.yap.yapcore.helpers.extentions.parseToDouble

abstract class SMFeeViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {
    private val transactionRepository: TransactionsRepository = TransactionsRepository
    var feeTiers: List<RemittanceFeeResponse.RemittanceFee.TierRateDTO> = arrayListOf()
    var isFeeReceived: MutableLiveData<Boolean> = MutableLiveData(false)
    var isAPIFailed: MutableLiveData<Boolean> = MutableLiveData(false)
    var feeType: String = ""
    var feeCurrency: String = "AED"
    private var fixedAmount: Double = 0.0
    var feeAmount: String = ""
    var vat: String = ""
    val updatedFee: MutableLiveData<String> = MutableLiveData("0.0")

    fun getTransferFees(
        productCode: String?,
        feeRequest: RemittanceFeeRequest = RemittanceFeeRequest()
    ) {
        launch {
            when (val response =
                transactionRepository.getTransactionFeeWithProductCode(productCode, feeRequest)) {
                is RetroApiResponse.Success -> {
                    feeType = response.data.data?.feeType ?: "FLAT"
                    feeCurrency = response.data.data?.feeCurrency ?: "AED"
                    fixedAmount = response.data.data?.fixedAmount ?: 0.0
                    response.data.data?.tierRateDTOList?.let {
                        feeTiers =
                            response.data.data?.tierRateDTOList as ArrayList<RemittanceFeeResponse.RemittanceFee.TierRateDTO>
                        isFeeReceived.value = true
                    }
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                    isAPIFailed.value = true
                }
            }
        }
    }

    fun updateFees(enterAmount: String, isTopUpFee: Boolean = false) {
        var result = "0.0"
        if (!feeTiers.isNullOrEmpty()) {
            result = when (feeType) {
                FeeType.FLAT.name -> getFlatFee(enterAmount, isTopUpFee).toString()
                FeeType.TIER.name -> getFeeFromTier(enterAmount, isTopUpFee).toString()
                else -> {
                    "0.0"
                }
            }
        }
        updatedFee.value = result
    }

    fun getFeeFromTier(
        enterAmount: String,
        isTopUpFee: Boolean = false
    ): String? {
        return if (!enterAmount.isBlank()) {
            val fee = feeTiers.firstOrNull { item ->
                item.amountFrom ?: 0.0 <= enterAmount.parseToDouble() && item.amountTo ?: 0.0 >= enterAmount.parseToDouble()
            } ?: return "0.0"
            if (fee.feeInPercentage == false) {
                (fee.feeAmount ?: 0.0).plus(fee.vatAmount ?: 0.0).plus(fixedAmount).toString()
            } else {
                if (isTopUpFee) getFeeInPercentageForTopup(
                    enterAmount,
                    fee
                ) else calFeeInPercentage(
                    enterAmount,
                    fee
                )
            }
        } else {
            "0.0"
        }
    }

    fun getFlatFee(
        enterAmount: String,
        isTopUpFee: Boolean = false
    ): String? {
        val fee = feeTiers.firstOrNull() ?: return "0.0"
        return if (fee.feeInPercentage == false) {
            feeAmount = if (fee.feeAmount == null) "0.0" else fee.feeAmount.toString()
            vat = if (fee.vatAmount == null) "0.0" else fee.vatAmount.toString()
            (fee.feeAmount ?: 0.0).plus(fee.vatAmount ?: 0.0).plus(fixedAmount).toString()
        } else {
            return if (isTopUpFee) getFeeInPercentageForTopup(
                enterAmount,
                fee
            ) else calFeeInPercentage(
                enterAmount,
                fee
            )
        }
    }

    private fun calFeeInPercentage(
        enterAmount: String,
        fee: RemittanceFeeResponse.RemittanceFee.TierRateDTO
    ): String? {
        val feeAmount =
            enterAmount.parseToDouble() * (fee.feePercentage?.parseToDouble()?.div(100) ?: 0.0)
        val vatAmount =
            (feeAmount + fixedAmount) * (fee.vatPercentage?.parseToDouble()?.div(100) ?: 0.0)
        this.feeAmount = feeAmount.toString()
        this.vat = vatAmount.toString()
        return (feeAmount + vatAmount+ fixedAmount).toString()
    }

    // Update only in remitience fixedAmount
    private fun getFeeInPercentageForTopup(
        enterAmount: String,
        fee: RemittanceFeeResponse.RemittanceFee.TierRateDTO
    ): String? {
        // Fee double taxation removal ticket YM-7999
        return calFeeInPercentage(enterAmount, fee)
//        val feeAmount =
//            enterAmount.parseToDouble() * (fee.feePercentage?.parseToDouble()?.div(100)
//                ?: 0.0)
//
//        val totalFeeAmount =
//            (feeAmount * (fee.feePercentage?.parseToDouble()?.div(100) ?: 0.0)).plus(feeAmount)
//
//        val vatAmount =
//            totalFeeAmount * (fee.vatPercentage?.parseToDouble()?.div(100) ?: 0.0)
//
//        this.feeAmount = totalFeeAmount.toString()
//        this.vat = vatAmount.toString()
//        return (totalFeeAmount + vatAmount).toString()
    }
}