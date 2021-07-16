package co.yap.networking.messages.responsedtos

import com.google.gson.annotations.SerializedName

data class OtpValidation(
    @SerializedName("otpToken") var token: String? = "",
    @SerializedName("waitingListRank") var rankNo: String? = "",
    @SerializedName("isWaiting") var isWaiting: Boolean? = false
) {
}