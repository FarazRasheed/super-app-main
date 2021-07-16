package co.yap.networking.cards.responsedtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CardBalance(
    @SerializedName("accountNumber")
    val accountNumber: String? = null,
    @SerializedName("availableBalance")
    val availableBalance: String? = null,
    @SerializedName("currencyCode")
    val currencyCode: String? = null,
    @SerializedName("currencyDecimals")
    val currencyDecimals: String? = null,
    @SerializedName("ledgerBalance")
    val ledgerBalance: String? = null,
    @SerializedName("totalPendingAuth")
    val totalPendingAuth: String? = null
) : Serializable