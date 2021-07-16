package co.yap.networking.cards.responsedtos

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AtmCdmResponse
    (
    @SerializedName("data")
    val data: List<AtmCdmData?>? = ArrayList()
) : ApiResponse(), Parcelable

@Parcelize
data class AtmCdmData(
    @SerializedName("address1")
    val address1: String? = null,
    @SerializedName("address2")
    val address2: String? = null,
    @SerializedName("area")
    val area: String? = null,
    @SerializedName("atmId")
    val atmId: String? = null,
    @SerializedName("atmUuid")
    val atmUuid: String? = null,
    @SerializedName("bank")
    val bank: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("createdBy")
    val createdBy: String? = null,
    @SerializedName("createdDate")
    val createdDate: String? = null,
    @SerializedName("creationDate")
    val creationDate: String? = null,
    @SerializedName("emirates")
    val emirates: String? = null,
    @SerializedName("isActive")
    val isActive: Boolean? = null,
    @SerializedName("latitude")
    val latitude: String? = null,
    @SerializedName("longitude")
    val longitude: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("updateDate")
    val updateDate: String? = null,
    @SerializedName("updatedBy")
    val updatedBy: String? = null,
    @SerializedName("updatedDate")
    val updatedDate: String? = null
) : Parcelable