package co.yap.sendmoney.fundtransfer.models

data class TransferFundData(
    var position: Int? = 0,
    var noteValue: String? = null,
    var transferAmount: String? = "",
    var transferFee: String? = "0.00",
    var cutOffTimeMsg: String? = null,
    var referenceNumber: String? = null,
    var otpAction: String? = null,
    var productCode: String? = null,
    var feeAmount: String? = "0.0",
    var vat: String? = "0.0",
//specific international transfer data
    var sourceCurrency: String? = null,
    var sourceAmount: String? = null,
    var destinationCurrency: String? = null,
    var destinationAmount: String? = null,
    var toFxRate: String? = null,
    var fromFxRate: String? = null,
    var rate: String? = null

)