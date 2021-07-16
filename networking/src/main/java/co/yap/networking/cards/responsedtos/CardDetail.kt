package co.yap.networking.cards.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CardDetail(
    @SerializedName("productCode")
    val productCode: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("displayName")
    val displayName: String? = null,
    @SerializedName("customerId")
    val customerId: String? = null,
    @SerializedName("mobileNumber")
    val mobileNumber: String? = null,
    @SerializedName("emailIdvar")
    val emailIdvar: String? = null,
    @SerializedName("proxyNumber")
    val proxyNumber: String? = null,
    @SerializedName("cvv")
    val cvv: String? = null,
    @SerializedName("cardNumber")
    val cardNumber: String? = null,
    @SerializedName("expiryDate")
    val expiryDate: String? = null,
    @SerializedName("activationDate")
    val activationDate: String? = null,
    @SerializedName("cardStatus")
    val cardStatus: Int? = null,
    @SerializedName("nonSoleProprietary")
    val nonSoleProprietary: Boolean = false
) : Parcelable