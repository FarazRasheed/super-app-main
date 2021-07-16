package co.yap.sendmoney.addbeneficiary.models

import co.yap.countryutils.country.Country


data class AddBeneficiaryData(

    var country: Country? = null,
    var localBeneficiary: Boolean = false,
    var transferType: MoneyTransferType? = null,
    var beneficiaryAccount: BeneficiaryAccount? = null,// either cash-pickup or local-bank-transfer
    var beneficiaryBank: BeneficiaryBank? = null  /*, // International bank transfer or swift transfer
    var bankSearchCriteriaFields: List<InputField>? = null,
    var extraBankQueries: List<BankQueryField>? = null*/

)