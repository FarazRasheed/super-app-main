package co.yap.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import co.yap.app.YAPApplication
import co.yap.household.onboard.otherscreens.InvalidEIDActivity
import co.yap.modules.dummy.ActivityNavigator
import co.yap.modules.dummy.NavigatorProvider
import co.yap.modules.others.helper.Constants.START_REQUEST_CODE
import co.yap.networking.AppData
import co.yap.networking.RetroNetwork
import co.yap.networking.interfaces.NetworkConstraintsListener
import co.yap.security.AppSignature
import co.yap.security.SecurityHelper
import co.yap.security.SignatureValidator
import co.yap.yapcore.config.BuildConfigManager
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.Constants.EXTRA
import co.yap.yapcore.constants.Constants.KEY_APP_UUID
import co.yap.yapcore.helpers.AuthUtils
import co.yap.yapcore.helpers.NetworkConnectionManager
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.extentions.longToast
import co.yap.yapcore.initializeAdjustSdk
import com.facebook.appevents.AppEventsLogger
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.leanplum.Leanplum
import com.leanplum.LeanplumActivityHelper
import com.yap.yapandroid.LivePersonChat
import com.yap.yapandroid.modules.login.activities.VerifyPassCodePresenterActivity
import timber.log.Timber
import java.util.*

class MainAAPApplication : YAPApplication(), NavigatorProvider {

    private external fun signatureKeysFromJNI(
        name: String,
        flavour: String,
        buildVariant: String,
        applicationId: String,
        versionName: String,
        versionCode: String
    ): AppSignature

    init {
        System.loadLibrary("native-lib")
    }

    override fun onCreate() {
        super.onCreate()
        initFireBase()
        val originalSign =
            signatureKeysFromJNI(
                AppSignature::class.java.canonicalName?.replace(".", "/") ?: "",
                BuildConfig.FLAVOR,
                BuildConfig.BUILD_TYPE,
                BuildConfig.APPLICATION_ID,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            )

        configManager = BuildConfigManager(
            md5 = originalSign.md5,
            sha1 = originalSign.sha1,
            sha256 = originalSign.sha256,
            leanPlumSecretKey = originalSign.leanPlumSecretKey,
            leanPlumKey = originalSign.leanPlumKey,
            adjustToken = originalSign.adjustToken,
            baseUrl = originalSign.baseUrl,
            buildType = originalSign.buildType,
            flavor = originalSign.flavor,
            versionName = originalSign.versionName,
            versionCode = originalSign.versionCode,
            applicationId = originalSign.applicationId,
            sslPin1 = originalSign.sslPin1,
            sslPin2 = originalSign.sslPin2,
            sslPin3 = originalSign.sslPin3,
            sslHost = originalSign.sslHost
        )
        initAllModules()
        SecurityHelper(this, originalSign, object : SignatureValidator {
            override fun onValidate(isValid: Boolean, originalSign: AppSignature?) {
                configManager?.hasValidSignature = true
                //if (originalSign?.isLiveRelease() == true) isValid else true
            }
        })
    }

    private fun initAllModules() {
        initNetworkLayer()
        setAppUniqueId(this)
        inItLeanPlum()
        LivePersonChat.getInstance(applicationContext).registerToLivePersonEvents()
        initializeAdjustSdk(configManager)
        initFacebook()
    }

    private fun initNetworkLayer() {
        RetroNetwork.initWith(this, getAppDataForNetwork(configManager))
        NetworkConnectionManager.init(this)

        RetroNetwork.listenNetworkConstraints(object : NetworkConstraintsListener {
            override fun onInternetUnavailable() {
            }

            override fun onCacheUnavailable() {
            }

            override fun onSessionInvalid() {
                AuthUtils.navigateToSoftLogin(applicationContext)
            }
        })
    }

    private fun initFireBase() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        FirebaseAnalytics.getInstance(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun inItLeanPlum() {
        Leanplum.setApplicationContext(this)
        //Parser.parseVariables(this)
        LeanplumActivityHelper.enableLifecycleCallbacks(this)

        if (configManager?.isReleaseBuild() == true) {
            Leanplum.setAppIdForProductionMode(
                configManager?.leanPlumSecretKey,
                configManager?.leanPlumKey
            )
        } else {
            Leanplum.setAppIdForDevelopmentMode(
                configManager?.leanPlumSecretKey,
                configManager?.leanPlumKey
            )
        }
        Leanplum.setIsTestModeEnabled(false)
        Leanplum.start(this)
    }

    private fun initFacebook() {
        AppEventsLogger.activateApp(this)
    }

    private fun setAppUniqueId(context: Context) {
        var uuid: String?
        val sharedPrefs = SharedPreferenceManager(context)
        sharedPrefs.setThemeValue(Constants.THEME_YAP)
        uuid = sharedPrefs.getValueString(KEY_APP_UUID)
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            sharedPrefs.save(KEY_APP_UUID, uuid)
        }
    }

    override fun onTerminate() {
        NetworkConnectionManager.destroy(this)
        super.onTerminate()
    }

    override fun provideNavigator(): ActivityNavigator {
        return object : ActivityNavigator {
            override fun startEIDNotAcceptedActivity(activity: FragmentActivity) {

                activity.startActivity(
                    Intent(
                        activity,
                        InvalidEIDActivity::class.java
                    )
                )
            }

            override fun startVerifyPassCodePresenterActivity(
                activity: FragmentActivity, bundle: Bundle,
                completionHandler: ((resultCode: Int, data: Intent?) -> Unit)?
            ) {
                try {
                    val intent = Intent(activity, VerifyPassCodePresenterActivity::class.java)
                    intent.putExtra(EXTRA, bundle)
                    (activity as AppCompatActivity).startForResult(intent) { result ->
                        completionHandler?.invoke(result.resultCode, result.data)
                    }.onFailed { result ->
                        completionHandler?.invoke(result.resultCode, result.data)
                    }

                } catch (e: Exception) {
                    if (e is ClassNotFoundException) {
                        longToast("Something went wrong")
                        activity.startActivityForResult(
                            Intent(activity, VerifyPassCodePresenterActivity::class.java),
                            START_REQUEST_CODE
                        )
                    }
                }

            }
        }
    }

    private fun getAppDataForNetwork(configManager: BuildConfigManager?): AppData {
        return AppData(
            flavor = configManager?.flavor ?: "",
            build_type = configManager?.buildType ?: "",
            baseUrl = configManager?.baseUrl ?: "",
            sslPin1 = configManager?.sslPin1 ?: "",
            sslPin2 = configManager?.sslPin2 ?: "",
            sslPin3 = configManager?.sslPin3 ?: "",
            sslHost = configManager?.sslHost ?: ""
        )
    }
}
