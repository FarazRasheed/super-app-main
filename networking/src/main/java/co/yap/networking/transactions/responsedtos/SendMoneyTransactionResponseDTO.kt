package co.yap.networking.transactions.responsedtos

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendMoneyTransactionResponseDTO(
    @SerializedName("data")
    var data: String?
) : ApiResponse(), Parcelable