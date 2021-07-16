package co.yap.yapcore.helpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.content.Intent.ACTION_VIEW
import android.content.res.Resources
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import co.yap.app.YAPApplication
import co.yap.countryutils.country.Country
import co.yap.countryutils.country.utils.Currency
import co.yap.networking.customers.requestdtos.Contact
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.widgets.loading.CircularProgressBar
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.ProductFlavour
import co.yap.yapcore.helpers.extentions.shortToast
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern


@SuppressLint("StaticFieldLeak")
object Utils {
    @JvmStatic
    fun getDimensionsByPercentage(context: Context, width: Int, height: Int): IntArray {
        val dimensions = IntArray(2)
        dimensions[0] = getDimensionInPercent(context, true, width)
        dimensions[1] = getDimensionInPercent(context, false, height)
        return dimensions
    }

    fun getColor(context: Context, @ColorRes color: Int) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(color, null)
        } else {
            context.getColor(color)
        }

    fun requestKeyboard(view: View, request: Boolean, forced: Boolean) {
        view.requestFocus()
        if (forced) {
            (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        } else if (request) {
            (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    fun hideKeyboard(view: View?) {
        view?.let { v ->
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun createProgressDialog(context: Context): Dialog {
        val dialog = Dialog(context, android.R.style.Theme_Light)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.progress_dialogue_fragment)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val progress = dialog.findViewById(R.id.circularProgressBar) as CircularProgressBar
        val layer = dialog.findViewById(R.id.layer) as View

        dialog.setOnShowListener {
            progress.visibility = View.VISIBLE
            layer.visibility = View.VISIBLE
            layer.alpha = 0f
            layer.animate().alpha(0.6f).setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        progress.indeterminateMode = true
                        layer.visibility = View.VISIBLE
                    }
                })
        }
        dialog.setOnDismissListener {
            progress.clearProgressAnimation()
        }
        return dialog
    }

    fun copyToClipboard(context: Context, text: CharSequence) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("YapAccount", text)
        clipboard.setPrimaryClip(clip)
    }

    @Deprecated("Please use toFormattedCurrency() extension method instead")
    fun getFormattedCurrency(num: String?): String {
        return try {
            if ("" != num && null != num) {
                val m = java.lang.Double.parseDouble(num)
                val formatter = DecimalFormat("###,###,##0.00")
                formatter.format(m)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }

    }

    fun getFormattedCurrencyWithoutDecimal(num: String?): String {
        return if ("" != num && null != num) {
            val m = java.lang.Double.parseDouble(num)
            val formatter = DecimalFormat("#,###")
            formatter.format(m)
        } else {
            ""
        }
    }

    fun getFormattedCurrencyWithoutComma(num: String?): String {
        return try {
            if ("" != num && null != num) {
                val m = java.lang.Double.parseDouble(num)
                val formatter = DecimalFormat("########0.00")
                formatter.format(m)
            } else {
                ""
            }
        } catch (ex: Exception) {
            ""
        }
    }

    fun convertDpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
    }

    fun convertPxToDp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
    }

    fun getNavigationBarHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight)
            realHeight - usableHeight
        else
            0
    }

    fun getCardDimensions(context: Context, width: Int, height: Int): IntArray {
        val dimensions = IntArray(2)
        dimensions[0] = getDimensionInPercent(context, true, width)
        dimensions[1] = getDimensionInPercent(context, false, height)
        return dimensions
    }

    fun getDimensionInPercent(context: Context, isWidth: Boolean, percent: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return if (isWidth) {
            ((displayMetrics.widthPixels.toDouble() / 100) * percent).toInt()
        } else {
            if (hasNavBar(context.resources)) {
                //val h = getNavBarHeight(context.resources)
                //val bottom = convertDpToPx(context, 56f)
                (((displayMetrics.heightPixels.toDouble() - getNavBarHeight(context.resources)) / 100) * percent).toInt()
            } else {
                ((displayMetrics.heightPixels.toDouble() / 100) * percent).toInt()
            }
        }
    }

    private fun hasNavBar(resources: Resources): Boolean {
        val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return id > 0 && resources.getBoolean(id)
    }

    private fun getNavBarHeight(resources: Resources): Int {

        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
        return 0
    }

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }

    fun verifyUsername(enteredUsername: String): String {
        var username = enteredUsername
        return if (isUsernameNumeric(username)) {
            when {
                username.startsWith("+") -> {
                    username = username.replace("+", "00")
                    username
                }
                username.startsWith("00") -> username
                username.startsWith("0") -> {
                    username = username.substring(1, username.length)
                    username
                }
                else -> username
            }
        } else {
            username
        }
    }

    fun isUsernameNumeric(username: String): Boolean {
        val inputStr: CharSequence
        var isValid = false
        val expression = "^[0-9+]*\$"

        inputStr = username
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(inputStr)

        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    fun validateEmail(email: String): Boolean {
        var isValidEmail = false
        if ("" == email.trim { it <= ' ' }) {
            isValidEmail = false
        } else if (isValidEmail2(email)) {
            isValidEmail = true
        } else {
            return isValidEmail
        }
        return isValidEmail
    }

    private fun isValidEmail2(email: String): Boolean {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    fun isValidEID(citizenNumber: String?): Boolean {
        return citizenNumber?.let {
            val expression = "^[0-9]{3}-[0-9]{4}-[0-9]{7}-[0-9]{1}$"
            val pattern = Pattern.compile(expression)
            val matcher = pattern.matcher(citizenNumber)
            return matcher.matches()
        } ?: false
    }

    fun setSpan(
        startIndex: Int,
        endIndex: Int,
        wordtoSpan: SpannableString,
        color: Int
    ): SpannableString {
        wordtoSpan.setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        wordtoSpan.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return wordtoSpan
    }

    fun shortName(cardFullName: String): String {
        return shortNameWithEmojiSupport(cardFullName)
    }

    fun shortNameWithEmojiSupport(cardFullName: String): String {
        val emo_regex =
            "(?:[\\uD83C\\uDF00-\\uD83D\\uDDFF]|[\\uD83E\\uDD00-\\uD83E\\uDDFF]|[\\uD83D\\uDE00-\\uD83D\\uDE4F]|[\\uD83D\\uDE80-\\uD83D\\uDEFF]|[\\u2600-\\u26FF]\\uFE0F?|[\\u2700-\\u27BF]\\uFE0F?|\\u24C2\\uFE0F?|[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}|[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?|[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3|[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?|[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?|[\\u2934\\u2935]\\uFE0F?|[\\u3030\\u303D]\\uFE0F?|[\\u3297\\u3299]\\uFE0F?|[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?|[\\u203C\\u2049]\\uFE0F?|[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?|[\\u00A9\\u00AE]\\uFE0F?|[\\u2122\\u2139]\\uFE0F?|\\uD83C\\uDC04\\uFE0F?|\\uD83C\\uDCCF\\uFE0F?|[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?)"

        var cardFullName = cardFullName
        cardFullName = cardFullName.trim { it <= ' ' }
        var shortName = ""
        if (cardFullName.isNotEmpty() && cardFullName.contains(" ")) {
            val nameStr =
                cardFullName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            var firstName = nameStr[0]
            if (Character.isLetter(nameStr[0][0])) {
                firstName = nameStr[0].substring(0, 1)
            }

            var lastName = nameStr[nameStr.size - 1]
            if (Character.isLetter(nameStr[nameStr.size - 1][0])) {
                lastName = nameStr[nameStr.size - 1].substring(0, 1)
            }

            val firstNameMatcher = Pattern.compile(emo_regex).matcher(firstName)
            var isFirstEmoji = false
            var firstData = ""

            while (firstNameMatcher.find()) {
                firstData = firstNameMatcher.group()
                isFirstEmoji = true
                break
            }

            shortName = if (isFirstEmoji) {
                firstData
            } else {
                firstName.substring(0, 1)
            }

            val matcher = Pattern.compile(emo_regex).matcher(lastName)
            var isEmji = false
            var data = ""
            while (matcher.find()) {
                data = matcher.group()
                isEmji = true
                break
            }

            if (isEmji) {
                shortName = shortName + data
            } else {
                shortName = shortName + lastName.substring(0, 1)
            }
            return shortName.toUpperCase()
        } else if (cardFullName.length > 0) {
            val nameStr =
                cardFullName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var firstName = nameStr[0]
            if (Character.isLetter(nameStr[0][0])) {
                firstName = nameStr[0].substring(0, 1)
            }

            val firstNameMatcher = Pattern.compile(emo_regex).matcher(firstName)
            var isFirstEmoji = false
            var firstData = ""

            while (firstNameMatcher.find()) {
                firstData = firstNameMatcher.group()
                isFirstEmoji = true
                break
            }

            shortName = if (isFirstEmoji) {
                firstData
            } else {
                firstName.substring(0, 1)
            }
            return shortName.toUpperCase()
        }
        return shortName.toUpperCase()
    }

    fun formatePhoneWithPlus(phoneNumber: String): String {
        if (phoneNumber.startsWith("00")) {
            return phoneNumber.replaceRange(
                0,
                2,
                "+"
            )
        } else {
            return phoneNumber.replaceRange(
                0,
                0,
                "+"
            )
        }
    }

    fun getFormattedMobileNumber(countryCode: String, mobile: String): String {
        return countryCode.trim() + " " + mobile.trim().replace(countryCode.trim(), "")
    }

    fun openTwitter(context: Context) {
        var intent: Intent?
        try {
            context.packageManager.getPackageInfo("com.twitter.android", 0)
            intent = Intent(
                ACTION_VIEW,
                Uri.parse("twitter.com/intent/follow?screen_name=YapTweets")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // no Twitter app, revert to browser
            context.startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://twitter.com/intent/follow?screen_name=YapTweets")
                )
            )
        }

    }

    fun openInstagram(context: Context) {
        val uri = Uri.parse("https://www.instagram.com/yapnow/")
        val likeIng = Intent(ACTION_VIEW, uri)
        likeIng.setPackage("com.instagram.android")

        try {
            context.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/yapnow/")
                )
            )
        }

    }

    fun getOpenFacebookIntent(context: Context): Intent? {

        return try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            Intent(ACTION_VIEW, Uri.parse("fb://page/288432705359181"))
        } catch (e: Exception) {
            try {
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/Yap-Now-288432705359181/")
                )
            } catch (e: Exception) {
                null
            }

        }

    }

    fun getFormattedPhone(mobileNo: String): String {
        return try {
            val pnu = PhoneNumberUtil.getInstance()
            val pn = pnu.parse(mobileNo, Locale.getDefault().country)
            return pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getFormattedPhoneNumber(context: Context, mobileNo: String): String {
        return try {
            val pnu = PhoneNumberUtil.getInstance()
            val pn = pnu.parse(mobileNo, getDefaultCountryCode(context))
            return pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getSppnableStringForAmount(
        context: Context,
        staticString: String,
        currencyType: String,
        amount: String
    ): SpannableStringBuilder? {
        return try {
            val fcs = ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            val separated = staticString.split(currencyType)
            val str = SpannableStringBuilder(staticString)

            str.setSpan(
                fcs,
                separated[0].length,
                separated[0].length + currencyType.length + (amount.toFormattedCurrency()?.length
                    ?: 0) + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str
        } catch (e: Exception) {
            return null
        }


    }

    fun getSpannableString(
        context: Context,
        staticString: String?,
        startDestination: String?
    ): SpannableStringBuilder? {
        return try {
            val fcs = ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            val separated = staticString?.split(startDestination!!)
            val str = SpannableStringBuilder(staticString)

            str.setSpan(
                fcs,
                separated?.get(0)!!.length,
                separated[0].length + startDestination!!.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun getSpannableStringForLargerBalance(
        context: Context,
        staticString: String,
        currencyType: String,
        amount: String
    ): SpannableStringBuilder? {
        return try {
            var textSize = context.resources.getDimensionPixelSize(R.dimen.text_size_h4)
            val fcs = ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            val fcsLarge = AbsoluteSizeSpan(textSize)
            val separated = staticString.split(currencyType)
            val str = SpannableStringBuilder(staticString)

            str.setSpan(
                fcs,
                separated[0].length,
                separated[0].length + currencyType.length + (amount.toFormattedCurrency()?.length
                    ?: 0) + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str.setSpan(
                fcsLarge,
                separated[0].length,
                separated[0].length + currencyType.length + (amount.toFormattedCurrency()?.length
                    ?: 0) + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            str
        } catch (e: Exception) {
            return null
        }


    }

    @SuppressLint("DefaultLocale")
    fun getCountryCodeFromTelephony(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso.toUpperCase()
    }

    fun getPhoneNumberCountryCodeForAPI(
        defaultCountryCode: String,
        mobileNo: String
    ): String {
        return try {
            val phoneUtil = PhoneNumberUtil.getInstance()
            val pn = phoneUtil.parse(mobileNo, defaultCountryCode)
            //didt find any other way to get number zero
            return """00${pn.countryCode}"""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getCountryCodeFormString(
        region: String
    ): String {
        return try {
            val phoneUtil = PhoneNumberUtil.getInstance()
            val pn = phoneUtil.getCountryCodeForRegion(region)
            return "+$pn"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }


    fun getPhoneWithoutCountryCode(defaultCountryCode: String, mobileNo: String): String {
        return try {
            val phoneUtil = PhoneNumberUtil.getInstance()
            val pn = phoneUtil.parse(mobileNo, defaultCountryCode)
            pn.nationalNumber.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getDefaultCountryCode(context: Context): String {
        val countryCode = getCountryCodeFromTimeZone(context)
        return if (countryCode == "") "AE" else countryCode
    }

    private fun getCountryCodeFromTimeZone(context: Context): String {
        val curTimeZoneId = Calendar.getInstance().timeZone.id
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TimeZone.getRegion(curTimeZoneId)
        } else {
            getCountryCodeFromTelephony(context)
        }
    }

    fun shareText(context: Context, body: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(sharingIntent, "Share"))
    }


    fun getContactColors(context: Context, position: Int): Int {
        return ContextCompat.getColor(context, contactColors[position % contactColors.size])
    }

    fun getBeneficiaryColors(context: Context, position: Int): Int {
        return ContextCompat.getColor(context, beneficiaryColors[position % beneficiaryColors.size])
    }

    fun getContactBackground(context: Context, position: Int) =
        ContextCompat.getDrawable(context, backgrounds[position % backgrounds.size])

    fun getBackgroundColor(context: Context, position: Int) =
        ContextCompat.getColor(context, backgroundColors[position % backgroundColors.size])

    fun getBeneficiaryBackgroundColor(context: Context, position: Int) =
        ContextCompat.getColor(
            context,
            beneficiaryBackgrounds[position % beneficiaryBackgrounds.size]
        )

    private val backgrounds = intArrayOf(
        R.drawable.bg_round_light_red,
        R.drawable.bg_round_light_blue,
        R.drawable.bg_round_light_green,
        R.drawable.bg_round_light_orange
    )

    private val backgroundColors = intArrayOf(
        R.color.bg_round_light_red,
        R.color.bg_round_light_blue,
        R.color.bg_round_light_green,
        R.color.bg_round_light_orange
    )

    private val contactColors = intArrayOf(
        R.color.colorSecondaryMagenta,
        R.color.colorSecondaryBlue,
        R.color.colorSecondaryGreen,
        R.color.colorSecondaryOrange
    )

    private val beneficiaryColors = intArrayOf(
        R.color.colorPrimarySoft,
        R.color.colorSecondaryOrange,
        R.color.colorSecondaryMagenta,
        R.color.colorSecondaryBlue,
        R.color.colorSecondaryGreen,
        R.color.colorPrimary
    )

    private val beneficiaryBackgrounds = intArrayOf(
        R.color.bg_round_light_primary_soft,
        R.color.bg_round_light_orange,
        R.color.bg_round_light_secondary_magenta,
        R.color.bg_round_light_secondary_blue,
        R.color.bg_round_light_green,
        R.color.bg_round_light_primary

    )

    fun getTwoDecimalPlaces(value: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(value).toDouble()
    }

    fun getBody(context: Context, contact: Contact): String {
        return Translator.getString(
            context,
            Strings.common_display_text_y2y_share,
            StringUtils.getFirstname(contact.title!!),
            SessionManager.user?.currentCustomer?.firstName!!,
            getAdjustURL()
        )
    }

    fun getGeneralInvitationBody(context: Context): String {
        return Translator.getString(
            context,
            Strings.common_display_text_y2y_general_share,
            SessionManager.user?.currentCustomer?.firstName!!,
            getAdjustURL()
        )
    }

    fun getFormattedCardNumber(cardNumber: String): String {
        return if (cardNumber.length == 4)
            "XXXX XXXX XXXX $cardNumber"
        else
            "XXXX XXXX XXXX XXXX"
    }

    fun confirmationDialog(
        context: Context,
        title: String?,
        message: String,
        positiveButton: String,
        negitiveButton: String,
        itemClick: OnItemClickListener,
        isCancelable: Boolean = true
    ) {
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle(title).setMessage(message)
            .setPositiveButton(
                positiveButton
            ) { _, _ ->
                itemClick.onItemClick(View(context), true, 0)
            }
            .setNegativeButton(
                negitiveButton
            ) { _, _ ->
                itemClick.onItemClick(View(context), false, 0)
            }
            .setCancelable(isCancelable)
            .show()
    }

    fun showComingSoon(context: Context) {
        context.shortToast("Coming Soon")
    }

    fun formateIbanString(iban: String?): String? {
        iban?.let {
            val sb = StringBuilder()
            for (i in 0..iban.length - 1) {
                if (i % 4 == 0 && i > 0) {
                    sb.append(" ")
                }
                sb.append(iban[i])
            }
            return sb.toString()
        } ?: return null

    }

    fun checkForUpdate(
        existingVersion: String,
        newVersion: String
    ): Boolean {
        var existingVersion = existingVersion
        var newVersion = newVersion
        if (existingVersion.isEmpty() || newVersion.isEmpty()) {
            return false
        }
        existingVersion = existingVersion.replace("\\.".toRegex(), "")
        newVersion = newVersion.replace("\\.".toRegex(), "")
        val existingVersionLength = existingVersion.length
        val newVersionLength = newVersion.length
        val versionBuilder = java.lang.StringBuilder()
        if (newVersionLength > existingVersionLength) {
            versionBuilder.append(existingVersion)
            for (i in existingVersionLength until newVersionLength) {
                versionBuilder.append("0")
            }
            existingVersion = versionBuilder.toString()
        } else if (existingVersionLength > newVersionLength) {
            versionBuilder.append(newVersion)
            for (i in newVersionLength until existingVersionLength) {
                versionBuilder.append("0")
            }
            newVersion = versionBuilder.toString()
        }
        return newVersion.toInt() > existingVersion.toInt()
    }

    fun getOtpBlockedMessage(context: Context): String {
        return "${context.getString(R.string.screen_blocked_otp_display_text_message).format(
            SessionManager.helpPhoneNumber
        )}^${AlertType.DIALOG.name}"
    }

    fun parseCountryList(
        list: List<co.yap.networking.customers.responsedtos.sendmoney.Country>?,
        addOIndex: Boolean = true
    ): ArrayList<Country>? {
        val sortedList = list?.sortedWith(compareBy { it.name })
        var countries: ArrayList<Country> = ArrayList()
        return sortedList?.let { it ->
            countries.clear()
            if (addOIndex) {
                countries.add(
                    0,
                    Country(name = "Select country")
                )
            }
            countries.addAll(it.map {
                Country(
                    id = it.id,
                    isoCountryCode3Digit = it.isoCountryCode2Digit,
                    isoCountryCode2Digit = it.isoCountryCode2Digit,
                    supportedCurrencies = it.currencyList?.filter { curr -> curr.active == true }
                        ?.map { cur ->
                            Currency(
                                code = cur.code,
                                default = cur.default,
                                name = cur.name,
                                active = cur.active,
                                cashPickUp = cur.cashPickUp,
                                rmtCountry = cur.rmtCountry
                            )
                        },
                    active = it.active,
                    isoNum = it.isoNum,
                    signUpAllowed = it.signUpAllowed,
                    name = it.name,
                    currency = getDefaultCurrency(
                        it.currencyList?.filter { curr -> curr.active == true }
                    ),
                    ibanMandatory = it.ibanMandatory
                )
            })
            return@let countries
        }
    }

    private fun getDefaultCurrency(
        activeCurrencies: List<co.yap.networking.customers.responsedtos.sendmoney.Currency>?
    ): Currency? {
        val defaultCurrency = activeCurrencies?.firstOrNull { it.default == true }
        return defaultCurrency?.let { item ->
            return Currency(
                code = item.code,
                default = item.default,
                name = item.name,
                active = item.active,
                cashPickUp = item.cashPickUp,
                rmtCountry = item.rmtCountry
            )
        } ?: getFirst(activeCurrencies)
    }

    private fun getFirst(activeCurrencies: List<co.yap.networking.customers.responsedtos.sendmoney.Currency>?): Currency? {
        return activeCurrencies?.firstOrNull { activeCurr -> activeCurr.active == true }
            ?.let { item ->
                return Currency(
                    code = item.code,
                    default = item.default,
                    name = item.name,
                    active = item.active,
                    cashPickUp = item.cashPickUp,
                    rmtCountry = item.rmtCountry
                )
            }
    }

    fun getAdjustURL(): String {
        val userId = SessionManager.user?.currentCustomer?.customerId
        val date = DateUtils.getCurrentDateWithFormat("yyyy-MM-dd hh:mm:ss")
        val time = date.replace(" ", "_")
        return (when (YAPApplication.configManager?.flavor) {
            ProductFlavour.PROD.flavour -> {
                "https://gqvg.adj.st?adjust_t=n44w5ee_6hpplis&${Constants.REFERRAL_ID}=$userId&${Constants.REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.PREPROD.flavour -> {
                "https://grwl.adj.st?adjust_t=v3jlxlh_oo71763&${Constants.REFERRAL_ID}=$userId&${Constants.REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.STG.flavour -> {
                "https://grwl.adj.st?adjust_t=q3o2z0e_sv94i35&${Constants.REFERRAL_ID}=$userId&${Constants.REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.INTERNAL.flavour -> {
                "https://grwl.adj.st?adjust_t=q3o2z0e_sv94i35&${Constants.REFERRAL_ID}=$userId&${Constants.REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.QA.flavour -> {
                "https://grwl.adj.st?adjust_t=q3o2z0e_sv94i35&${Constants.REFERRAL_ID}=$userId&${Constants.REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.DEV.flavour -> {
                "https://grwl.adj.st?adjust_t=q3o2z0e_sv94i35&${Constants.REFERRAL_ID}=$userId&${Constants.REFERRAL_TIME}=${time.trim()}"
            }
            else -> throw IllegalStateException("Invalid build flavour found ${YAPApplication.configManager?.flavor}")
        })
    }

    @JvmStatic
    fun getConfiguredDecimals(currencyCode: String): Int {
        val allowedDecimal = SessionManager.getCurrencies().firstOrNull {
            it.currencyCode?.toLowerCase() == currencyCode.toLowerCase()
        }?.allowedDecimalsNumber
        return allowedDecimal?.toInt() ?: SessionManager.getDefaultCurrencyDecimals()
    }

    @JvmStatic
    fun getConfiguredDecimalsDashboard(currencyCode: String): Int? {
        val allowedDecimal = SessionManager.getCurrencies().firstOrNull {
            it.currencyCode?.toLowerCase() == currencyCode.toLowerCase()
        }?.allowedDecimalsNumber
        return allowedDecimal?.toInt()
    }

    fun dpToFloat(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        )
    }

    fun spToFloat(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, dp,
            context.resources.displayMetrics
        )
    }


}
