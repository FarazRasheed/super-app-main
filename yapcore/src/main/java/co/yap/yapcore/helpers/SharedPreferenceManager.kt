package co.yap.yapcore.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import co.yap.yapcore.adjust.ReferralInfo
import co.yap.yapcore.constants.Constants.KEY_PASSCODE
import co.yap.yapcore.constants.Constants.KEY_THEME
import co.yap.yapcore.constants.Constants.KEY_USERNAME
import com.google.gson.Gson
import java.lang.Exception

class SharedPreferenceManager(val context: Context) {

    private val PREFS_NAME = "YAPPref"
    private val inviterAdjustId = "inviterAdjustId"
    private var masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private var sharedPref = EncryptedSharedPreferences.create(
        PREFS_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object : SingletonHolder<SharedPreferenceManager, Context>(::SharedPreferenceManager)

    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolien(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        try {
            val editor: SharedPreferences.Editor = sharedPref.edit()
            val allEntr: Map<String, *> = sharedPref.all
            for (entry in allEntr.keys) {
                editor.remove(entry)
            }
            editor.apply()
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }

    fun saveUserNameWithEncryption(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_USERNAME, text)
        editor.apply()

    }

    private fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())  //match a number with optional '-' and decimal.
    }

    fun getDecryptedUserName(): String? {
        return getValueString(KEY_USERNAME)
    }

    fun savePassCodeWithEncryption(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_PASSCODE, text)
        editor.apply()

    }

    fun getDecryptedPassCode(): String? {
        return getValueString(KEY_PASSCODE)
    }

    fun getThemeValue(): String? {
        return sharedPref.getString(KEY_THEME, "")
    }

    fun setThemeValue(themeValue: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_THEME, themeValue)
        editor.apply()
    }

    fun getReferralInfo(): ReferralInfo? {
        return getValueString(inviterAdjustId)?.let {
            return if (it.isNullOrBlank()) {
                null
            } else {
                Gson().fromJson(
                    getValueString(inviterAdjustId),
                    ReferralInfo::class.java
                )
            }
        }
    }

    fun setReferralInfo(referralInfo: ReferralInfo?) {
        save(
            inviterAdjustId,
            if (referralInfo != null) Gson().toJson(referralInfo) else ""
        )
    }

}
