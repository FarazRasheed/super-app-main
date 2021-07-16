package co.yap.networking.transactions.responsedtos.topuptransactionsession

import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName

data class CreateTransactionSessionResponseDTO(@SerializedName("data") val data: CreateSessionResponseObject) :
    ApiResponse()