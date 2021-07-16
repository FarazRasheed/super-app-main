package co.yap.networking.transactions.responsedtos.topuptransactionsession

import co.yap.networking.customers.models.Session
import com.google.gson.annotations.SerializedName

data class CreateSessionResponseObject(
    @SerializedName("order") val order: OrderResponseDTO,
    @SerializedName("session") val session: Session
) {
}