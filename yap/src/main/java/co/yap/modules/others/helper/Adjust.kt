package co.yap.modules.others.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustReferrerReceiver
import java.net.URL


class InstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) { // Adjust receiver.
        AdjustReferrerReceiver().onReceive(context, intent)

        val rawReferrer = intent!!.getStringExtra(com.adjust.sdk.Constants.REFERRER) ?: return
//        inviter=abd123

        Adjust.getDefaultInstance().sendReferrer(rawReferrer, context)
//        if (null != rawReferrer) {
//
//            INVITER_ADJUST_ID_TEST = rawReferrer.toString()
//            context?.let {
//                SharedPreferenceManager(it).save(
//                    Constants.INVITER_ADJUST_ID_TEST,
//                    rawReferrer.toString() + "local uri"
//                )
//            }
//
//            // And any other receiver which needs the intent.
//        }

        val data: Uri? = intent?.data
        Adjust.appWillOpenUrl(data, context)

        if (null != data) {
            Log.v(" Adjust", "data " + data)

            val url = URL(
                data?.scheme,
                data?.host,
                data?.path
            )
//            INVITER_ADJUST_ID_TEST = data.toString()
//            context?.let {
//                SharedPreferenceManager(it).save(
//                    Constants.INVITER_ADJUST_ID_TEST,
//                    data.toString() + "local uri"
//                )
//            }
            Log.v(" Adjust", "InstallReceiver " + rawReferrer)

            // And any other receiver which needs the intent.
        }
    }
}