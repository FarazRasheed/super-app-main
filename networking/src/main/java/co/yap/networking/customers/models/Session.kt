package co.yap.networking.customers.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("number")
    val number: String? = null
) : Parcelable