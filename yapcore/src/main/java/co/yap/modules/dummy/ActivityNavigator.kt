package co.yap.modules.dummy

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

interface ActivityNavigator {
    fun startEIDNotAcceptedActivity(activity: FragmentActivity)
    fun startVerifyPassCodePresenterActivity(
        activity: FragmentActivity,
        bundle: Bundle = Bundle(),
        completionHandler: ((resultCode: Int, data: Intent?) -> Unit)?
    )
}