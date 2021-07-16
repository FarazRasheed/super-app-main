package co.yap.networking.cards.responsedtos

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Card(
    val newPin: String?,
    var cardType: String,
    val uuid: String,
    var physical: Boolean,
    val active: Boolean,
    var cardName: String?,
    var nameUpdated: Boolean?,
    var status: String,
    val shipmentStatus: String?,
    val deliveryStatus: String?,
    var blocked: Boolean,
    val delivered: Boolean,
    var cardSerialNumber: String,
    var maskedCardNo: String,
    var atmAllowed: Boolean,
    var onlineBankingAllowed: Boolean,
    var retailPaymentAllowed: Boolean,
    var paymentAbroadAllowed: Boolean,
    var accountType: String,
    val expiryDate: String,
    var cardBalance: String,
    val cardScheme: String,
    val currentBalance: String,
    var availableBalance: String,
    val customerId: String,
    var pinCreated: Boolean,
    val accountNumber: String,
    val productCode: String,
    var shipmentDate: String? = null,
    var activationDate: String? = null
) : Parcelable