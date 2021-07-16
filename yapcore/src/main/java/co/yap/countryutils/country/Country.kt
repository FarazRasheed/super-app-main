package co.yap.countryutils.country

import android.content.Context
import android.os.Parcelable
import co.yap.countryutils.country.utils.Currency
import co.yap.countryutils.country.utils.CurrencyUtils
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    var id: Int? = null,
    var isoCountryCode3Digit: String? = null,
    private var cashPickUpAllowed: Boolean? = false,
    var isoCountryCode2Digit: String? = null,
    var supportedCurrencies: List<Currency>? = null,
    var active: Boolean? = false,
    var isoNum: String? = "",
    var signUpAllowed: Boolean? = false,
    private var name: String? = null,
    private var flagDrawableResId: Int = -1,
    private var currency: Currency? = null,
    var ibanMandatory: Boolean? = false
) : Parcelable {
    var isCashPickUpAllowed: Boolean?
        get() {
            val size = supportedCurrencies!!.size
            if (size > 0) {
                val mainCurrency = supportedCurrencies!![size - 1]
                return mainCurrency.isCashPickUpAllowed
            }
            return this.cashPickUpAllowed
        }
        @Deprecated("")
        set(cashPickUpAllowed) {
            this.cashPickUpAllowed = cashPickUpAllowed
        }


    fun getName(): String {
        if (name == null) name = ""
        return name as String
    }

    fun setName(name: String) {
        this.name = name
    }


    fun getCurrency(): Currency? {
        if (currency == null) {
            // First Check if we can get the main currency from list of supported currencies.
            val localCurrency =
                CurrencyUtils.getCurrencyByCountryCode(
                    isoCountryCode2Digit!!
                )
            for (c in supportedCurrencies!!) {
                if (c.equals(localCurrency)) {
                    currency = localCurrency
                    break
                }
            }
            if (currency == null) {
                val c = supportedCurrencies!![0]
                // find currency from utils with flag and symbol etc
                currency =
                    CurrencyUtils.getCurrencyByCode(
                        c.code!!
                    )
            }
        }
        return currency
    }

    fun getCurrencySM(): Currency? {
        return currency
    }

    fun setCurrency(currency: Currency) {
        this.currency = currency
    }

    override fun equals(obj: Any?): Boolean {
        val equals = (obj as Country).getName().equals(getName(), ignoreCase = true)
        return equals || super.equals(obj)
    }

    fun getFlagDrawableResId(context: Context): Int {
        if (flagDrawableResId <= 0) {
            if (!isoCountryCode2Digit.isNullOrEmpty())
                flagDrawableResId = CurrencyUtils.getFlagDrawable(context, isoCountryCode2Digit!!)
        }
        return flagDrawableResId
    }

    fun setFlagDrawableResId(flagDrawableResId: Int) {
        this.flagDrawableResId = flagDrawableResId
    }

}
