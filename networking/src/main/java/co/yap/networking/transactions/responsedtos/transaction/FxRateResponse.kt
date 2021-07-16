package co.yap.networking.transactions.responsedtos.transaction


import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FxRateResponse(
    @SerializedName("errors")
    val errors: String? = null, // null
    @SerializedName("data")
    val data: Data
) : Parcelable, ApiResponse() {
    @Parcelize
    data class Data(
        @SerializedName("from_currency_code")
        val fromCurrencyCode: String? = null, // AED
        @SerializedName("to_currency_code")
        val toCurrencyCode: String? = null, // AED
        @SerializedName("value")
        val value: Value? = null,
        @SerializedName("date")
        val date: String? = null, // 2019-12-16T13:39:50Z
        @SerializedName("fx_rates")
        val fxRates: List<FxRate>? = arrayListOf(),
        @SerializedName("total_credit_amount")
        val totalCreditAmount: String? = null, // 1.00
        @SerializedName("status")
        val status: String? = null // 200
    ) : Parcelable {
        @Parcelize
        data class Value(
            @SerializedName("currency")
            val currency: String? = null, // AED
            @SerializedName("amount")
            val amount: String? = null // 1
        ) : Parcelable

        @Parcelize
        data class FxRate(
            @SerializedName("rate")
            val rate: String? = null, // 1
            @SerializedName("currency")
            val currency: String? = null,
            @SerializedName("converted_amount")
            val convertedAmount: String? = null // 1.00
        ) : Parcelable
    }
}