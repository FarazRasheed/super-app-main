package co.yap.networking.customers.requestdtos

import co.yap.networking.customers.models.Session
import com.google.gson.annotations.SerializedName

data class CreateBeneficiaryRequest(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("session")
    val session: Session
)