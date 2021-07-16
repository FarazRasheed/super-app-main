package co.yap.networking.transactions.responsedtos

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName


data class CutOffTime(
    @SerializedName("data")
    val `data`: CutOffData? = null
):ApiResponse()

data class CutOffData(
    @SerializedName("active")
    val active: Boolean? = null,
    @SerializedName("configType")
    val configType: String? = null,
    @SerializedName("createdBy")
    val createdBy: String? = null,
    @SerializedName("creationDate")
    val creationDate: String? = null,
    @SerializedName("currency")
    val currency: String? = null,
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("errorMsg")
    val errorMsg: String? = null,
    @SerializedName("productCode")
    val productCode: String? = null,
    @SerializedName("productName")
    val productName: String? = null,
    @SerializedName("updatedDate")
    val updatedDate: String? = null
):ApiResponse()