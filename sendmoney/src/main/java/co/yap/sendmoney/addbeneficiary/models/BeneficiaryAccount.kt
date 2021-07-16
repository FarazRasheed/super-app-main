package co.yap.sendmoney.addbeneficiary.models

import co.yap.countryutils.country.utils.Currency

data class BeneficiaryAccount(

    var accountNumber: String? = null,
    var iban: String? = null,
    var nickName: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var mobileNumber: String? = null,
    var currency: Currency? = null /*,
    var extraInfoFields: List<InputField>?  */

)