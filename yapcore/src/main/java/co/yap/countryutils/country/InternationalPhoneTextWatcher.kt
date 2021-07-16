package co.yap.countryutils.country

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.Selection
import android.text.TextUtils
import android.text.TextWatcher
import com.google.i18n.phonenumbers.AsYouTypeFormatter
import com.google.i18n.phonenumbers.PhoneNumberUtil

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class InternationalPhoneTextWatcher(
    countryNameCode: String?,
    countryPhoneCode: Int,
    internationalOnly: Boolean
) :
    TextWatcher {
    private var phoneNumberUtil: PhoneNumberUtil
    /**
     * Indicates the change was caused by ourselves.
     */
    private var mSelfChange = false
    /**
     * Indicates the formatting has been stopped.
     */
    private var mStopFormatting = false
    private var mFormatter: AsYouTypeFormatter? = null
    private var countryNameCode: String? = null
    private var lastFormatted: Editable? = null
    private var countryPhoneCode = 0
    //when country is changed, we update the number.
//at this point this will avoid "stopFormatting"
    private var needUpdateForCountryChange = false
    private val internationalOnly: Boolean

    private fun updateCountry(countryNameCode: String?, countryPhoneCode: Int) {
        this.countryNameCode = countryNameCode
        this.countryPhoneCode = countryPhoneCode
        mFormatter = phoneNumberUtil.getAsYouTypeFormatter(countryNameCode)
        //mFormatter.clear()
        if (lastFormatted != null) {
            needUpdateForCountryChange = true
            val onlyDigits: String = PhoneNumberUtil.normalizeDigitsOnly(lastFormatted.toString())
            lastFormatted!!.replace(0, lastFormatted?.length?:0, onlyDigits, 0, onlyDigits.length)
            needUpdateForCountryChange = false
        }
    }

    override
    fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int,
        after: Int
    ) {
        if (mSelfChange || mStopFormatting) {
            return
        }
        // If the user manually deleted any non-dialable characters, stop formatting
        if (count > 0 && hasSeparator(s, start, count) && !needUpdateForCountryChange) {
            stopFormatting()
        }
    }

    override
    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (mSelfChange || mStopFormatting) {
            return
        }
        // If the user inserted any non-dialable characters, stop formatting
        if (count > 0 && hasSeparator(s, start, count)) {
            stopFormatting()
        }
    }

    @Synchronized
    override
    fun afterTextChanged(s: Editable) {
        if (mStopFormatting) { // Restart the formatting when all texts were clear.
            mStopFormatting = !(0 === s.length)
            return
        }
        if (mSelfChange) { // Ignore the change caused by s.replace().
            return
        }
        //calculate few things that will be helpful later
        val selectionEnd: Int = Selection.getSelectionEnd(s)
        val isCursorAtEnd = selectionEnd == s.length
        //get formatted text for this number
        val formatted = reformat(s)
        //now calculate cursor position in formatted text
        var finalCursorPosition = 0
        if (formatted == s.toString()) { //means there is no change while formatting don't move cursor
            finalCursorPosition = selectionEnd
        } else if (isCursorAtEnd) { //if cursor was already at the end, put it at the end.
            finalCursorPosition = formatted.length
        } else { // if no earlier case matched, we will use "digitBeforeCursor" way to figure out the cursor position
            var digitsBeforeCursor = 0
            for (i in 0..s.length) {
                if (i >= selectionEnd) {
                    break
                }
                if (PhoneNumberUtils.isNonSeparator(s[i])) {
                    digitsBeforeCursor++
                }
            }
            //at this point we will have digitsBeforeCursor calculated.
// now find this position in formatted text
            var i = 0
            var digitPassed = 0
            while (i < formatted.length) {
                if (digitPassed == digitsBeforeCursor) {
                    finalCursorPosition = i
                    break
                }
                if (PhoneNumberUtils.isNonSeparator(formatted[i])) {
                    digitPassed++
                }
                i++
            }
        }
        //if this ends right before separator, we might wish to move it further so user do not delete separator by mistake.
// because deletion of separator will cause stop formatting that should not happen by mistake
        if (!isCursorAtEnd) {
            while (0 < finalCursorPosition - 1 && !PhoneNumberUtils.isNonSeparator(
                    formatted[finalCursorPosition - 1]
                )
            ) {
                finalCursorPosition--
            }
        }
        //Now we have everything calculated, set this values in
        try {
            mSelfChange = true
            s.replace(0, s.length, formatted, 0, formatted.length)
            mSelfChange = false
            lastFormatted = s
            Selection.setSelection(s, finalCursorPosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * this will format the number in international format (only).
     */
    private fun reformat(char: CharSequence): String {
        var s = char
        var internationalFormatted = ""
        mFormatter?.clear()
        var lastNonSeparator = 0.toChar()
        val countryCallingCode = "+$countryPhoneCode"
        if (internationalOnly || s.isNotEmpty() && s[0] != '0') //to have number formatted as international format, add country code before that
            s = countryCallingCode + s
        val len = s.length
        for (i in 0 until len) {
            val c = s[i]
            if (PhoneNumberUtils.isNonSeparator(c)) {
                if (lastNonSeparator.toInt() != 0) {
                    internationalFormatted = mFormatter?.inputDigit(lastNonSeparator).toString()
                }
                lastNonSeparator = c
            }
        }
        if (lastNonSeparator.toInt() != 0) {
            internationalFormatted = mFormatter?.inputDigit(lastNonSeparator) ?: ""
        }
        internationalFormatted = internationalFormatted.trim { it <= ' ' }
        if (internationalOnly || s.isEmpty() || s[0] != '0') {
            internationalFormatted =
                if (internationalFormatted.length > countryCallingCode.length) {
                    if (internationalFormatted[countryCallingCode.length] == ' ') internationalFormatted.substring(
                        countryCallingCode.length + 1
                    ) else internationalFormatted.substring(countryCallingCode.length)
                } else {
                    ""
                }
        }
        return if (TextUtils.isEmpty(internationalFormatted)) "" else internationalFormatted
    }

    private fun stopFormatting() {
        mStopFormatting = true
        mFormatter?.clear()
    }

    private fun hasSeparator(s: CharSequence, start: Int, count: Int): Boolean {
        for (i in start until start + count) {
            val c = s[i]
            if (!PhoneNumberUtils.isNonSeparator(c)) {
                return true
            }
        }
        return false
    }

    init {
        require(!(countryNameCode == null || countryNameCode.isEmpty()))
        phoneNumberUtil = PhoneNumberUtil.getInstance()
        updateCountry(countryNameCode, countryPhoneCode)
        this.internationalOnly = internationalOnly
    }
}