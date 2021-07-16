package com.yap.yapandroid.modules.refreal

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.yap.modules.dashboard.main.activities.YapDashboardActivity
import co.yap.yapcore.adjust.ReferralInfo
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.SharedPreferenceManager
import com.adjust.sdk.Adjust
import com.yap.yapandroid.main.MainActivity

class AdjustReferrerReceiver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let { uri ->
            Adjust.appWillOpenUrl(uri, this)
            val customerId = uri.getQueryParameter(Constants.REFERRAL_ID)
            customerId?.let { cusId ->
                uri.getQueryParameter(Constants.REFERRAL_TIME)?.let { time ->
                    val date = time.replace("_", " ")
                    SharedPreferenceManager(this).setReferralInfo(
                        ReferralInfo(
                            cusId,
                            date
                        )
                    )
                    takeDecision()
                } ?: takeDecision()
            } ?: takeDecision()
        } ?: takeDecision()
    }

    private fun isRunning(ctx: Context): Boolean {
        val activityManager =
            ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks: List<ActivityManager.RunningTaskInfo> =
            activityManager.getRunningTasks(Int.MAX_VALUE)
        for (task in tasks) {
            if (MainActivity::class.java.canonicalName.equals(
                    task.baseActivity?.shortClassName,
                    ignoreCase = true
                )
                || YapDashboardActivity::class.java.canonicalName.equals(
                    task.baseActivity?.shortClassName,
                    ignoreCase = true
                )
            ) {
                return true
            }
        }
        return false
    }

    private fun takeDecision() {
        if (isRunning(this))
            finish()
        else
            startLauncherActivity()
    }

    private fun startLauncherActivity() {
        startActivity(packageManager.getLaunchIntentForPackage(packageName))
        finish()
    }
}
