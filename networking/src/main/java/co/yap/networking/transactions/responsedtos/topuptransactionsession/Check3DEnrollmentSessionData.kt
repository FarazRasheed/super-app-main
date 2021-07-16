package co.yap.networking.transactions.responsedtos.topuptransactionsession

import com.google.gson.annotations.SerializedName

class Check3DEnrollmentSessionData(
    @SerializedName("3DSecureId") val _3DSecureId: String,
    @SerializedName("3DSecure") val _3DSecure: Enrollment3DSecure
)