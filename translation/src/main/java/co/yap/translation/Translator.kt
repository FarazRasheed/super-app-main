package co.yap.translation

import android.content.Context

object Translator {

    fun getString(context: Context, keyID: Int, vararg args: String): String {
        return context.resources.getString(keyID, *args)
    }

    fun getString(context: Context, keyID: String, vararg args: String): String {
        val stringResourceId = context.resources.getIdentifier(keyID, "string", context.packageName)
        return getString(context, stringResourceId, *args)
    }

    fun getString(context: Context, keyID: Int): String {
        return context.resources.getString(keyID)
    }

    fun getString(context: Context, keyID: String): String {
        val stringResourceId = context.resources.getIdentifier(keyID, "string", context.packageName)
        return getString(context, stringResourceId)
    }

    fun getString(context: Context, keyID: String, value: String): String {
        val stringResourceId = context.resources.getIdentifier(keyID, "string", context.packageName)
        return context.resources.getString(stringResourceId, value)
    }
}
