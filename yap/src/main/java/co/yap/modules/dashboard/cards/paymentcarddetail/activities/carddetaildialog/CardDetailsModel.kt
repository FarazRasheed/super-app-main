package co.yap.modules.dashboard.cards.paymentcarddetail.activities.carddetaildialog

data class CardDetailsModel(
    var cardType: String? = "",
    var cardNumber: String? = "",
    var cardExpiry: String? = "",
    var cardCvv: String? = "",
    var displayName: String? = ""
) {
}