package co.yap.yapcore.firebase

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

fun Context?.firebaseTagManagerEvent(tagModel: FirebaseTagManagerModel) {
    this?.let {
        FirebaseAnalytics.getInstance(it).logEvent(tagModel.action, null)
    }
}