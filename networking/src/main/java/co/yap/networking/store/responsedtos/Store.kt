package co.yap.networking.store.responsedtos

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Store(
    var id: Int?=0,
    @SerializedName("title")
    var name: String?=null,
    @SerializedName("description")
    var desc: String?=null,
    @SerializedName("urlToImage")
    var image: Int?=0,
    var storeIcon: Int?=0
) : Parcelable

@Parcelize
data class StoreParent(
    @SerializedName("totalResults")
    var id: Int?=0,
    @SerializedName("status")
    var name: String?=null,
    @SerializedName("articles")
    var stores: List<Store>?=null
) : ApiResponse(), Parcelable