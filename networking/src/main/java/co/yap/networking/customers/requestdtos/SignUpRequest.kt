package co.yap.networking.customers.requestdtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class SignUpRequest(
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("countryCode")
    val countryCode: String? = null,
    @SerializedName("mobileNo")
    val mobileNo: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("passcode")
    val passcode: String? = null,
    @SerializedName("accountType")
    val accountType: String? = null,
    @SerializedName("token")
    val token: String? = null
) : Serializable
