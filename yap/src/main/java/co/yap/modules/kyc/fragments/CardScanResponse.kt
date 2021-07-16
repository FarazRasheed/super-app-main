package co.yap.modules.kyc.fragments

data class CardScanResponse(
    val check_composite: String,
    val check_date_of_birth: String,
    val check_expiration_date: String,
    val check_number: String,
    val country: String,
    val date_of_birth: String,
    val expiration_date: String,
    val method: String,
    val mrz_type: String,
    val names: String,
    val nationality: String,
    val number: String,
    val optional1: String,
    val optional2: String,
    val sex: String,
    val success: Boolean,
    val surname: String,
    val type: String,
    val valid_composite: Boolean,
    val valid_date_of_birth: Boolean,
    val valid_expiration_date: Boolean,
    val valid_number: Boolean,
    val valid_score: Int
)