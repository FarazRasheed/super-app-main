package co.yap.networking.transactions.requestdtos

import android.os.Parcelable
import co.yap.networking.customers.models.Session
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Check3DEnrollmentSessionRequest(
    @SerializedName("beneficiaryId")
    val beneficiaryId:Int? = null,
    @SerializedName("order")
    val order: Order?= null,
    @SerializedName("session")
    val session: Session?=null
):Parcelable