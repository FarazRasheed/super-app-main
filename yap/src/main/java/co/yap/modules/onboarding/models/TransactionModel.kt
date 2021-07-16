package co.yap.modules.onboarding.models

data class TransactionModel(
    val vendor: String,
    var amount: Double,
    var amountPercentage: Double,
    var type: String,
    var dateTime: String,
    var category: String,
    var currency: String
)