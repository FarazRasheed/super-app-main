package co.yap.app

import com.yap.yapandroid.AAPApplication
import com.yap.yapandroid.ConfigurationsUAE

class MainAAPApplication : AAPApplication() {

    override fun onCreate() {

        ConfigurationsUAE.configure(
            flavour = BuildConfig.FLAVOR,
            buildType = BuildConfig.BUILD_TYPE,
            applicationId = BuildConfig.APPLICATION_ID,
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE.toString()
        )
        configManager = ConfigurationsUAE.configManager

        super.onCreate()

    }

}
