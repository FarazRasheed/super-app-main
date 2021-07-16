package co.yap.yapcore

import android.app.Activity
import android.app.Application
import android.os.Bundle
import co.yap.yapcore.config.BuildConfigManager
import co.yap.yapcore.enums.ProductFlavour
import co.yap.yapcore.managers.SessionManager
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.AdjustEvent
import com.adjust.sdk.LogLevel

/*
* Following sdk's included
* -> Adjust SDK
* */

fun Application.initializeAdjustSdk(configManager: BuildConfigManager?) {

    configManager?.let { configurations ->
        val config = AdjustConfig(
            this,
            configurations.adjustToken,
            if (configurations.isReleaseBuild()) AdjustConfig.ENVIRONMENT_PRODUCTION else AdjustConfig.ENVIRONMENT_SANDBOX
        )

        when (configurations.flavor) {
            ProductFlavour.PROD.flavour -> {
                config.setAppSecret(1, 325677892, 77945854, 746350982, 870707894)
            }
            ProductFlavour.PREPROD.flavour -> {
                config.setAppSecret(1, 82588340, 60633897, 806753301, 962146915)
            }
            ProductFlavour.STG.flavour -> {
                config.setAppSecret(1, 1236756048, 110233912, 2039250280, 199413548)
            }
            ProductFlavour.INTERNAL.flavour -> {
                config.setAppSecret(1, 1236756048, 110233912, 2039250280, 199413548)
            }
            ProductFlavour.QA.flavour -> {
            }
            ProductFlavour.DEV.flavour -> {
            }
            else -> throw IllegalStateException("Invalid build flavour found ${configurations.flavor}")
        }

        if (!configurations.isReleaseBuild()) config.setLogLevel(LogLevel.VERBOSE)
        config.setSendInBackground(true)
        config.setOnAttributionChangedListener {}
        config.setOnEventTrackingSucceededListener {}
        config.setOnEventTrackingFailedListener { }
        config.setOnSessionTrackingSucceededListener { }
        config.setOnSessionTrackingFailedListener { }
        config.setOnSessionTrackingFailedListener {}
        config.setOnDeeplinkResponseListener { true }
        Adjust.onCreate(config)
        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())
    }
}

private class AdjustLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityResumed(activity: Activity?) {
        Adjust.onResume()
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    override fun onActivityPaused(activity: Activity?) {
        Adjust.onPause()
    }
}

fun fireAdjustEvent(event: String) {
    val adjustEvent = AdjustEvent(event)
    adjustEvent.setCallbackId(SessionManager.user?.currentCustomer?.customerId)
    adjustEvent.addCallbackParameter("account_id", SessionManager.user?.currentCustomer?.customerId)
    Adjust.trackEvent(adjustEvent)
}

class AdjustEvents {
    companion object {
        fun trackAdjustPlatformEvent(eventName: String, value: String = "") {
            fireAdjustEvent(eventName)
        }
    }
}
