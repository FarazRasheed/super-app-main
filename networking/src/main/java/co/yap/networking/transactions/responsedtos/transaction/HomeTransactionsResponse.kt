package co.yap.networking.transactions.responsedtos.transaction

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class HomeTransactionsResponse(
    @SerializedName("data")
    var data: HomeTransactionListData,
    @SerializedName("errors")
    var errors: Any?
) : ApiResponse()