package com.yap.yapandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import co.yap.app.YAPApplication
import com.liveperson.api.LivePersonCallbackImpl
import com.liveperson.api.LivePersonIntents
import com.liveperson.api.response.types.CloseReason
import com.liveperson.api.sdk.LPConversationData
import com.liveperson.api.sdk.PermissionType
import com.liveperson.infra.LPAuthenticationParams
import com.liveperson.infra.log.LPMobileLog
import com.liveperson.messaging.TaskType
import com.liveperson.messaging.model.AgentData
import com.liveperson.messaging.sdk.api.LivePerson

@Deprecated("Use LivePersonChat like this LivePersonChat.getInstance(applicationContext).registerToLivePersonEvents() ")
open class ChatApplication : YAPApplication() {

    private val authKey = "17038977"
    private val atg = ChatApplication::class.java.simpleName
    private lateinit var instance: ChatApplication
    private var livePersonCallback: LivePersonCallbackImpl? = null
    private lateinit var mLivePersonReceiver: BroadcastReceiver
    private var showToastOnCallback: Boolean = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerToLivePersonEvents()
    }

    private fun registerToLivePersonEvents() {
        createLivePersonReceiver()
        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(
                mLivePersonReceiver,
                LivePersonIntents.getIntentFilterForAllEvents()
            )
    }


    private fun createLivePersonReceiver() {
        mLivePersonReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {

                when (intent.action) {
                    LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_AVATAR_TAPPED_INTENT_ACTION -> onAgentAvatarTapped(
                        LivePersonIntents.getAgentData(intent)
                    )

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_DETAILS_CHANGED_INTENT_ACTION -> {
                        val agentData = LivePersonIntents.getAgentData(intent)
                        onAgentDetailsChanged(agentData)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_TYPING_INTENT_ACTION -> {
                        val isTyping = LivePersonIntents.getAgentTypingValue(intent)
                        onAgentTyping(isTyping)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CONNECTION_CHANGED_INTENT_ACTION -> {
                        val isConnected = LivePersonIntents.getConnectedValue(intent)
                        onConnectionChanged(isConnected)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_MARKED_AS_NORMAL_INTENT_ACTION -> onConversationMarkedAsNormal()

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_MARKED_AS_URGENT_INTENT_ACTION -> onConversationMarkedAsUrgent()

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_RESOLVED_INTENT_ACTION -> {
                        val lpConversationData = LivePersonIntents.getLPConversationData(intent)
                        onConversationResolved(lpConversationData)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_STARTED_INTENT_ACTION -> {
                        val lpConversationData1 = LivePersonIntents.getLPConversationData(intent)
                        onConversationStarted(lpConversationData1)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_FRAGMENT_CLOSED_INTENT_ACTION -> onConversationFragmentClosed()

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_LAUNCHED_INTENT_ACTION -> onCsatLaunched()

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_DISMISSED_INTENT_ACTION -> onCsatDismissed()

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_SKIPPED_INTENT_ACTION -> onCsatSkipped()

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_SUBMITTED_INTENT_ACTION -> {
                        val conversationId = LivePersonIntents.getConversationID(intent)
                        val starRating = LivePersonIntents.getCsatStarRating(intent)
                        onCsatSubmitted(conversationId, starRating)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_ERROR_INTENT_ACTION -> {
                        val type = LivePersonIntents.getOnErrorTaskType(intent)
                        val message = LivePersonIntents.getOnErrorMessage(intent)
                        onError(type, message)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_OFFLINE_HOURS_CHANGES_INTENT_ACTION -> onOfflineHoursChanges(
                        LivePersonIntents.getOfflineHoursOn(intent)
                    )

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_TOKEN_EXPIRED_INTENT_ACTION -> onTokenExpired()

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_USER_DENIED_PERMISSION -> {
                        val deniedPermissionType = LivePersonIntents.getPermissionType(intent)
                        val doNotShowAgainMarked =
                            LivePersonIntents.getPermissionDoNotShowAgainMarked(intent)
                        onUserDeniedPermission(deniedPermissionType, doNotShowAgainMarked)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_USER_ACTION_ON_PREVENTED_PERMISSION -> {
                        val preventedPermissionType = LivePersonIntents.getPermissionType(intent)
                        onUserActionOnPreventedPermission(preventedPermissionType)
                    }

                    LivePersonIntents.ILivePersonIntentAction.LP_ON_STRUCTURED_CONTENT_LINK_CLICKED -> {
                        val uri = LivePersonIntents.getLinkUri(intent)
                        onStructuredContentLinkClicked(uri)
                    }
                }

            }
        }
    }

    fun registerToLivePersonCallbacks() {
        createLivePersonCallback()
        LivePerson.setCallback(livePersonCallback)
    }

    private fun createLivePersonCallback() {
        if (livePersonCallback != null) {
            return
        }
        livePersonCallback = object : LivePersonCallbackImpl() {
            override fun onError(type: TaskType?, message: String?) {
                onError(type!!, message)
            }

            override fun onTokenExpired() {
                onTokenExpired()
            }

            override fun onConversationStarted(convData: LPConversationData?) {
                onConversationStarted(convData!!)
            }

            override fun onConversationResolved(convData: LPConversationData?) {
                onConversationResolved(convData!!)
            }

            override fun onConversationFragmentClosed() {
                onConversationFragmentClosed()
            }

            override fun onConversationResolved(reason: CloseReason?) {
                /*Toast.makeText(getApplicationContext(), "onConversationResolved", Toast.LENGTH_LONG).show();*/
            }

            override fun onConnectionChanged(isConnected: Boolean) {
                onConnectionChanged(isConnected)
            }

            override fun onAgentTyping(isTyping: Boolean) {
                onAgentTyping(isTyping)
            }

            override fun onAgentDetailsChanged(agentData: AgentData?) {
                onAgentDetailsChanged(agentData)
            }

            override fun onCsatLaunched() {
                onCsatLaunched()
            }

            override fun onCsatDismissed() {
                onCsatDismissed()
            }

            override fun onCsatSubmitted(conversationId: String?, starRating: Int) {
                onCsatSubmitted(conversationId, starRating)
            }

            override fun onCsatSkipped() {
                onCsatSkipped()
            }

            override fun onConversationMarkedAsUrgent() {
                onConversationMarkedAsUrgent()
            }

            override fun onConversationMarkedAsNormal() {
                onConversationMarkedAsNormal()
            }

            override fun onOfflineHoursChanges(isOfflineHoursOn: Boolean) {
                onOfflineHoursChanges(isOfflineHoursOn)
            }

            override fun onAgentAvatarTapped(agentData: AgentData?) {
                onAgentAvatarTapped(agentData!!)

            }

            override fun onUserDeniedPermission(
                permissionType: PermissionType?,
                doNotShowAgainMarked: Boolean
            ) {
                onUserDeniedPermission(permissionType!!, doNotShowAgainMarked)
            }

            override fun onUserActionOnPreventedPermission(permissionType: PermissionType?) {
                onUserActionOnPreventedPermission(permissionType!!)
            }

            override fun onStructuredContentLinkClicked(uri: String?) {
                onStructuredContentLinkClicked(uri)
            }
        }
    }


    fun setShowToastOnCallback(showToastOnCallback: Boolean) {
        this.showToastOnCallback = showToastOnCallback
    }

    private fun showToast(message: String) {
        if (showToastOnCallback) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        } else {
            LPMobileLog.d(atg + "_CALLBACK", message)
        }
    }

    private fun onAgentAvatarTapped(agentData: AgentData) {
        showToast("on Agent Avatar Tapped - " + agentData.mFirstName + " " + agentData.mLastName)
    }


    private fun onOfflineHoursChanges(isOfflineHoursOn: Boolean) {
        showToast("on Offline Hours Changes - $isOfflineHoursOn")
    }

    private fun onConversationMarkedAsNormal() {
        showToast("Conversation Marked As Normal")
    }

    private fun onConversationMarkedAsUrgent() {
        showToast("Conversation Marked As Urgent")
    }

    private fun onCsatSubmitted(conversationId: String?, starRating: Int) {
        showToast("CSAT Submitted with ConversationID = " + conversationId + "Star Rating = " + starRating)
    }

    private fun onCsatLaunched() {
        showToast("on CSAT Launched")
    }

    private fun onCsatDismissed() {
        showToast("on CSAT Dismissed")
    }

    private fun onCsatSkipped() {
        showToast("on CSAT Skipped")
    }

    private fun onAgentDetailsChanged(agentData: AgentData?) {
        showToast("Agent Details Changed " + agentData!!)
    }

    private fun onAgentTyping(isTyping: Boolean) {
        showToast("isTyping $isTyping")
    }

    private fun onConnectionChanged(isConnected: Boolean) {
        showToast("onConnectionChanged $isConnected")
    }

    private fun onConversationResolved(convData: LPConversationData) {
        showToast(
            "Conversation resolved " + convData.getId()
                    + " reason " + convData.getCloseReason()
        )
    }

    private fun onConversationStarted(convData: LPConversationData) {
        showToast(
            ("Conversation started " + convData.getId()
                    + " reason " + convData.getCloseReason())
        )
    }

    private fun onConversationFragmentClosed() {
        showToast("Conversation fragment closed")
    }

    private fun onTokenExpired() {
        showToast("onTokenExpired ")

        // Change authentication key here:
        LivePerson.reconnect(
            LPAuthenticationParams().setAuthKey(authKey)
        )
    }

    private fun onError(type: TaskType, message: String?) {
        showToast(" problem " + type.name + " message " + message)
    }

    private fun onUserDeniedPermission(
        permissionType: PermissionType,
        doNotShowAgainMarked: Boolean
    ) {
        showToast("onUserDeniedPermission " + permissionType.name + " doNotShowAgainMarked = " + doNotShowAgainMarked)
    }

    private fun onUserActionOnPreventedPermission(permissionType: PermissionType) {
        showToast("onUserActionOnPreventedPermission " + permissionType.name)
    }

    private fun onStructuredContentLinkClicked(uri: String?) {
        showToast("onStructuredContentLinkClicked. Uri: " + uri!!)
    }

}