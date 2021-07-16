package co.yap.widgets.mobile


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Build
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.Keep
import co.yap.yapcore.R
import co.yap.yapcore.helpers.extentions.contextColor
import com.google.gson.Gson
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.io.StringWriter
import java.util.*


class CountryCodePicker : RelativeLayout {
    private var CCP_PREF_FILE = "CCP_PREF_FILE"
    private var defaultCountryCode: Int = 0
    private var defaultCountryNameCode: String? = null
    var mCntext: Context = getContext()

    private var mInflater: LayoutInflater? = null
    var textView_selectedCountry: TextView? = null
    var ivDropDownIconDrawable: ImageView? = null
    var setIconVisibility: Boolean = false
    private var editText_registeredCarrierNumber: EditText? = null
    var holder: RelativeLayout? = null
        private set
    var imageViewFlag: ImageView? = null
    private var linearFlagBorder: LinearLayout? = null
    private var linearFlagHolder: LinearLayout? = null
    private var selectedCCPCountry: CCPCountry? = null

    internal var defaultCCPCountry: CCPCountry? = null

    private var relativeClickConsumer: RelativeLayout? = null
    private var codePicker: CountryCodePicker? = null
    private lateinit var currentTextGravity: TextGravity

    private var selectedAutoDetectionPref = AutoDetectionPref.SIM_NETWORK_LOCALE
    private var phoneUtil: PhoneNumberUtil? = null
    private var showNameCode = true
    private var showPhoneCode = true


    var isCcpDialogShowPhoneCode = true
        set
    private var showFlag = true
    private var showFullName = false

    var isShowFastScroller = true
        set

    var ccpDialogShowTitle = true

    var ccpDialogShowFlag = true
    var ccp_onFlagClick = false
    var ccp_hideFlag = true

    var isSearchAllowed = true
    private var showArrow = true
    var isShowCloseIcon = false
        private set
    private var rememberLastSelection = false
    private var detectCountryWithAreaCode = true

    var ccpDialogShowNameCode = true
    var isDialogInitialScrollToSelectionEnabled = false
        private set
    var ccpUseEmoji = false
    private var ccpUseDummyEmojiForPreview = false
    private var isInternationalFormattingOnlyEnabled = true
        private set
    private var hintExampleNumberType = PhoneNumberType.MOBILE
    private var selectionMemoryTag = "ccp_last_selection"
    private var selectedCountryTag = "selectedCountry"
    private var contentColor = DEFAULT_UNSET
    private var arrowColor = DEFAULT_UNSET
    private var borderFlagColor: Int = 0
    private var dialogTypeFace: Typeface? = null

    var preferredCountries: List<CCPCountry>? = null
    private var ccpTextgGravity = TEXT_GRAVITY_CENTER

    private var countryPreference: String? = null

    var fastScrollerBubbleColor = 0

    var customMasterCountriesList: List<CCPCountry>? = null

    private var customMasterCountriesParam: String? = null
    private var excludedCountriesParam: String? = null
    private var customDefaultLanguage = Language.ENGLISH
    private var languageToApply = Language.ENGLISH

    internal var dialogKeyboardAutoPopup: Boolean = true
    private var ccpClickable = true
    private var isAutoDetectLanguageEnabled = false
    private var isAutoDetectCountryEnabled = false
    private var numberAutoFormattingEnabled = true
    private var hintExampleNumberEnabled = false
    private var xmlWidth = "notSet"
    private var validityTextWatcher: TextWatcher? = null
    private var formattingTextWatcher: InternationalPhoneTextWatcher? = null
    private var reportedValidity: Boolean = false
    private var areaCodeCountryDetectorTextWatcher: TextWatcher? = null
    private var countryDetectionBasedOnAreaAllowed: Boolean = false
    private var lastCheckedAreaCode: String? = null
    private var lastCursorPosition = 0
    private var countryChangedDueToAreaCode = false
    private var onCountryChangeListener: OnCountryChangeListener? = null
    private var phoneNumberValidityChangeListener: PhoneNumberValidityChangeListener? = null
    private var failureListener: FailureListener? = null

    var dialogEventsListener: DialogEventsListener? = null
        set
    private var customDialogTextProvider: CustomDialogTextProvider? = null

    var fastScrollerHandleColor = 0
        set

    var tvCountryCodePickerColor: Int = R.color.greyDark

    var dialogBackgroundColor: Int = 0
        set

    var dialogTextColor: Int = 0
        set

    var dialogSearchEditTextTintColor: Int = 0
        set

    var fastScrollerBubbleTextAppearance = 0
        set
    private var currentCountryGroup: CCPCountryGroup? = null
    private var customClickListener: View.OnClickListener? = null
    private var countryCodeHolderClickListener: OnClickListener =
        OnClickListener { v ->
            if (customClickListener == null) {
                if (isCcpClickable) {
                    if (isDialogInitialScrollToSelectionEnabled) {
                        launchCountrySelectionDialog(selectedCountryNameCode)
                    } else {
                        launchCountrySelectionDialog()
                    }
                }
            } else {
                customClickListener!!.onClick(v)
            }
        }

    private fun isNumberAutoFormattingEnabled(): Boolean {
        return numberAutoFormattingEnabled
    }

    private fun setDefaultCountryUae() {
        defaultCCPCountry = CCPCountry("ae", "971", "UAE", CCPCountry.DEFAULT_FLAG_RES)
        selectedCCPCountry = defaultCCPCountry
    }

    /**
     * This will set boolean for numberAutoFormattingEnabled and refresh formattingTextWatcher
     *
     * @param numberAutoFormattingEnabled
     */

    fun setNumberAutoFormattingEnabled(numberAutoFormattingEnabled: Boolean) {
        this.numberAutoFormattingEnabled = numberAutoFormattingEnabled
        if (editText_registeredCarrierNumber != null) {
            updateFormattingTextWatcher()
        }
    }

    private var isShowPhoneCode: Boolean
        get() {
            return showPhoneCode
        }
        set(showPhoneCode) {
            this.showPhoneCode = showPhoneCode
            selectedCountry = this!!.selectedCCPCountry!!
        }
    private val ccpLanguageFromLocale: Language?
        get() {
            val currentLocale = mCntext.getResources().getConfiguration().locale
            for (language in Language.values()) {
                if (language.code.equals(currentLocale.getLanguage(), ignoreCase = true)) {
                    if ((language.country == null || language.country.equals(
                            currentLocale.getCountry(),
                            ignoreCase = true
                        ))
                    )
                        return language
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if ((language.script == null || language.script.equals(
                                currentLocale.getScript(),
                                ignoreCase = true
                            ))
                        )
                            return language
                    }
                }
            }
            return null
        }
    private var selectedCountry: CCPCountry? = null
        private set(selectedCCPCountry) {
            setSelectedCountry(selectedCCPCountry)
        }

    private val selectedHintNumberType: PhoneNumberUtil.PhoneNumberType
        get() {
            when (hintExampleNumberType) {
                PhoneNumberType.MOBILE -> return PhoneNumberUtil.PhoneNumberType.MOBILE
                PhoneNumberType.FIXED_LINE -> return PhoneNumberUtil.PhoneNumberType.FIXED_LINE
                PhoneNumberType.FIXED_LINE_OR_MOBILE -> return PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE
                PhoneNumberType.TOLL_FREE -> return PhoneNumberUtil.PhoneNumberType.TOLL_FREE
                PhoneNumberType.PREMIUM_RATE -> return PhoneNumberUtil.PhoneNumberType.PREMIUM_RATE
                PhoneNumberType.SHARED_COST -> return PhoneNumberUtil.PhoneNumberType.SHARED_COST
                PhoneNumberType.VOIP -> return PhoneNumberUtil.PhoneNumberType.VOIP
                PhoneNumberType.PERSONAL_NUMBER -> return PhoneNumberUtil.PhoneNumberType.PERSONAL_NUMBER
                PhoneNumberType.PAGER -> return PhoneNumberUtil.PhoneNumberType.PAGER
                PhoneNumberType.UAN -> return PhoneNumberUtil.PhoneNumberType.UAN
                PhoneNumberType.VOICEMAIL -> return PhoneNumberUtil.PhoneNumberType.VOICEMAIL
                PhoneNumberType.UNKNOWN ->
                    return PhoneNumberUtil.PhoneNumberType.UNKNOWN
                else -> return PhoneNumberUtil.PhoneNumberType.MOBILE
            }
        }

    /**
     * This updates country dynamically as user types in area isoCountryCode2Digit
     *
     * @return
     */
    private fun countryDetectorTextWatcher(): TextWatcher {

        if (editText_registeredCarrierNumber != null) {
            if (areaCodeCountryDetectorTextWatcher == null) {
                areaCodeCountryDetectorTextWatcher = object : TextWatcher {
                    internal var lastCheckedNumber: String? = null


                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val selectedCountry = getSelectedCountry()
                        if (selectedCountry != null && (lastCheckedNumber == null || lastCheckedNumber != s.toString()) && countryDetectionBasedOnAreaAllowed) {
                            //possible countries
                            if (currentCountryGroup != null) {
                                val enteredValue =
                                    getEditText_registeredCarrierNumber().text.toString()
                                if (enteredValue.length >= currentCountryGroup!!.areaCodeLength) {
                                    val digitsValue =
                                        PhoneNumberUtil.normalizeDigitsOnly(enteredValue)
                                    if (digitsValue.length >= currentCountryGroup!!.areaCodeLength) {
                                        val currentAreaCode =
                                            digitsValue.substring(
                                                0,
                                                currentCountryGroup!!.areaCodeLength
                                            )
                                        if (currentAreaCode != lastCheckedAreaCode) {
                                            val detectedCountry =
                                                currentCountryGroup!!.getCountryForAreaCode(
                                                    context,
                                                    getLanguageToApply(),
                                                    currentAreaCode
                                                )
                                            if (!detectedCountry.equals(selectedCountry)) {
                                                countryChangedDueToAreaCode = true
                                                lastCursorPosition = Selection.getSelectionEnd(s)
                                                setSelectedCountry(detectedCountry)
                                            }
                                            lastCheckedAreaCode = currentAreaCode
                                        }
                                    }
                                }
                            }
                            lastCheckedNumber = s.toString()
                        } else {
                            phoneNumberValidityChangeListener!!.onValidityChanged(reportedValidity)

                        }

                    }

                    override fun afterTextChanged(s: Editable) {


                    }
                }
            }
        }
        return this.areaCodeCountryDetectorTextWatcher!!
    }

    private var isCcpClickable: Boolean
        get() {
            return ccpClickable
        }
        set(ccpClickable) {
            this.ccpClickable = ccpClickable
            if (!ccpClickable) {
                relativeClickConsumer!!.setOnClickListener(null)
                relativeClickConsumer!!.isClickable = false
                relativeClickConsumer!!.isEnabled = false
            } else {
                relativeClickConsumer!!.setOnClickListener(countryCodeHolderClickListener)
                relativeClickConsumer!!.isClickable = true
                relativeClickConsumer!!.isEnabled = true
            }
        }

    val dialogTitle: String
        get() {
            val defaultTitle = CCPCountry.getDialogTitle(mCntext, getLanguageToApply())
            if (customDialogTextProvider != null) {
                return customDialogTextProvider!!.getCCPDialogTitle(
                    getLanguageToApply(),
                    defaultTitle!!
                )
            } else {
                return defaultTitle!!
            }
        }

    val searchHintText: String
        get() {
            val defaultHint = CCPCountry.getSearchHintMessage(mCntext, getLanguageToApply())
            if (customDialogTextProvider != null) {
                return customDialogTextProvider!!.getCCPDialogSearchHintText(
                    getLanguageToApply(),
                    defaultHint!!
                )
            } else {
                return defaultHint!!
            }
        }

    val noResultACK: String
        get() {
            val defaultNoResultACK =
                CCPCountry.getNoResultFoundAckMessage(mCntext, getLanguageToApply())
            if (customDialogTextProvider != null) {
                return customDialogTextProvider!!.getCCPDialogNoResultACK(
                    getLanguageToApply(),
                    defaultNoResultACK!!
                )
            } else {
                return defaultNoResultACK!!
            }
        }

    val defaultCountryCodeAsInt: Int
        get() {
            var code = 0
            try {
                code = Integer.parseInt(getDefaultCountryCode())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return code
        }

    val defaultCountryCodeWithPlus: String
        get() {
            return "+" + getDefaultCountryCode()
        }


    val defaultCountryName: String
        get() {
            return defaultCCPCountry!!.name
        }

    private val selectedCountryCode: String
        get() {
            return selectedCountry!!.phoneCode
        }

    private val selectedCountryCodeWithPlus: String
        get() {
            return "+$selectedCountryCode"
        }

    val selectedCountryCodeAsInt: Int
        get() {
            var code = 0
            try {
                code = Integer.parseInt(selectedCountryCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return code
        }

    val selectedCountryName: String
        get() {
            return selectedCountry!!.name
        }

    val selectedCountryEnglishName: String
        get() {
            return selectedCountry!!.englishName!!
        }

    private val selectedCountryNameCode: String? = null


    private val enteredPhoneNumber: Phonenumber.PhoneNumber
        @Throws(NumberParseException::class)
        get() {
            var carrierNumber = ""
            if (editText_registeredCarrierNumber != null) {
                carrierNumber =
                    PhoneNumberUtil.normalizeDigitsOnly(editText_registeredCarrierNumber!!.getText().toString())
            }
            return getPhoneUtil().parse(carrierNumber, selectedCountryNameCode)
        }

    var fullNumber: String
        get() {
            return try {
                val phoneNumber = enteredPhoneNumber
                getPhoneUtil().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
                    .substring(1)
            } catch (e: NumberParseException) {
                selectedCountryCode
            }
        }
        set(fullNumber) {
            var country =
                CCPCountry.getCountryForNumber(
                    context,
                    getLanguageToApply(),
                    preferredCountries,
                    fullNumber
                )
            if (country == null)
                country = defaultCCPCountry
            selectedCountry = country!!
            val carrierNumber = detectCarrierNumber(fullNumber, country)
            getEditText_registeredCarrierNumber().setText(carrierNumber)
            updateFormattingTextWatcher()
        }

    val formattedFullNumber: String
        get() {
            return try {
                val phoneNumber = enteredPhoneNumber
                "+" + getPhoneUtil().format(
                    phoneNumber,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL
                ).substring(1)
            } catch (e: NumberParseException) {
                selectedCountryCode
            }
        }

    val fullNumberWithPlus: String
        get() {
            try {
                val phoneNumber = enteredPhoneNumber
                return getPhoneUtil().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
            } catch (e: NumberParseException) {
                return selectedCountryCode
            }
        }

    fun isValidFullNumber(): Boolean {
        try {
            return if (getEditText_registeredCarrierNumber().text.isNotEmpty()) {
                val phoneNumber = getPhoneUtil().parse(
                    "+" + getSelectedCountry()!!.getphoneCode() + getEditText_registeredCarrierNumber().text.toString(),
                    getSelectedCountry()!!.getnameCode()
                )
                getPhoneUtil().isValidNumber(phoneNumber)
            } else run {
                false
            }
        } catch (e: NumberParseException) {
            //            when number could not be parsed, its not valid
            return false
        }

    }


    constructor(context: Context) : super(context) {
        this.mCntext = context
        assignViews(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.mCntext = context
        assignViews(attrs)
        init(attrs)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mCntext = context
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        assignViews(attrs)
        codePicker = this
        if (attrs != null) {
            applyCustomProperty(attrs)
        }
    }

    private fun assignViews(attrs: AttributeSet?) {
        var holderView: View? = null

        LayoutInflater.from(mCntext).inflate(R.layout.component_code_picker, this, true)

        textView_selectedCountry = findViewById(R.id.tvSelectedCountry)
        ivDropDownIconDrawable = findViewById(R.id.ivDropDownIcon)

        holder = findViewById(R.id.countryCodeHolder)
        imageViewFlag = findViewById(R.id.ivFlag)
        if (setIconVisibility) {
            relativeClickConsumer!!.setPadding(0, 0, 0, 0)
            textView_selectedCountry!!.text = "971"

        } else {
            textView_selectedCountry!!.text = "+971"

        }

        textView_selectedCountry!!.setTextColor(tvCountryCodePickerColor)
        imageViewFlag!!.setImageResource(R.drawable.flag_ae)

        linearFlagHolder = findViewById(R.id.llFlagHolder)
        linearFlagBorder = findViewById(R.id.llFlag)
        relativeClickConsumer = findViewById(R.id.rlContainer)
        if (ccp_onFlagClick) {// force disable click to open dialogue list of countries
            relativeClickConsumer!!.setOnClickListener(countryCodeHolderClickListener)

        }
        if (ccp_hideFlag) {
            imageViewFlag!!.visibility = View.VISIBLE
        } else {
            imageViewFlag!!.visibility = View.GONE

        }
        detectCountryWithAreaCode = true
        updateFormattingTextWatcher()
        setDefaultCountryUae()
    }

    private fun applyCustomProperty(attrs: AttributeSet) {
        val a =
            context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountryCodePicker, 0, 0)
        //default country isoCountryCode2Digit
        try {
            //hide nameCode. If someone wants only phone isoCountryCode2Digit to avoid name collision for same country phone isoCountryCode2Digit.
            showNameCode = a.getBoolean(R.styleable.CountryCodePicker_ccp_showNameCode, true)
            ccp_onFlagClick = a.getBoolean(R.styleable.CountryCodePicker_ccp_onFlagClick, false)
            ccp_hideFlag = a.getBoolean(R.styleable.CountryCodePicker_ccp_hideFlag, true)
            if (ccp_hideFlag) {
                imageViewFlag!!.visibility = View.VISIBLE
            } else {
                imageViewFlag!!.visibility = View.GONE

            }

            tvCountryCodePickerColor = a.getColor(
                R.styleable.CountryCodePicker_ccp_pickerTextColor,
                tvCountryCodePickerColor
            )

            textView_selectedCountry!!.setTextColor(tvCountryCodePickerColor)

            setIconVisibility =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_showDropDown, setIconVisibility)
            if (setIconVisibility) {
                ivDropDownIconDrawable!!.visibility = View.VISIBLE
                relativeClickConsumer!!.setPadding(0, 0, 0, 0)
                textView_selectedCountry!!.text = "971"

            }

            //number auto formatting
            numberAutoFormattingEnabled =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_autoFormatNumber, true)
            //show phone isoCountryCode2Digit.
            showPhoneCode = a.getBoolean(R.styleable.CountryCodePicker_ccp_showPhoneCode, true)
            //show phone isoCountryCode2Digit on dialog
            isCcpDialogShowPhoneCode =
                a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_showPhoneCode, showPhoneCode)
            //show name isoCountryCode2Digit on dialog
            ccpDialogShowNameCode =
                a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_showNameCode, true)
            //show title on dialog
            ccpDialogShowTitle =
                a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_showTitle, true)
            //show title on dialog
            ccpUseEmoji = a.getBoolean(R.styleable.CountryCodePicker_ccp_useFlagEmoji, false)
            //show title on dialog
            ccpUseDummyEmojiForPreview =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_useDummyEmojiForPreview, false)
            //show flag on dialog
            ccpDialogShowFlag = a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_showFlag, true)
            //ccpDialog initial scroll to selection
            isDialogInitialScrollToSelectionEnabled =
                a.getBoolean(
                    R.styleable.CountryCodePicker_ccpDialog_initialScrollToSelection,
                    false
                )
            //show full name
            showFullName = a.getBoolean(R.styleable.CountryCodePicker_ccp_showFullName, false)
            //show fast scroller
            isShowFastScroller =
                a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_showFastScroller, true)
            //bubble color
            fastScrollerBubbleColor =
                a.getColor(R.styleable.CountryCodePicker_ccpDialog_fastScroller_bubbleColor, 0)
            //scroller handle color
            fastScrollerHandleColor =
                a.getColor(R.styleable.CountryCodePicker_ccpDialog_fastScroller_handleColor, 0)
            //scroller text appearance
            fastScrollerBubbleTextAppearance =
                a.getResourceId(
                    R.styleable.CountryCodePicker_ccpDialog_fastScroller_bubbleTextAppearance,
                    0
                )
            //auto detect language
            isAutoDetectLanguageEnabled =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_autoDetectLanguage, false)
            //detect country from area isoCountryCode2Digit
            detectCountryWithAreaCode =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_areaCodeDetectedCountry, true)
            //remember last selection
            rememberLastSelection =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_rememberLastSelection, false)
            //example number hint enabled?
            hintExampleNumberEnabled =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_hintExampleNumber, false)
            //international formatting only
            isInternationalFormattingOnlyEnabled =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_internationalFormattingOnly, true)
            //example number hint type
            val hintNumberTypeIndex =
                a.getInt(R.styleable.CountryCodePicker_ccp_hintExampleNumberType, 0)
            hintExampleNumberType = PhoneNumberType.values()[hintNumberTypeIndex]
            //memory tag name for selection
            selectionMemoryTag =
                a.getString(R.styleable.CountryCodePicker_ccp_selectionMemoryTag) ?: ""
            //country auto detection pref
            val autoDetectionPrefValue =
                a.getInt(R.styleable.CountryCodePicker_ccp_countryAutoDetectionPref, 123)
            selectedAutoDetectionPref =
                AutoDetectionPref.getPrefForValue((autoDetectionPrefValue).toString())
            //auto detect county
            isAutoDetectCountryEnabled =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_autoDetectCountry, false)
            //show arrow
            showArrow = a.getBoolean(R.styleable.CountryCodePicker_ccp_showArrow, true)
            //show close icon
            isShowCloseIcon =
                a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_showCloseIcon, false)
            //show flag
            showFlag(a.getBoolean(R.styleable.CountryCodePicker_ccp_showFlag, true))
            //autopop keyboard
            dialogKeyboardAutoPopup =
                a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_keyboardAutoPopup, true)
            setDialogKeyboardAutoPopup(dialogKeyboardAutoPopup)
            //if custom default language is specified, then set it as custom else sets english as custom
            val attrLanguage: Int
            attrLanguage = a.getInt(
                R.styleable.CountryCodePicker_ccp_defaultLanguage,
                Language.ENGLISH.ordinal
            )
            customDefaultLanguage = getLanguageEnum(attrLanguage)
            updateLanguageToApply()
            //custom master list
            customMasterCountriesParam =
                a.getString(R.styleable.CountryCodePicker_ccp_customMasterCountries)
            excludedCountriesParam =
                a.getString(R.styleable.CountryCodePicker_ccp_excludedCountries)
            if (!isInEditMode()) {
                refreshCustomMasterList()
            }
            //preference
            countryPreference = a.getString(R.styleable.CountryCodePicker_ccp_countryPreference)

            if (!isInEditMode()) {
                refreshPreferredCountries()
            }
            //text gravity
            if (a.hasValue(R.styleable.CountryCodePicker_ccp_textGravity)) {
                ccpTextgGravity =
                    a.getInt(R.styleable.CountryCodePicker_ccp_textGravity, TEXT_GRAVITY_CENTER)
            }
            applyTextGravity(ccpTextgGravity)
            //default country
            defaultCountryNameCode = a.getString(R.styleable.CountryCodePicker_ccp_defaultNameCode)
            var setUsingNameCode = false
            if (defaultCountryNameCode != null && defaultCountryNameCode!!.length != 0) {
                if (!isInEditMode()) {
                    if (CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                            getContext(), getLanguageToApply(),
                            defaultCountryNameCode!!
                        ) != null
                    ) {
                        setUsingNameCode = true
                        defaultCCPCountry = CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                            getContext(), getLanguageToApply(),
                            defaultCountryNameCode!!
                        )
                        selectedCountry = this!!.defaultCCPCountry!!
                    }
                } else {
                    if (CCPCountry.getCountryForNameCodeFromEnglishList(defaultCountryNameCode!!) != null) {
                        setUsingNameCode = true
                        defaultCCPCountry =
                            CCPCountry.getCountryForNameCodeFromEnglishList(defaultCountryNameCode!!)
                        selectedCountry = this!!.defaultCCPCountry!!

                        setDefaultCountry(
                            CCPCountry!!.getCountryForNameCodeFromLibraryMasterList(
                                context, getLanguageToApply()!!, defaultCountryNameCode!!
                            )!!
                        )
                        setSelectedCountry(defaultCCPCountry)
                    }
                }

                if (!setUsingNameCode) {
                    defaultCCPCountry = CCPCountry.getCountryForNameCodeFromEnglishList("AE")
                    selectedCountry = this!!.defaultCCPCountry!!
                    setUsingNameCode = true
                    setDefaultCountry(
                        CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                            context, getLanguageToApply(), defaultCountryNameCode!!
                        )!!
                    )
                    setSelectedCountry(defaultCCPCountry)
                }
            }

            var defaultCountryCode =
                a.getInteger(R.styleable.CountryCodePicker_ccp_defaultPhoneCode, -1)
            if (!setUsingNameCode && defaultCountryCode != -1) {
                if (!isInEditMode) {

                    if (defaultCountryCode != -1 && CCPCountry.getCountryForCode(
                            context, getLanguageToApply(),
                            this.preferredCountries!!, defaultCountryCode
                        ) == null
                    ) {
                        defaultCountryCode = LIB_DEFAULT_COUNTRY_CODE
                    }
                    setDefaultCountryUsingPhoneCode(defaultCountryCode)
                    selectedCountry = defaultCCPCountry!!

                } else {

                    var defaultCountry =
                        CCPCountry.getCountryForCodeFromEnglishList(defaultCountryCode.toString() + "")
                    if (defaultCCPCountry == null) {
                        defaultCountry =
                            CCPCountry.getCountryForCodeFromEnglishList(LIB_DEFAULT_COUNTRY_CODE.toString() + "")
                    }
                    setDefaultCountry(
                        CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                            context, getLanguageToApply(), defaultCountryNameCode!!
                        )!!
                    )
                    setSelectedCountry(defaultCCPCountry)

                }
            }
            if (defaultCCPCountry == null) {
                defaultCCPCountry = CCPCountry.getCountryForNameCodeFromEnglishList("AE")
                if (selectedCountry == null) {
                    selectedCountry = defaultCCPCountry!!
                }
            }

            if (isAutoDetectCountryEnabled && !isInEditMode) {
                setAutoDetectedCountry(true)
            }
            if (rememberLastSelection && !isInEditMode) {
                loadLastSelectedCountryInCCP()
            }

            val arrowColor: Int =
                a.getColor(R.styleable.CountryCodePicker_ccp_arrowColor, DEFAULT_UNSET)
            val contentColor: Int = if (isInEditMode) {
                a.getColor(R.styleable.CountryCodePicker_ccp_contentColor, DEFAULT_UNSET)
            } else {
                a.getColor(
                    R.styleable.CountryCodePicker_ccp_contentColor,
                    context.contextColor(R.color.greyDark)
                )
            }
            if (contentColor != DEFAULT_UNSET) {
                setContentColor(contentColor)
            }

            val borderFlagColor: Int = if (isInEditMode) {
                a.getColor(R.styleable.CountryCodePicker_ccp_flagBorderColor, 0)
            } else {
                a.getColor(
                    R.styleable.CountryCodePicker_ccp_flagBorderColor,
                    context.contextColor(R.color.transparent)
                )
            }
            if (borderFlagColor != 0) {
                setFlagBorderColor(borderFlagColor)
            }
            //dialog colors
            dialogBackgroundColor =
                a.getColor(
                    R.styleable.CountryCodePicker_ccpDialog_backgroundColor,
                    context.contextColor(R.color.transparent)
                )
            dialogTextColor = a.getColor(
                R.styleable.CountryCodePicker_ccpDialog_textColor,
                context.contextColor(R.color.transparent)
            )
            dialogSearchEditTextTintColor =
                a.getColor(
                    R.styleable.CountryCodePicker_ccpDialog_searchEditTextTint,
                    context.contextColor(R.color.transparent)
                )
            //text size
            val textSize = a.getDimensionPixelSize(R.styleable.CountryCodePicker_ccp_textSize, 0)
            if (textSize > 0) {
                textView_selectedCountry!!.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    textSize.toFloat()
                )
//                setFlagSize(textSize)
            }
            //if arrow size is explicitly defined
            val arrowSize = a.getDimensionPixelSize(R.styleable.CountryCodePicker_ccp_arrowSize, 0)

            isSearchAllowed =
                a.getBoolean(R.styleable.CountryCodePicker_ccpDialog_allowSearch, true)
            isCcpClickable = a.getBoolean(R.styleable.CountryCodePicker_ccp_clickable, true)
        } catch (e: Exception) {
            val sw = StringWriter()
//            val pw = PrintWriter(sw)
//            e.printStackTrace(pw)
//            tvSelectedCountry!!.setText(sw.toString())
        } finally {
            a.recycle()
        }
    }


    internal fun isDialogKeyboardAutoPopup(): Boolean {
        return dialogKeyboardAutoPopup
    }

    private fun setDialogKeyboardAutoPopup(dialogKeyboardAutoPopup: Boolean) {
        this.dialogKeyboardAutoPopup = dialogKeyboardAutoPopup
    }


    private fun loadLastSelectedCountryInCCP() {

        val sharedPref = context.getSharedPreferences(
            CCP_PREF_FILE, Context.MODE_PRIVATE
        )

        val lastSelectedCountryNameCode = sharedPref.getString(selectionMemoryTag, null)

        if (lastSelectedCountryNameCode != null) {
            setCountryForNameCode(lastSelectedCountryNameCode)
        }
    }

    private fun storeSelectedCountryNameCode(selectedCountryNameCode: String) {
        val sharedPref = context.getSharedPreferences(
            CCP_PREF_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()
        editor.putString(selectionMemoryTag, selectedCountryNameCode)
        editor.apply()
    }

    private fun applyTextGravity(enumIndex: Int) {
        if (enumIndex == TextGravity.LEFT.enumIndex) {
            textView_selectedCountry!!.gravity = Gravity.LEFT
        } else if (enumIndex == TextGravity.CENTER.enumIndex) {
            textView_selectedCountry!!.gravity = Gravity.CENTER
        } else {
            textView_selectedCountry!!.gravity = Gravity.RIGHT
        }
    }

    private fun updateLanguageToApply() {
        languageToApply = if (this.isInEditMode) {
            customDefaultLanguage
        } else {
            if (isAutoDetectLanguageEnabled) {
                ccpLanguageFromLocale ?: getCustomDefaultLanguage()
            } else {
                customDefaultLanguage
            }
        }
    }

    private fun updateHint() {
        if (editText_registeredCarrierNumber != null && hintExampleNumberEnabled) {
            var formattedNumber = ""
            val exampleNumber = getPhoneUtil().getExampleNumberForType(
                selectedCountryNameCode,
                selectedHintNumberType
            )
            if (exampleNumber != null) {
                formattedNumber = exampleNumber.getNationalNumber().toString() + ""
                formattedNumber = PhoneNumberUtils.formatNumber(
                    selectedCountryCodeWithPlus + formattedNumber,
                    selectedCountryNameCode
                )
                formattedNumber = formattedNumber.substring(selectedCountryCodeWithPlus.length)
                    .trim { it <= ' ' }
            }
            editText_registeredCarrierNumber!!.hint = formattedNumber
        }
    }

    fun getLanguageToApply(): Language {
        return languageToApply
    }

    private fun updateFormattingTextWatcher() {
        if (editText_registeredCarrierNumber != null && selectedCCPCountry != null) {
            val enteredValue = getEditText_registeredCarrierNumber().getText().toString()
            val digitsValue = PhoneNumberUtil.normalizeDigitsOnly(enteredValue)
            if (formattingTextWatcher != null) {
                editText_registeredCarrierNumber!!.removeTextChangedListener(formattingTextWatcher)
            }
            if (areaCodeCountryDetectorTextWatcher != null) {
                editText_registeredCarrierNumber!!.removeTextChangedListener(
                    areaCodeCountryDetectorTextWatcher
                )
            }
            if (numberAutoFormattingEnabled) {
                formattingTextWatcher = InternationalPhoneTextWatcher(
                    context,
                    getselectedCountryNameCode(), getselectedCountryCodeAsInt(),
                    isInternationalFormattingOnlyEnabled
                )
                editText_registeredCarrierNumber!!.addTextChangedListener(formattingTextWatcher)
            }
            if (detectCountryWithAreaCode) {
                areaCodeCountryDetectorTextWatcher = countryDetectorTextWatcher()
                editText_registeredCarrierNumber!!.addTextChangedListener(
                    areaCodeCountryDetectorTextWatcher
                )
            }
//            editText_registeredCarrierNumber!!.setText("")
//            editText_registeredCarrierNumber!!.setText(digitsValue)
//            editText_registeredCarrierNumber!!.setSelection(editText_registeredCarrierNumber!!.getText().length)
        } else {
            if (editText_registeredCarrierNumber == null) {
                Log.v(TAG, "EditTextStatus: EditText not registered " + selectionMemoryTag)
            } else {
                Log.v(TAG, "EditTextStatus: selected country is null " + selectionMemoryTag)
            }
        }
    }

    private fun getCustomDefaultLanguage(): Language {
        return customDefaultLanguage
    }

    fun getselectedCountryNameCode(): String {
        return getSelectedCountry()!!.getnameCode().toUpperCase()
    }

    fun getselectedCountryCodeAsInt(): Int {
        var code = 0
        try {
            code = Integer.parseInt(getselectedCountryCode())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return code
    }

    private fun getselectedCountryCode(): String {
        return getSelectedCountry()!!.phoneCode
    }

    private fun setCustomDefaultLanguage(customDefaultLanguage: Language) {
        this.customDefaultLanguage = customDefaultLanguage
        updateLanguageToApply()
        selectedCountry =
            CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                context,
                getLanguageToApply(),
                selectedCCPCountry!!.nameCode
            )!!
    }

    fun showCloseIcon(showCloseIcon: Boolean) {
        this.isShowCloseIcon = showCloseIcon
    }

    private fun getEditText_registeredCarrierNumber(): EditText {
        return this.editText_registeredCarrierNumber!!
    }

    private fun setEditText_registeredCarrierNumber(editText_registeredCarrierNumber: EditText) {
        this.editText_registeredCarrierNumber = editText_registeredCarrierNumber
        updateValidityTextWatcher()
        updateFormattingTextWatcher()
        updateHint()
        requestKeyboard()
    }

    private fun requestKeyboard() {
        editText_registeredCarrierNumber!!.requestFocus()
        (editText_registeredCarrierNumber!!.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    private fun updateValidityTextWatcher() {
        try {
            editText_registeredCarrierNumber!!.removeTextChangedListener(validityTextWatcher)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        reportedValidity = isValidFullNumber()
        if (phoneNumberValidityChangeListener != null) {
            phoneNumberValidityChangeListener!!.onValidityChanged(reportedValidity)
        }
        validityTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (phoneNumberValidityChangeListener != null) {
                    val currentValidity: Boolean
                    currentValidity = isValidFullNumber()
                    if (currentValidity != reportedValidity) {
                        reportedValidity = currentValidity
                        phoneNumberValidityChangeListener!!.onValidityChanged(reportedValidity)
                    }
                }
            }
        }
        editText_registeredCarrierNumber!!.addTextChangedListener(validityTextWatcher)
    }


    fun refreshPreferredCountries() {
        if (countryPreference == null || countryPreference!!.length == 0) {
            preferredCountries = null
        } else {
            val localCCPCountryList = ArrayList<CCPCountry>()
            for (nameCode in countryPreference!!.split((",").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                val ccpCountry = CCPCountry.getCountryForNameCodeFromCustomMasterList(
                    getContext(),
                    customMasterCountriesList,
                    getLanguageToApply(),
                    nameCode
                )
                if (ccpCountry != null) {
                    if (!isAlreadyInList(ccpCountry, localCCPCountryList)) {
                        localCCPCountryList.add(ccpCountry)
                    }
                }
            }
            if (localCCPCountryList.size == 0) {
                preferredCountries = null
            } else {
                preferredCountries = localCCPCountryList
            }
        }
        if (preferredCountries != null) {
            for (CCPCountry in preferredCountries!!) {
                CCPCountry.log()
            }
        } else {
        }
    }

    fun refreshCustomMasterList() {
        if (customMasterCountriesParam == null || customMasterCountriesParam!!.length == 0) {
            if (excludedCountriesParam != null && excludedCountriesParam!!.length != 0) {
                excludedCountriesParam = excludedCountriesParam!!.toLowerCase()
                val libraryMasterList =
                    CCPCountry.getLibraryMasterCountryList(context, getLanguageToApply())
                val localCCPCountryList = ArrayList<CCPCountry>()
                for (ccpCountry in libraryMasterList!!) {
                    if (!excludedCountriesParam!!.contains(ccpCountry.nameCode.toLowerCase())) {
                        localCCPCountryList.add(ccpCountry)
                    }
                }
                if (localCCPCountryList.size > 0) {
                    customMasterCountriesList = localCCPCountryList
                } else {
                    customMasterCountriesList = null
                }
            } else {
                customMasterCountriesList = null
            }
        } else {

            val localCCPCountryList = ArrayList<CCPCountry>()
            for (nameCode in customMasterCountriesParam!!.split((",").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                val ccpCountry =
                    CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                        getContext(),
                        getLanguageToApply(),
                        nameCode
                    )
                if (ccpCountry != null) {
                    if (!isAlreadyInList(ccpCountry, localCCPCountryList)) {
                        localCCPCountryList.add(ccpCountry)
                    }
                }
            }
            if (localCCPCountryList.size == 0) {
                customMasterCountriesList = null
            } else {
                customMasterCountriesList = localCCPCountryList
            }
        }
        if (customMasterCountriesList != null) {

            for (ccpCountry in customMasterCountriesList!!) {
                ccpCountry.log()
            }
        } else {

        }
    }

    fun setCustomMasterCountries(customMasterCountriesParam: String) {
        this.customMasterCountriesParam = customMasterCountriesParam
    }

    fun setExcludedCountries(excludedCountries: String) {
        this.excludedCountriesParam = excludedCountries
        refreshCustomMasterList()
    }

    private fun isAlreadyInList(CCPCountry: CCPCountry, CCPCountryList: List<CCPCountry>): Boolean {
        if (CCPCountry != null && CCPCountryList != null) {
            for (iterationCCPCountry in CCPCountryList) {
                if (iterationCCPCountry.nameCode.equals(CCPCountry.nameCode)) {
                    return true
                }
            }
        }
        return false
    }

    private fun detectCarrierNumber(fullNumber: String, CCPCountry: CCPCountry): String {
        val carrierNumber: String
        if (CCPCountry == null || fullNumber == null || fullNumber.isEmpty()) {
            carrierNumber = fullNumber
        } else {
            val indexOfCode = fullNumber.indexOf(CCPCountry.phoneCode)
            if (indexOfCode == -1) {
                carrierNumber = fullNumber
            } else {
                carrierNumber = fullNumber.substring(indexOfCode + CCPCountry.phoneCode.length)
            }
        }
        return carrierNumber
    }

    private fun getLanguageEnum(index: Int): Language {
        if (index < Language.values().size) {
            return Language.values()[index]
        } else {
            return Language.ENGLISH
        }
    }

    @Deprecated("")
    fun setDefaultCountryUsingPhoneCode(defaultCountryCode: Int) {
        val defaultCCPCountry = CCPCountry.getCountryForCode(
            getContext(), getLanguageToApply(),
            this!!.preferredCountries!!, defaultCountryCode
        )
        if (defaultCCPCountry == null) {
        } else {
            this.defaultCountryCode = defaultCountryCode
            this.defaultCCPCountry = defaultCCPCountry
        }
    }

    fun getDefaultCountryCode(): String {
        return defaultCCPCountry!!.phoneCode
    }

    fun getDefaultCountryNameCode(): String {
        return defaultCCPCountry!!.nameCode.toUpperCase()
    }

    fun resetToDefaultCountry() {
        defaultCCPCountry = CCPCountry.getCountryForNameCodeFromLibraryMasterList(
            getContext(),
            getLanguageToApply(),
            getDefaultCountryNameCode()
        )
        selectedCountry = defaultCCPCountry!!
    }

    fun setCountryForNameCode(countryNameCode: String) {
        val country = CCPCountry.getCountryForNameCodeFromLibraryMasterList(
            getContext(),
            getLanguageToApply(),
            countryNameCode
        )
        if (country == null) {
            if (defaultCCPCountry == null) {
                defaultCCPCountry = preferredCountries?.let {
                    CCPCountry.getCountryForCode(
                        getContext(), getLanguageToApply(),
                        it, defaultCountryCode
                    )
                }
            }
            selectedCountry = this!!.defaultCCPCountry!!
        } else {
            selectedCountry = country
        }
    }

    fun registerCarrierNumberEditText(editTextCarrierNumber: EditText) {
        setEditText_registeredCarrierNumber(editTextCarrierNumber)
    }

    fun setContentColor(contentColor: Int) {
        this.contentColor = contentColor
        textView_selectedCountry!!.setTextColor(this.contentColor)


    }


    fun setFlagBorderColor(borderFlagColor: Int) {
        this.borderFlagColor = borderFlagColor
        linearFlagBorder!!.setBackgroundColor(this.borderFlagColor)
    }

    fun setTextSize(textSize: Int) {
        if (textSize > 0) {
            textView_selectedCountry!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//            setFlagSize(textSize)
        }
    }


    fun showNameCode(showNameCode: Boolean) {
        this.showNameCode = showNameCode
        selectedCountry = selectedCCPCountry!!
    }


    fun setCountryPreference(countryPreference: String) {
        this.countryPreference = countryPreference
    }

    fun changeDefaultLanguage(language: Language) {
        setCustomDefaultLanguage(language)
    }

    fun setOnCountryChangeListener(onCountryChangeListener: OnCountryChangeListener) {
        this.onCountryChangeListener = onCountryChangeListener
    }

    fun setFlagSize(flagSize: Int) {
//        imageViewFlag!!.getLayoutParams().height = flagSize
//        imageViewFlag!!.requestLayout()
    }

    fun showFlag(showFlag: Boolean) {
        this.showFlag = showFlag
        refreshFlagVisibility()
        if (!isInEditMode())
            selectedCountry = selectedCCPCountry!!
    }

    private fun refreshFlagVisibility() {
        if (showFlag) {
            if (ccpUseEmoji) {
                linearFlagHolder!!.setVisibility(GONE)
            } else {
                linearFlagHolder!!.setVisibility(VISIBLE)
            }
        } else {
            linearFlagHolder!!.setVisibility(GONE)
        }
    }

    fun useFlagEmoji(useFlagEmoji: Boolean) {
        this.ccpUseEmoji = useFlagEmoji
        refreshFlagVisibility()
        selectedCountry = selectedCCPCountry!!
    }

    fun showFullName(showFullName: Boolean) {
        this.showFullName = showFullName
        selectedCountry = selectedCCPCountry!!
    }

    fun setPhoneNumberValidityChangeListener(phoneNumberValidityChangeListener: PhoneNumberValidityChangeListener) {
        this.phoneNumberValidityChangeListener = phoneNumberValidityChangeListener
        if (editText_registeredCarrierNumber != null) {
            reportedValidity = isValidFullNumber()
            phoneNumberValidityChangeListener.onValidityChanged(reportedValidity)
        }
    }

    fun setAutoDetectionFailureListener(failureListener: FailureListener) {
        this.failureListener = failureListener
    }

    fun setCustomDialogTextProvider(customDialogTextProvider: CustomDialogTextProvider) {
        this.customDialogTextProvider = customDialogTextProvider
    }

    @JvmOverloads
    fun launchCountrySelectionDialog(countryNameCode: String? = null) {
        CountryCodeDialog.openCountryCodeDialog(this.codePicker!!, countryNameCode)
    }

    private fun getPhoneUtil(): PhoneNumberUtil {
        if (phoneUtil == null) {
            phoneUtil = PhoneNumberUtil.createInstance(context)
        }
        return this!!.phoneUtil!!
    }

    fun setAutoDetectedCountry(loadDefaultWhenFails: Boolean) {
        try {
            var successfullyDetected = false
            for (i in 0 until selectedAutoDetectionPref.representation.length) {
                when (selectedAutoDetectionPref.representation.get(i)) {
                    '1' -> successfullyDetected = detectSIMCountry(false)
                    '2' -> successfullyDetected = detectNetworkCountry(false)
                    '3' -> successfullyDetected = detectLocaleCountry(false)
                }
                if (successfullyDetected) {
                    break
                } else {
                    if (failureListener != null) {
                        failureListener!!.onCountryAutoDetectionFailed()
                    }
                }
            }
            if (!successfullyDetected && loadDefaultWhenFails) {
                resetToDefaultCountry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w(TAG, "setAutoDetectCountry: Exception" + e.message)
            if (loadDefaultWhenFails) {
                resetToDefaultCountry()
            }
        }
    }

    fun detectSIMCountry(loadDefaultWhenFails: Boolean): Boolean {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountryISO = telephonyManager.getSimCountryIso()
            if (simCountryISO == null || simCountryISO.isEmpty()) {
                if (loadDefaultWhenFails) {
                    resetToDefaultCountry()
                }
                return false
            }
            selectedCountry =
                CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                    getContext(),
                    getLanguageToApply(),
                    simCountryISO
                )!!
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            if (loadDefaultWhenFails) {
                resetToDefaultCountry()
            }
            return false
        }
    }

    fun detectNetworkCountry(loadDefaultWhenFails: Boolean): Boolean {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val networkCountryISO = telephonyManager.getNetworkCountryIso()
            if (networkCountryISO == null || networkCountryISO.isEmpty()) {
                if (loadDefaultWhenFails) {
                    resetToDefaultCountry()
                }
                return false
            }
            selectedCountry = CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                getContext(),
                getLanguageToApply(),
                networkCountryISO
            )!!
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            if (loadDefaultWhenFails) {
                resetToDefaultCountry()
            }
            return false
        }
    }

    fun detectLocaleCountry(loadDefaultWhenFails: Boolean): Boolean {
        try {
            val localeCountryISO = context.getResources().getConfiguration().locale.getCountry()
            if (localeCountryISO == null || localeCountryISO.isEmpty()) {
                if (loadDefaultWhenFails) {
                    resetToDefaultCountry()
                }
                return false
            }
            selectedCountry = CCPCountry.getCountryForNameCodeFromLibraryMasterList(
                getContext(),
                getLanguageToApply(),
                localeCountryISO
            )!!
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            if (loadDefaultWhenFails) {
                resetToDefaultCountry()
            }
            return false
        }
    }

    fun setCountryAutoDetectionPref(selectedAutoDetectionPref: AutoDetectionPref) {
        this.selectedAutoDetectionPref = selectedAutoDetectionPref
    }


    fun onUserTappedCountry(CCPCountry: CCPCountry) {
        codePicker!!.storeSelectedCountryNameCode(CCPCountry.nameCode)

        setSelectedCountry(CCPCountry)
    }


    private fun getDefaultCountry(): CCPCountry? {
        return defaultCCPCountry
    }

    private fun setDefaultCountry(defaultCCPCountry: CCPCountry) {
        this.defaultCCPCountry = defaultCCPCountry

    }


    private fun getSelectedCountry(): CCPCountry? {
        if (selectedCCPCountry == null) {
            retrieveSelectedCountry()
            if (selectedCCPCountry == null) {

                setSelectedCountry(getDefaultCountry())
            }
        }

        return selectedCCPCountry
    }

    internal fun setSelectedCountry(selectedCCPCountry: CCPCountry?) {
        var selectedCCPCountry = selectedCCPCountry

        //force disable area isoCountryCode2Digit country detection
        countryDetectionBasedOnAreaAllowed = false
        lastCheckedAreaCode = ""

        //as soon as country is selected, textView should be updated
        if (selectedCCPCountry == null) {
            selectedCCPCountry =
                CCPCountry.getCountryForCode(
                    getContext(), getLanguageToApply(),
                    this!!.preferredCountries!!, defaultCountryCode
                )
            if (selectedCCPCountry == null) {
                return
            }
        }

        this.selectedCCPCountry = selectedCCPCountry

        var displayText = ""

        // add flag if required
        if (showFlag && ccpUseEmoji) {
            if (isInEditMode()) {
                //                android studio preview shows huge space if 0 width space is not added.
                if (ccpUseDummyEmojiForPreview) {
                    //show chequered flag if dummy preview is expected.
                    displayText += "\uD83C\uDFC1\u200B "
                } else {
                    displayText += CCPCountry.getFlagEmoji(selectedCCPCountry) + "\u200B "
                }

            } else {
                displayText += CCPCountry.getFlagEmoji(selectedCCPCountry) + "  "
            }
        }

        // add full name to if required
        if (showFullName) {
            displayText = displayText + selectedCCPCountry.name
        }
        // hide phone isoCountryCode2Digit if required
        if (showPhoneCode) {
            if (displayText.length > 0) {
                displayText += "  "
            }
            displayText += "+" + selectedCCPCountry.phoneCode
        }

        textView_selectedCountry!!.setText(displayText)

        //avoid blank state of ccp
        if (showFlag == false && displayText.length == 0) {
            displayText += "+" + selectedCCPCountry.phoneCode
            textView_selectedCountry!!.setText(displayText)
        }

        if (onCountryChangeListener != null) {
            onCountryChangeListener!!.onCountrySelected()
        }

        imageViewFlag!!.setImageResource(selectedCCPCountry.flagID)

        updateFormattingTextWatcher()

        updateHint()

        //notify to registered validity listener
        if (editText_registeredCarrierNumber != null && phoneNumberValidityChangeListener != null) {
            reportedValidity = isValidFullNumber()
            phoneNumberValidityChangeListener!!.onValidityChanged(reportedValidity)
        }

        //once updates are done, this will release lock
        countryDetectionBasedOnAreaAllowed = true

        //if the country was auto detected based on area isoCountryCode2Digit, this will correct the cursor position.
        if (countryChangedDueToAreaCode) {
            try {
                editText_registeredCarrierNumber!!.setSelection(lastCursorPosition)
                countryChangedDueToAreaCode = false
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        saveSelectedCountry()

    }


    private fun saveSelectedCountry() {
        val sharedPref = context.getSharedPreferences(
            CCP_PREF_FILE, Context.MODE_PRIVATE
        )
        val editor = sharedPref.edit()

        var gson: Gson = Gson()
        var json: String = gson.toJson(selectedCCPCountry);

        editor.putString(selectedCountryTag, json)
        editor.apply()
    }

    private fun retrieveSelectedCountry() {

        val sharedPref = context.getSharedPreferences(
            CCP_PREF_FILE, Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json: String = sharedPref.getString(selectedCountryTag, "") ?: ""

        if (!json.isNullOrEmpty()) {
            selectedCCPCountry = gson.fromJson(json, CCPCountry::class.java)

        }
        setDetectCountryWithAreaCode(true)
    }

    fun setDetectCountryWithAreaCode(detectCountryWithAreaCode: Boolean) {
        updateValidityTextWatcher()

        updateFormattingTextWatcher()
    }

    fun setHintExampleNumberEnabled(hintExampleNumberEnabled: Boolean) {
        this.hintExampleNumberEnabled = hintExampleNumberEnabled
        updateHint()
    }

    fun setHintExampleNumberType(hintExampleNumberType: PhoneNumberType) {
        this.hintExampleNumberType = hintExampleNumberType
        updateHint()
    }

    fun enableDialogInitialScrollToSelection(initialScrollToSelection: Boolean) {
        this.isDialogInitialScrollToSelectionEnabled = isDialogInitialScrollToSelectionEnabled
    }

    fun overrideClickListener(clickListener: View.OnClickListener) {
        customClickListener = clickListener
    }
    @Keep
    enum class Language {
        AFRIKAANS("af"),
        ARABIC("ar"),
        BENGALI("bn"),
        CHINESE_SIMPLIFIED("zh", "CN", "Hans"),
        CHINESE_TRADITIONAL("zh", "TW", "Hant"),
        CZECH("cs"),
        DANISH("da"),
        DUTCH("nl"),
        ENGLISH("en"),
        FARSI("fa"),
        FRENCH("fr"),
        GERMAN("de"),
        GREEK("el"),
        GUJARATI("gu"),
        HEBREW("iw"),
        HINDI("hi"),
        INDONESIA("in"),
        ITALIAN("it"),
        JAPANESE("ja"),
        KOREAN("ko"),
        POLISH("pl"),
        PORTUGUESE("pt"),
        PUNJABI("pa"),
        RUSSIAN("ru"),
        SLOVAK("sk"),
        SPANISH("es"),
        SWEDISH("sv"),
        TURKISH("tr"),
        UKRAINIAN("uk"),
        UZBEK("uz"),
        VIETNAMESE("vi");

        var code: String
        var country: String = ""
        var script: String = ""

        private constructor(code: String, country: String, script: String) {
            this.code = code
            this.country = country
            this.script = script
        }

        private constructor(code: String) {
            this.code = code
        }
    }
    @Keep
    enum class PhoneNumberType {
        MOBILE,
        FIXED_LINE,
        // In some regions (e.g. the USA), it is impossible to distinguish between fixed-line and
        // mobile numbers by looking at the phone number itself.
        FIXED_LINE_OR_MOBILE,
        // Freephone lines
        TOLL_FREE,
        PREMIUM_RATE,
        // The cost of this call is shared between the caller and the recipient, and is hence typically
        // less than PREMIUM_RATE calls. See // http://en.wikipedia.org/wiki/Shared_Cost_Service for
        // more information.
        SHARED_COST,
        // Voice over IP numbers. This includes TSoIP (Telephony Service over IP).
        VOIP,
        // A personal number is associated with a particular person, and may be routed to either a
        // MOBILE or FIXED_LINE number. Some more information can be found here:
        // http://en.wikipedia.org/wiki/Personal_Numbers
        PERSONAL_NUMBER,
        PAGER,
        // Used for "Universal Access Numbers" or "Company Numbers". They may be further routed to
        // specific offices, but allow one number to be used for a company.
        UAN,
        // Used for "Voice Mail Access Numbers".
        VOICEMAIL,
        // A phone number is of type UNKNOWN when it does not fit any of the known patterns for a
        // specific region.
        UNKNOWN
    }
    @Keep
    enum class AutoDetectionPref private constructor(representation: String) {
        SIM_ONLY("1"),
        NETWORK_ONLY("2"),
        LOCALE_ONLY("3"),
        SIM_NETWORK("12"),
        NETWORK_SIM("21"),
        SIM_LOCALE("13"),
        LOCALE_SIM("31"),
        NETWORK_LOCALE("23"),
        LOCALE_NETWORK("32"),
        SIM_NETWORK_LOCALE("123"),
        SIM_LOCALE_NETWORK("132"),
        NETWORK_SIM_LOCALE("213"),
        NETWORK_LOCALE_SIM("231"),
        LOCALE_SIM_NETWORK("312"),
        LOCALE_NETWORK_SIM("321");

        var representation: String

        init {
            this.representation = representation
        }

        companion object {
            fun getPrefForValue(value: String): AutoDetectionPref {
                for (autoDetectionPref in AutoDetectionPref.values()) {
                    if (autoDetectionPref.representation == value) {
                        return autoDetectionPref
                    }
                }
                return SIM_NETWORK_LOCALE
            }
        }
    }
    @Keep
    enum class TextGravity private constructor(i: Int) {
        LEFT(-1), CENTER(0), RIGHT(1);

        var enumIndex: Int = 0

        init {
            enumIndex = i
        }
    }

    interface OnCountryChangeListener {
        fun onCountrySelected()
    }

    interface FailureListener {
        fun onCountryAutoDetectionFailed()
    }

    interface PhoneNumberValidityChangeListener {
        fun onValidityChanged(isValidNumber: Boolean)
    }

    interface DialogEventsListener {
        fun onCcpDialogOpen(dialog: Dialog)
        fun onCcpDialogDismiss(dialogInterface: DialogInterface)
        fun onCcpDialogCancel(dialogInterface: DialogInterface)
    }

    interface CustomDialogTextProvider {
        fun getCCPDialogTitle(language: Language, defaultTitle: String): String
        fun getCCPDialogSearchHintText(language: Language, defaultSearchHintText: String): String
        fun getCCPDialogNoResultACK(language: Language, defaultNoResultACK: String): String
    }

    protected override fun onDetachedFromWindow() {
        CountryCodeDialog.clear()
        super.onDetachedFromWindow()
    }

    companion object {
        val DEFAULT_UNSET = -99
        private var TAG = "CCP"
        private var BUNDLE_SELECTED_CODE = "selectedCode"
        private var LIB_DEFAULT_COUNTRY_CODE = 91
        private val TEXT_GRAVITY_LEFT = -1
        private val TEXT_GRAVITY_RIGHT = 1
        private val TEXT_GRAVITY_CENTER = 0
        private val ANDROID_NAME_SPACE = "http://schemas.android.com/apk/res/android"
    }
}