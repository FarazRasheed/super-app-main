package co.yap.sendmoney.currencyPicker.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class MultiCurrencyWallet(
    var country2DigitCode: String,
    var currencyUnit: String,
    var currencyName: String,
    var position :Int

) : Parcelable {
    fun getCurrencySymbolWithName(): String {
        return "${currencyUnit} ${currencyName}"
    }
}