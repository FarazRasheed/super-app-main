package co.yap.networking.household.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HouseHoldPlan(
    @SerializedName("planType")
    var type: String? = null,
    @SerializedName("amount")
    var amount: String? = null,
    @SerializedName("discount")
    var discount: Int? = 0
): Parcelable