package co.yap.widgets.mobile

import android.content.Context
import android.util.Log
import co.yap.yapcore.R
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.text.Collator
import java.util.*

class CCPCountry : Comparable<CCPCountry> {
    lateinit var nameCode: String
    lateinit var phoneCode: String
    lateinit var name: String
    var englishName: String? = null
    internal var flagResID = DEFAULT_FLAG_RES

    val flagID: Int
        get() {
            if (flagResID == -99) {
                flagResID = getFlagMasterResID(this)
            }
            return flagResID
        }

    constructor()

    fun getphoneCode(): String {
        return phoneCode
        var name: String = "971"
        if (null != phoneCode && ::phoneCode.isInitialized) {
            name = phoneCode
        }
        return name
    }

    fun getnameCode(): String {
        var name: String = "ae"
        if (null != nameCode && ::nameCode.isInitialized) {
            name = nameCode
        }
        return name
    }

    constructor(nameCode: String, phoneCode: String, name: String, flagResID: Int) {
        this.nameCode = nameCode.toUpperCase(Locale.ROOT)
        this.phoneCode = phoneCode
        this.name = name
        this.flagResID = flagResID
    }

    fun log() {
        try {
        } catch (ex: NullPointerException) {
        }

    }

    internal fun logString(): String {
        return nameCode.toUpperCase() + " +" + phoneCode + "(" + name + ")"
    }

    internal fun isEligibleForQuery(query: String): Boolean {
        var query = query
        query = query.toLowerCase()
        return name.toLowerCase().contains(query) || nameCode.toLowerCase().contains(query) || phoneCode.toLowerCase().contains(
            query
        ) || getEnglishName(this)!!.toLowerCase().contains(query)
    }

    override fun compareTo(o: CCPCountry): Int {
        return Collator.getInstance().compare(name, o.name)
    }

    companion object {

        internal var DEFAULT_FLAG_RES = -99
        internal var TAG = "Class Country"
        private var loadedLibraryMasterListLanguage: CountryCodePicker.Language? = null
        private var dialogTitle: String? = null
        private var searchHintMessage: String? = null
        private var noResultFoundAckMessage: String? = null
        var loadedLibraryMaterList: List<CCPCountry>? = null
            internal set
        //countries with +1
        private val ANTIGUA_AND_BARBUDA_AREA_CODES = "268"
        private val ANGUILLA_AREA_CODES = "264"
        private val BARBADOS_AREA_CODES = "246"
        private val BERMUDA_AREA_CODES = "441"
        private val BAHAMAS_AREA_CODES = "242"
        private val CANADA_AREA_CODES =
            "204/226/236/249/250/289/306/343/365/403/416/418/431/437/438/450/506/514/519/579/581/587/600/604/613/639/647/705/709/769/778/780/782/807/819/825/867/873/902/905/"
        private val DOMINICA_AREA_CODES = "767"
        private val DOMINICAN_REPUBLIC_AREA_CODES = "809/829/849"
        private val GRENADA_AREA_CODES = "473"
        private val JAMAICA_AREA_CODES = "876"
        private val SAINT_KITTS_AND_NEVIS_AREA_CODES = "869"
        private val CAYMAN_ISLANDS_AREA_CODES = "345"
        private val SAINT_LUCIA_AREA_CODES = "758"
        private val MONTSERRAT_AREA_CODES = "664"
        private val PUERTO_RICO_AREA_CODES = "787"
        private val SINT_MAARTEN_AREA_CODES = "721"
        private val TURKS_AND_CAICOS_ISLANDS_AREA_CODES = "649"
        private val TRINIDAD_AND_TOBAGO_AREA_CODES = "868"
        private val SAINT_VINCENT_AND_THE_GRENADINES_AREA_CODES = "784"
        private val BRITISH_VIRGIN_ISLANDS_AREA_CODES = "284"
        private val US_VIRGIN_ISLANDS_AREA_CODES = "340"


         fun getCountryForCode(
            context: Context,
            language: CountryCodePicker.Language,
            preferredCountries: List<CCPCountry>,
            code: Int
        ): CCPCountry? {
            return getCountryForCode(context, language, preferredCountries, code.toString() + "")
        }

         fun getCountryForCode(
            context: Context,
            language: CountryCodePicker.Language,
            preferredCountries: List<CCPCountry>?,
            code: String
        ): CCPCountry? {

            if (preferredCountries != null && !preferredCountries.isEmpty()) {
                for (CCPCountry in preferredCountries) {
                    if (CCPCountry.phoneCode == code) {
                        return CCPCountry
                    }
                }
            }

            for (CCPCountry in getLibraryMasterCountryList(context, language)!!) {
                if (CCPCountry.phoneCode == code) {
                    return CCPCountry
                }
            }
            return null
        }

        private fun loadDataFromXML(context: Context, language: CountryCodePicker.Language) {
            var countries: MutableList<CCPCountry> = ArrayList()
            var tempDialogTitle = ""
            var tempSearchHint = ""
            var tempNoResultAck = ""
            try {
                val xmlFactoryObject = XmlPullParserFactory.newInstance()
                val xmlPullParser = xmlFactoryObject.newPullParser()
                val ins = context.resources.openRawResource(
                    context.resources
                        .getIdentifier(
                            "ccp_" + language.toString().toLowerCase(Locale.ROOT),
                            "raw", context.packageName
                        )
                )
                xmlPullParser.setInput(ins, "UTF-8")
                var event = xmlPullParser.eventType
                while (event != XmlPullParser.END_DOCUMENT) {
                    val name = xmlPullParser.name
                    when (event) {
                        XmlPullParser.START_TAG -> {
                        }
                        XmlPullParser.END_TAG -> if (name == "country") {
                            val ccpCountry = CCPCountry()
                            ccpCountry.nameCode = xmlPullParser.getAttributeValue(null, "name_code").toUpperCase()
                            ccpCountry.phoneCode = xmlPullParser.getAttributeValue(null, "phone_code")
                            Companion.setEnglishName(ccpCountry, xmlPullParser.getAttributeValue(null, "english_name"))

                            ccpCountry.name = xmlPullParser.getAttributeValue(null, "name")
                            countries.add(ccpCountry)
                        } else if (name == "ccp_dialog_title") {
                            tempDialogTitle = xmlPullParser.getAttributeValue(null, "translation")
                        } else if (name == "ccp_dialog_search_hint_message") {
                            tempSearchHint = xmlPullParser.getAttributeValue(null, "translation")
                        } else if (name == "ccp_dialog_no_result_ack_message") {
                            tempNoResultAck = xmlPullParser.getAttributeValue(null, "translation")
                        }
                    }
                    event = xmlPullParser.next()
                }
                loadedLibraryMasterListLanguage = language
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

            }

            if (countries.size == 0) {
                loadedLibraryMasterListLanguage = CountryCodePicker.Language.ENGLISH
                countries = libraryMasterCountriesEnglish
            }

            dialogTitle = if (tempDialogTitle.length > 0) tempDialogTitle else "Select a country"
            searchHintMessage = if (tempSearchHint.length > 0) tempSearchHint else "Search..."
            noResultFoundAckMessage = if (tempNoResultAck.length > 0) tempNoResultAck else "Results not found"
            loadedLibraryMaterList = countries

            // sort list
            Collections.sort(loadedLibraryMaterList)
        }

        fun getDialogTitle(context: Context, language: CountryCodePicker.Language): String? {
            if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage !== language || dialogTitle == null || dialogTitle!!.length == 0) {
                loadDataFromXML(context, language)
            }
            return dialogTitle
        }

        fun getSearchHintMessage(context: Context, language: CountryCodePicker.Language): String? {
            if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage !== language || searchHintMessage == null || searchHintMessage!!.length == 0) {
                loadDataFromXML(context, language)
            }
            return searchHintMessage
        }

        fun getNoResultFoundAckMessage(context: Context, language: CountryCodePicker.Language): String? {
            if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage !== language || noResultFoundAckMessage == null || noResultFoundAckMessage!!.length == 0) {
                loadDataFromXML(context, language)
            }
            return noResultFoundAckMessage
        }

        fun setDialogTitle(dialogTitle: String) {
            CCPCountry.dialogTitle = dialogTitle
        }

        fun setSearchHintMessage(searchHintMessage: String) {
            CCPCountry.searchHintMessage = searchHintMessage
        }

        fun setNoResultFoundAckMessage(noResultFoundAckMessage: String) {
            CCPCountry.noResultFoundAckMessage = noResultFoundAckMessage
        }

        internal fun getCountryForCodeFromEnglishList(code: String): CCPCountry? {

            val countries: List<CCPCountry>
            countries = libraryMasterCountriesEnglish

            for (ccpCountry in countries) {
                if (ccpCountry.phoneCode == code) {
                    return ccpCountry
                }
            }
            return null
        }

        internal fun getCustomMasterCountryList(context: Context, codePicker: CountryCodePicker): List<CCPCountry>? {
            codePicker.refreshCustomMasterList()
            return if (codePicker.customMasterCountriesList != null && codePicker.customMasterCountriesList!!.size > 0) {
                codePicker.customMasterCountriesList
            } else {
                getLibraryMasterCountryList(context, codePicker.getLanguageToApply()!!)
            }
        }


        internal fun getCountryForNameCodeFromCustomMasterList(
            context: Context,
            customMasterCountriesList: List<CCPCountry>?,
            language: CountryCodePicker.Language,
            nameCode: String
        ): CCPCountry? {
            if (customMasterCountriesList == null || customMasterCountriesList.size == 0) {
                return getCountryForNameCodeFromLibraryMasterList(context, language, nameCode)
            } else {
                for (ccpCountry in customMasterCountriesList) {
                    if (ccpCountry.nameCode.equals(nameCode, ignoreCase = true)) {
                        return ccpCountry
                    }
                }
            }
            return null
        }


        fun getCountryForNameCodeFromLibraryMasterList(
            context: Context,
            language: CountryCodePicker.Language,
            nameCode: String
        ): CCPCountry? {
            val countries: List<CCPCountry>?
            countries = CCPCountry.getLibraryMasterCountryList(context, language)
            for (ccpCountry in countries!!) {
                if (ccpCountry.nameCode.equals(nameCode, ignoreCase = true)) {
                    return ccpCountry
                }
            }
            return null
        }

        internal fun getCountryForNameCodeFromEnglishList(nameCode: String): CCPCountry? {
            val countries: List<CCPCountry>
            countries = libraryMasterCountriesEnglish
            for (CCPCountry in countries) {
                if (CCPCountry.nameCode.equals(nameCode, ignoreCase = true)) {
                    return CCPCountry
                }
            }
            return null
        }

        internal fun getCountryForNumber(
            context: Context,
            language: CountryCodePicker.Language,
            preferredCountries: List<CCPCountry>?,
            fullNumber: String
        ): CCPCountry? {
            val firstDigit: Int
            //String plainNumber = PhoneNumberUtil.getInstance().normalizeDigitsOnly(fullNumber);
            if (fullNumber.length != 0) {
                if (fullNumber[0] == '+') {
                    firstDigit = 1
                } else {
                    firstDigit = 0
                }
                var ccpCountry: CCPCountry? = null
                for (i in firstDigit..fullNumber.length) {
                    val code = fullNumber.substring(firstDigit, i)
                    var countryGroup: CCPCountryGroup? = null
                    try {
                        countryGroup = CCPCountryGroup.getCountryGroupForPhoneCode(Integer.parseInt(code))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (countryGroup != null) {
                        val areaCodeStartsAt = firstDigit + code.length
                        //when phone number covers area isoCountryCode2Digit too.
                        if (fullNumber.length >= areaCodeStartsAt + countryGroup!!.areaCodeLength) {
                            val areaCode =
                                fullNumber.substring(areaCodeStartsAt, areaCodeStartsAt + countryGroup!!.areaCodeLength)
                            return countryGroup!!.getCountryForAreaCode(context, language, areaCode)
                        } else {
                            return getCountryForNameCodeFromLibraryMasterList(
                                context,
                                language,
                                countryGroup!!.defaultNameCode
                            )
                        }
                    } else {
                        ccpCountry = CCPCountry.getCountryForCode(context, language, preferredCountries, code)
                        if (ccpCountry != null) {
                            return ccpCountry
                        }
                    }
                }
            }
            //it reaches here means, phone number has some problem.
            return null
        }

        fun getCountryForNumber(
            context: Context,
            language: CountryCodePicker.Language,
            fullNumber: String
        ): CCPCountry? {
            return getCountryForNumber(context, language, null, fullNumber)
        }

        internal fun getFlagMasterResID(CCPCountry: CCPCountry): Int {
            when (CCPCountry.nameCode.toLowerCase()) {
                //this should be sorted based on country name isoCountryCode2Digit.
                "ae" //united arab emirates
                -> return R.drawable.flag_ae

                else -> return R.drawable.flag_ae
            }
        }


        internal fun getFlagEmoji(CCPCountry: CCPCountry): String {
            when (CCPCountry.nameCode.toLowerCase()) {
                //this should be sorted based on country name isoCountryCode2Digit.
                "ae" -> return "🇦🇪"
                else -> return " "
            }
        }


        fun getLibraryMasterCountryList(context: Context, language: CountryCodePicker.Language): List<CCPCountry>? {
            if (loadedLibraryMasterListLanguage == null || language !== loadedLibraryMasterListLanguage || loadedLibraryMaterList == null || loadedLibraryMaterList!!.size == 0) { //when it is required to load country in country list
                loadDataFromXML(context, language)
            }
            return loadedLibraryMaterList
        }

        val libraryMasterCountriesEnglish: MutableList<CCPCountry>
            get() {
                val countries = ArrayList<CCPCountry>()
                countries.add(CCPCountry("ae", "971", "UAE", DEFAULT_FLAG_RES))
                return countries
            }

        fun setEnglishName(ccpCountry: CCPCountry, ename: String) {
            ccpCountry.englishName = ename
        }

        fun getEnglishName(ccpCountry: CCPCountry): String {
            if (null == ccpCountry.englishName) {
                return " "
            }
            return ccpCountry.englishName!!
        }

    }

     fun getCountryForCode(
        context: Context,
        language: CountryCodePicker.Language,
        preferredCountries: List<CCPCountry>,
        code: Int
    ): CCPCountry? {
        return getCountryForCode(context, language, preferredCountries, code.toString() + "")
    }

}
