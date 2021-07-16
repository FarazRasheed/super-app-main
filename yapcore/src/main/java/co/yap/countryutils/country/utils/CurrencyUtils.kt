package co.yap.countryutils.country.utils

import android.content.Context
import android.text.TextUtils
import co.yap.countryutils.country.Country
import co.yap.yapcore.R
import co.yap.yapcore.managers.SessionManager
import java.util.*


object CurrencyUtils {
    val CURRENCIES = arrayOf(
        Currency(
            code = "EUR",
            name = "Euro",
            symbol = "€",
            flag = R.drawable.flag_eu
        ),
        Currency(
            "USD",
            "United States Dollar",
            "$",
            R.drawable.flag_us
        ),
        Currency(
            "GBP",
            "British Pound",
            "£",
            R.drawable.flag_gb
        ),
        Currency(
            "CZK",
            "Czech Koruna",
            "Kč",
            R.drawable.flag_cz
        ),
        Currency(
            "TRY",
            "Turkish Lira",
            "₺",
            R.drawable.flag_tr
        ),
        Currency(
            SessionManager.getDefaultCurrency(),
            "Emirati Dirham",
            "د.إ",
            R.drawable.flag_ae
        ),
        Currency(
            "AFN",
            "Afghanistan Afghani",
            "؋",
            R.drawable.flag_af
        ),
        Currency(
            "ARS",
            "Argentine Peso",
            "$",
            R.drawable.flag_ar
        ),
        Currency(
            "AUD",
            "Australian Dollar",
            "$",
            R.drawable.flag_au
        ),
        Currency(
            "BBD",
            "Barbados Dollar",
            "$",
            R.drawable.flag_bb
        ),
        Currency(
            "BDT",
            "Bangladeshi Taka",
            " Tk",
            R.drawable.flag_bd
        ),
        Currency(
            "BGN",
            "Bulgarian Lev",
            "лв",
            R.drawable.flag_bg
        ),
        Currency(
            "BHD",
            "Bahraini Dinar",
            "BD",
            R.drawable.flag_bh
        ),
        Currency(
            "BMD",
            "Bermuda Dollar",
            "$",
            R.drawable.flag_bm
        ),
        Currency(
            "BND",
            "Brunei Darussalam Dollar",
            "$",
            R.drawable.flag_bn
        ),
        Currency(
            "BOB",
            "Bolivia Bolíviano",
            "\$b",
            R.drawable.flag_bo
        ),
        Currency(
            "BRL",
            "Brazil Real",
            "R$",
            R.drawable.flag_br
        ),
        Currency(
            "BTN",
            "Bhutanese Ngultrum",
            "Nu.",
            R.drawable.flag_bt
        ),
        Currency(
            "BZD",
            "Belize Dollar",
            "BZ$",
            R.drawable.flag_bz
        ),
        Currency(
            "CAD",
            "Canada Dollar",
            "$",
            R.drawable.flag_ca
        ),
        Currency(
            "CHF",
            "Switzerland Franc",
            "CHF",
            R.drawable.flag_ch
        ),
        Currency(
            "CLP",
            "Chile Peso",
            "$",
            R.drawable.flag_cl
        ),
        Currency(
            "CNY",
            "China Yuan Renminbi",
            "¥",
            R.drawable.flag_cn
        ),
        Currency(
            "COP",
            "Colombia Peso",
            "$",
            R.drawable.flag_co
        ),
        Currency(
            "CRC",
            "Costa Rica Colon",
            "₡",
            R.drawable.flag_cr
        ),
        Currency(
            "DKK",
            "Denmark Krone",
            "kr",
            R.drawable.flag_dk
        ),
        Currency(
            "DOP",
            "Dominican Republic Peso",
            "RD$",
            R.drawable.flag_do
        ),
        Currency(
            "EGP",
            "Egypt Pound",
            "£",
            R.drawable.flag_eg
        ),
        Currency(
            "ETB",
            "Ethiopian Birr",
            "Br",
            R.drawable.flag_et
        ),
        Currency(
            "GEL",
            "Georgian Lari",
            "₾",
            R.drawable.flag_ge
        ),
        Currency(
            "GHS",
            "Ghana Cedi",
            "¢",
            R.drawable.flag_gh
        ),
        Currency(
            "GMD",
            "Gambian dalasi",
            "D",
            R.drawable.flag_gm
        ),
        Currency(
            "GYD",
            "Guyana Dollar",
            "$",
            R.drawable.flag_gy
        ),
        Currency(
            "HKD",
            "Hong Kong Dollar",
            "$",
            R.drawable.flag_hk
        ),
        Currency(
            "HRK",
            "Croatia Kuna",
            "kn",
            R.drawable.flag_hr
        ),
        Currency(
            "HUF",
            "Hungary Forint",
            "Ft",
            R.drawable.flag_hu
        ),
        Currency(
            "IDR",
            "Indonesia Rupiah",
            "Rp",
            R.drawable.flag_id
        ),
        Currency(
            "ILS",
            "Israel Shekel",
            "₪",
            R.drawable.flag_il
        ),
        Currency(
            "INR",
            "Indian Rupee",
            "₹",
            R.drawable.flag_in
        ),
        Currency(
            "ISK",
            "Iceland Krona",
            "kr",
            R.drawable.flag_is
        ),
        Currency(
            "JMD",
            "Jamaica Dollar",
            "J$",
            R.drawable.flag_jm
        ),
        Currency(
            "JPY",
            "Japanese Yen",
            "¥",
            R.drawable.flag_jp
        ),
        Currency(
            "KES",
            "Kenyan Shilling",
            "KSh",
            R.drawable.flag_ke
        ),
        Currency(
            "KRW",
            "Korea (South) Won",
            "₩",
            R.drawable.flag_kr
        ),
        Currency(
            "KWD",
            "Kuwaiti Dinar",
            "د.ك",
            R.drawable.flag_kw
        ),
        Currency(
            "KYD",
            "Cayman Islands Dollar",
            "$",
            R.drawable.flag_ky
        ),
        Currency(
            "KZT",
            "Kazakhstan Tenge",
            "лв",
            R.drawable.flag_kz
        ),
        Currency(
            "LAK",
            "Laos Kip",
            "₭",
            R.drawable.flag_la
        ),
        Currency(
            "LKR",
            "Sri Lanka Rupee",
            "₨",
            R.drawable.flag_lk
        ),
        Currency(
            "LRD",
            "Liberia Dollar",
            "$",
            R.drawable.flag_lr
        ),
        Currency(
            "LTL",
            "Lithuanian Litas",
            "Lt",
            R.drawable.flag_lt
        ),
        Currency(
            "MAD",
            "Moroccan Dirham",
            "MAD",
            R.drawable.flag_ma
        ),
        Currency(
            "MDL",
            "Moldovan Leu",
            "MDL",
            R.drawable.flag_md
        ),
        Currency(
            "MNT",
            "Mongolia Tughrik",
            "₮",
            R.drawable.flag_mn
        ),
        Currency(
            "MUR",
            "Mauritius Rupee",
            "₨",
            R.drawable.flag_mu
        ),
        Currency(
            "MWK",
            "Malawian Kwacha",
            "MK",
            R.drawable.flag_mw
        ),
        Currency(
            "MXN",
            "Mexico Peso",
            "$",
            R.drawable.flag_mx
        ),
        Currency(
            "MYR",
            "Malaysia Ringgit",
            "RM",
            R.drawable.flag_my
        ),
        Currency(
            "MZN",
            "Mozambique Metical",
            "MT",
            R.drawable.flag_mz
        ),
        Currency(
            "NAD",
            "Namibia Dollar",
            "$",
            R.drawable.flag_na
        ),
        Currency(
            "NGN",
            "Nigeria Naira",
            "₦",
            R.drawable.flag_ng
        ),
        Currency(
            "NIO",
            "Nicaragua Cordoba",
            "C$",
            R.drawable.flag_ni
        ),
        Currency(
            "NOK",
            "Norway Krone",
            "kr",
            R.drawable.flag_no
        ),
        Currency(
            "NPR",
            "Nepal Rupee",
            "₨",
            R.drawable.flag_np
        ),
        Currency(
            "NZD",
            "New Zealand Dollar",
            "$",
            R.drawable.flag_nz
        ),
        Currency(
            "OMR",
            "Oman Rial",
            "﷼",
            R.drawable.flag_om
        ),
        Currency(
            "PEN",
            "Peru Sol",
            "S/.",
            R.drawable.flag_pe
        ),
        Currency(
            "PGK",
            "Papua New Guinean Kina",
            "K",
            R.drawable.flag_pg
        ),
        Currency(
            "PHP",
            "Philippines Peso",
            "₱",
            R.drawable.flag_ph
        ),
        Currency(
            "PKR",
            "Pakistan Rupee",
            "₨",
            R.drawable.flag_pk
        ),
        Currency(
            "PYG",
            "Paraguay Guarani",
            "Gs",
            R.drawable.flag_py
        ),
        Currency(
            "QAR",
            "Qatar Riyal",
            "﷼",
            R.drawable.flag_qa
        ),
        Currency(
            "RON",
            "Romania Leu",
            "lei",
            R.drawable.flag_ro
        ),
        Currency(
            "RSD",
            "Serbia Dinar",
            "Дин.",
            R.drawable.flag_rs
        ),
        Currency(
            "RUB",
            "Russia Ruble",
            "₽",
            R.drawable.flag_ru
        ),
        Currency(
            "SAR",
            "Saudi Arabia Riyal",
            "﷼",
            R.drawable.flag_sa
        ),
        Currency(
            "SEK",
            "Sweden Krona",
            "kr",
            R.drawable.flag_se
        ),
        Currency(
            "SGD",
            "Singapore Dollar",
            "$",
            R.drawable.flag_sg
        ),
        Currency(
            "SOS",
            "Somalia Shilling",
            "S",
            R.drawable.flag_so
        ),
        Currency(
            "SRD",
            "Suriname Dollar",
            "$",
            R.drawable.flag_sr
        ),
        Currency(
            "THB",
            "Thailand Baht",
            "฿",
            R.drawable.flag_th
        ),
        Currency(
            "TTD",
            "Trinidad and Tobago Dollar",
            "TT$",
            R.drawable.flag_tt
        ),
        Currency(
            "TWD",
            "Taiwan New Dollar",
            "NT$",
            R.drawable.flag_tw
        ),
        Currency(
            "TZS",
            "Tanzanian Shilling",
            "TSh",
            R.drawable.flag_tz
        ),
        Currency(
            "UAH",
            "Ukraine Hryvnia",
            "₴",
            R.drawable.flag_ua
        ),
        Currency(
            "UGX",
            "Ugandan Shilling",
            "USh",
            R.drawable.flag_ug
        ),
        Currency(
            "UYU",
            "Uruguay Peso",
            "\$U",
            R.drawable.flag_uy
        ),
        Currency(
            "VEF",
            "Venezuela Bolívar",
            "Bs",
            R.drawable.flag_ve
        ),
        Currency(
            "VND",
            "Viet Nam Dong",
            "₫",
            R.drawable.flag_vn
        ),
        Currency(
            "YER",
            "Yemen Rial",
            "﷼",
            R.drawable.flag_ye
        ),
        Currency(
            "ZAR",
            "South Africa Rand",
            "R",
            R.drawable.flag_za
        )
    )

    /*
     *      GENERIC STATIC FUNCTIONS
     */

    private var allCurrenciesList: List<Currency>? = null

    val allCurrencies: List<Currency>?
        get() {
            if (allCurrenciesList == null) {
                allCurrenciesList = Arrays.asList<Currency>(*CURRENCIES)
            }
            return allCurrenciesList
        }

//    val allCurrencies: List<Currency>?
//        get() {
//            fun getAllCurrencies(): List<Currency> {
//                if (allCurrenciesList == null) {
//                    allCurrenciesList = Arrays.asList<Currency>(*CURRENCIES)
//                }
//                return allCurrenciesList
//            }
//        }

    fun getCurrencyByISO(currencyIsoCode: String): Currency? {
        // Because the dataList we have is sorted by ISO codes and not by names, we must check all
        // currencies one by one

        for (c in CURRENCIES) {
            if (currencyIsoCode == c.code) {
                return c
            }
        }
        return null
    }

    fun getCurrencyByName(currencyName: String): Currency? {
        // Because the dataList we have is sorted by ISO codes and not by names, we must check all
        // currencies one by one

        for (c in CURRENCIES) {
            if (currencyName == c.name) {
                return c
            }
        }
        return null
    }

    fun getCurrencyByCode(code: String): Currency? {
        for (c in CURRENCIES) {
            if (code == c.code) {
                return c
            }
        }
        return null
    }

    fun getCurrencyByCountryCode(countryCode: String): Currency? {
        // Because the dataList we have is sorted by ISO codes and not by names, we must check all
        // currencies one by one

        val locale = Locale("", countryCode)
        var currencyCode = ""
        try {
            val currency = java.util.Currency.getInstance(locale)
            if (currency != null) {
                currencyCode = currency.currencyCode
            }
        } catch (e: IllegalArgumentException) {

        } catch (e: NullPointerException) {

        }

        return if (!TextUtils.isEmpty(currencyCode)) {
            getCurrencyByCode(
                currencyCode
            )
        } else null

    }

    fun getFlagByCurrencyCode(context: Context, code: String): Int {
        try {
            return context.resources
                .getIdentifier(
                    "flag_" + code.toLowerCase(), "drawable",
                    context.packageName
                )
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }

    }

    fun getCountriesList(context: Context): List<Country> {
        val codes = Locale.getISOCountries()
        val countries = ArrayList<Country>()
        for (countryCode in codes) {
            val loc = Locale("", countryCode)
            val countryName = loc.displayCountry
            val c = Country()
            c.setName(countryName)
            c.isoCountryCode2Digit = countryCode.toString()
            if (countryName.length > 0 && !countries.contains(c)) {
                countries.add(c)
                val flagResName = "flag_" + countryCode.toLowerCase()
                val flag =
                    getFlagDrawable(
                        context,
                        flagResName
                    )
                c.setFlagDrawableResId(flag)
            }
        }
        return countries
    }


    fun getFlagDrawable(context: Context, resName: String): Int {
        if (!TextUtils.isEmpty(resName)) {
            var name = resName
            if (resName.length == 2 || resName.length == 3) {
                // it is probably a country iso isoCountryCode2Digit
                name = "flag_" + resName.toLowerCase()
            }
            return context.resources.getIdentifier(
                name,
                "drawable",
                context.packageName
            )
        }
        return 0
    }

//    fun getFlagDrawable(countryIsoName: String): Int {
//        return getFlagDrawable(
//            Utils.context!!,
//            countryIsoName
//        )
//    }

    fun getCountryName(countryCode: String): String {
        if (TextUtils.isEmpty(countryCode)) return ""
        val loc = Locale("", countryCode)
        return loc.displayCountry
    }

    fun getCurrencySymbol(currencyCode: String): String {
        val currency = java.util.Currency.getInstance(currencyCode)
        return currency.symbol
    }


}
