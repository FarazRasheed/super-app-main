package co.yap.networking.customers.responsedtos

import com.google.gson.annotations.SerializedName

data class  AppUpdate(
    @SerializedName("androidAppVersionDate")
    var androidAppVersionDate: String,
    @SerializedName("androidAppVersionNumber")
    var androidAppVersionNumber: String,
    @SerializedName("androidForceUpdate")
    var androidForceUpdate: Boolean,
    @SerializedName("androidReleaseNotes")
    var androidReleaseNotes: String,
    @SerializedName("createdBy")
    var createdBy: String,
    @SerializedName("createdOn")
    var createdOn: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("iosAppVersionDate")
    var iosAppVersionDate: String,
    @SerializedName("iosAppVersionNumber")
    var iosAppVersionNumber: String,
    @SerializedName("iosForceUpdate")
    var iosForceUpdate: Boolean,
    @SerializedName("iosReleaseNotes")
    var iosReleaseNotes: String,
    @SerializedName("updatedBy")
    var updatedBy: String,
    @SerializedName("updatedOn")
    var updatedOn: String
)