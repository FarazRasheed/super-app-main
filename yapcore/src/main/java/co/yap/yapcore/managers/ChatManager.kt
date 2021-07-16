package co.yap.yapcore.managers

import android.app.Activity
import co.yap.networking.authentication.AuthApi
import co.yap.networking.authentication.AuthRepository
import co.yap.yapcore.helpers.extentions.toast
import com.liveperson.infra.ConversationViewParams
import com.liveperson.infra.InitLivePersonProperties
import com.liveperson.infra.LPAuthenticationParams
import com.liveperson.infra.LPConversationsHistoryStateToDisplay
import com.liveperson.infra.callbacks.InitLivePersonCallBack
import com.liveperson.messaging.sdk.api.LivePerson
import com.liveperson.messaging.sdk.api.model.ConsumerProfile
import java.lang.ref.WeakReference

object ChatManager {
    private val authRepository: AuthApi = AuthRepository
    private var activity: WeakReference<Activity>? = null
    private val brandId = "17038977"
    private val appInstallId = SessionManager.user?.uuid

    fun config(activity: Activity) {
        this.activity = WeakReference(activity)
        startChat()
    }

    private fun startChat() {
        LivePerson.initialize(
            activity?.get(),
            InitLivePersonProperties(
                brandId, appInstallId,
                object : InitLivePersonCallBack {
                    override fun onInitSucceed() {
                        openChatConversation()
                    }
                    override fun onInitFailed(e: Exception) {
                        activity?.get()?.toast("Unable to open chat")
                    }
                })
        )
    }

    private fun openChatConversation() {
        val authParams = LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH)
        authParams.hostAppJWT = authRepository.getJwtToken()
        val params = ConversationViewParams(false)
            .setHistoryConversationsStateToDisplay(LPConversationsHistoryStateToDisplay.OPEN)
            .setReadOnlyMode(false)
        LivePerson.showConversation(activity?.get(), authParams, params)
        val consumerProfile = ConsumerProfile.Builder()
            .setFirstName(SessionManager.user?.currentCustomer?.firstName)
            .setLastName(SessionManager.user?.currentCustomer?.lastName)
            .setPhoneNumber(SessionManager.user?.currentCustomer?.getCompletePhone())
            .build()
        LivePerson.setUserProfile(consumerProfile)
    }

}