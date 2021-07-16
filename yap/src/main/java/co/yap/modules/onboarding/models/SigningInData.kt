package co.yap.modules.onboarding.models

import co.yap.modules.onboarding.enums.AccountType

data class SigningInData(
    var clientId: String? = null,
    var clientSecret: String? = null,
    var otp: String? = null,
    var deviceID: String? = null,
    var accountType: AccountType? = null,
    var token: String? = null
)