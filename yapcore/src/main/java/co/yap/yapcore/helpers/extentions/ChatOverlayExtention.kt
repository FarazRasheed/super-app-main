package co.yap.yapcore.helpers.extentions

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import co.yap.networking.authentication.AuthRepository
import co.yap.widgets.CounterFloatingActionButton
import co.yap.yapcore.R
import co.yap.yapcore.managers.SessionManager
import com.leanplum.Leanplum
import com.liveperson.infra.ConversationViewParams
import com.liveperson.infra.InitLivePersonProperties
import com.liveperson.infra.LPAuthenticationParams
import com.liveperson.infra.LPConversationsHistoryStateToDisplay
import com.liveperson.infra.callbacks.InitLivePersonCallBack
import com.liveperson.messaging.sdk.api.LivePerson
import com.liveperson.messaging.sdk.api.model.ConsumerProfile

const val BRAND_ID: String = "17038977"

fun Activity.initializeChatOverLayButton(unreadCount: Int) {
    if (unreadCount <= 0) return
    val param = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.WRAP_CONTENT,
        FrameLayout.LayoutParams.WRAP_CONTENT
    )
    param.setMargins(
        dimen(R.dimen.margin_medium),
        dimen(R.dimen.margin_medium),
        dimen(R.dimen.margin_medium),
        dimen(R.dimen.margin_btn_side_paddings_xl)
    )
    param.gravity = Gravity.END or Gravity.BOTTOM
    val view = layoutInflater.inflate(
        R.layout.layout_overlay_live_chat,
        null
    ) as? CounterFloatingActionButton
    (window.decorView as FrameLayout).findViewById<FrameLayout>(android.R.id.content)
        .addView(view, param)
    view?.count = unreadCount
    view?.setOnClickListener { chatSetup() }
}

fun Activity.overLayButtonVisibility(visibility: Int) {
    if (Leanplum.getInbox().unreadCount() > 0)
        (window.decorView as FrameLayout).findViewById<View>(R.id.faLiveChat).visibility =
            visibility
}

fun Activity.chatSetup() {
    SessionManager.user?.let {
//        val monitoringParams = MonitoringInitParams(appInstallId)
        LivePerson.initialize(
            this,
            InitLivePersonProperties(
                BRAND_ID, it.uuid,
                object : InitLivePersonCallBack {
                    override fun onInitSucceed() {
                        openChatActivity()
                    }

                    override fun onInitFailed(e: Exception) {
                        toast("Unable to open chat")
                    }
                })
        )
    }
}

private fun Activity.openChatActivity() {
    SessionManager.user?.currentCustomer?.let {
        val authParams = LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH)
        authParams.hostAppJWT = AuthRepository.getJwtToken()
//        authParams.hostAppJWT = CookiesManager.jwtToken
        val params = ConversationViewParams(false)
            .setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.OPEN)
            .setReadOnlyMode(false)
        LivePerson.showConversation(this, authParams, params)
        val consumerProfile = ConsumerProfile.Builder()
            .setFirstName(it.firstName)
            .setLastName(it.lastName)
            .setPhoneNumber(it.getCompletePhone())
            .build()
        LivePerson.setUserProfile(consumerProfile)
    }
}