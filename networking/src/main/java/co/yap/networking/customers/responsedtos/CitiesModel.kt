package co.yap.networking.customers.responsedtos

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class CitiesModel(
    @SerializedName("data")
    var data: ArrayList<City>? = arrayListOf(),
    @SerializedName("errors")
    var errors: Any?
) : ApiResponse()

@Parcelize
data class City(
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("cityCode")
    var cityCode: String? = "0",
    @SerializedName("active")
    var active: Boolean? = false
) : Parcelable
