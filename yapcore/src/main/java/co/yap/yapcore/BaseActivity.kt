package co.yap.yapcore

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import co.yap.app.YAPApplication
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.YAPThemes
import co.yap.yapcore.helpers.*
import co.yap.yapcore.helpers.extentions.preventTakeScreenShot
import co.yap.yapcore.helpers.extentions.toast
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity<V : IBase.ViewModel<*>> : AppCompatActivity(), IBase.View<V>,
    NetworkConnectionManager.OnNetworkStateChangeListener,
    PermissionsManager.OnPermissionGrantedListener {

    private var snackbar: Snackbar? = null
    private var DURATION_CODE = -2
    private var checkConnectivity: Boolean = true
    private lateinit var permissionsManager: PermissionsManager
    private var progress: Dialog? = null
    open lateinit var context: Context
    open fun onToolBarClick(id: Int) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
//        setUpFirebaseAnalytics()

        applySelectedTheme(SharedPreferenceManager(this))
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        NetworkConnectionManager.init(this)
        NetworkConnectionManager.subscribe(this)
        permissionsManager = PermissionsManager(this, this, this)
        registerStateListeners()

        progress = Utils.createProgressDialog(this)
        preventTakeScreenShot(YAPApplication.configManager?.isReleaseBuild() == true)
        viewModel.toolBarClickEvent.observe(this, Observer {
            onToolBarClick(it)
        })
    }

    private fun applySelectedTheme(prefs: SharedPreferenceManager) {
        when (prefs.getThemeValue()) {
            Constants.THEME_YAP -> {
                setScreenState(YAPThemes.CORE())
            }
            Constants.THEME_HOUSEHOLD -> {
                setScreenState(YAPThemes.HOUSEHOLD())
            }
            else -> {// default
                setScreenState(YAPThemes.CORE())
            }
        }
    }

    private fun setScreenState(screenState: YAPThemes) {
        when (screenState) {
            is YAPThemes.CORE -> {
                theme.applyStyle(R.style.CoreAppTheme, true)
            }
            is YAPThemes.HOUSEHOLD -> {
                theme.applyStyle(R.style.AppThemeHouseHold, true)
            }
            else -> {
                theme.applyStyle(R.style.CoreAppTheme, true)
            }
        }
    }

    fun hideKeyboard() = Utils.hideKeyboard(this.currentFocus)

    override fun showToast(msg: String) {
        if ("" != msg.trim { it <= ' ' }) {
            val messages = msg.split("^")
            if (msg.contains("^")) {
                when (messages.last()) {
                    AlertType.TOAST.name -> toast(messages.first())
                    AlertType.DIALOG.name -> {
                        showAlertDialogAndExitApp(
                            "",
                            message = messages.first(),
                            closeActivity = false
                        )
                    }
                    AlertType.DIALOG_WITH_FINISH.name -> showAlertDialogAndExitApp(
                        "",
                        message = messages.first(),
                        closeActivity = true
                    )
                    AlertType.DIALOG_WITH_CUSTOM_BUTTON_TEXT.name -> showAlertDialogAndExitApp(
                        "",
                        message = messages.first(),
                        rightButtonText = "CLOSE",
                        closeActivity = true
                    )
                    AlertType.DIALOG_WITH_CLICKABLE.name -> {
                        showAlertDialogAndExitApp(
                            Title = "",
                            message = messages.first(),
                            closeActivity = false,
                            isOtpBlocked = true
                        )
                    }
                }
            } else {
                toast(messages.first())
            }
        }
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
        if (checkConnectivity && isConnected) {
            checkConnectivity = false

        } else {
            showInternetSnack(!isConnected)
            checkConnectivity = false
        }
    }

    override fun showInternetSnack(isVisible: Boolean) {
        if (isVisible) showNoInternetSnackBar() else showInternetConnectedSnackBar()
    }

    private fun showNoInternetSnackBar() {
        snackbar = setSnackBar(
            this,
            getString(Strings.common_display_text_error_no_internet),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(
                "Settings"
            ) { startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
            .setActionTextColor(Utils.getColor(this, R.color.colorDarkGreen))
        snackbar?.show()
    }

    private fun showInternetConnectedSnackBar() {
        val snackbarConnected = setSnackBar(
            this,
            "Internet connected.",
            Snackbar.LENGTH_SHORT
        )
        snackbarConnected.show()
        snackbar?.dismiss()
    }

    override fun showLoader(isVisible: Boolean) {
        if (isVisible) progress?.show() else progress?.dismiss()
        Utils.hideKeyboard(this.window.decorView)
    }

    private fun setSnackBar(activity: Activity, message: String, duration: Int): Snackbar {
        val layout: View
        val snackbar = Snackbar
            .make(activity.findViewById(android.R.id.content), message, duration)
        layout = snackbar.view
        layout.setBackgroundColor(activity.getColor(R.color.colorDarkGreen))
        val text =
            layout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        text.setTextColor(activity.getColor(R.color.colorWhite))

        if (duration == DURATION_CODE) {
//            layout.setBackgroundColor(activity.getColor(R.color.colorAccent))
            layout.setBackgroundColor(ThemeColorUtils.colorPrimaryAttribute(this))
//            layout.setBackgroundColor(Color.RED)

            val snackbarView = snackbar.view
            val textView =
                snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
        }
        return snackbar
    }

    override fun onDestroy() {
        NetworkConnectionManager.unsubscribe(this)
        unregisterStateListeners()
        cancelAllSnackBar()
        progress?.dismiss()
        viewModel.toolBarClickEvent.removeObservers(this)
        super.onDestroy()
    }

    override fun onPermissionGranted(permission: String?) {
    }

    override fun onPermissionNotGranted(permission: String?) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Checks if user has granted the specific permission
     */
    override fun isPermissionGranted(permission: String): Boolean {
        return permissionsManager.isPermissionGranted(permission)
    }

    /**
     * Request permissions explicitly
     */
    override fun requestPermissions() {
        return permissionsManager.requestAppPermissions()
    }

    override fun getString(resourceKey: String): String = Translator.getString(this, resourceKey)

    private val stateObserver = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (propertyId == BR.toast && viewModel.state.toast.isNotBlank()) {
                showToast(viewModel.state.toast)
            }
            if (propertyId == BR.loading) {
                showLoader(viewModel.state.loading)
            }
        }
    }

    private fun registerStateListeners() {
        if (viewModel is BaseViewModel<*>) {
            viewModel.registerLifecycleOwner(this)
        }
        if (viewModel.state is BaseState) {
            (viewModel.state as BaseState).addOnPropertyChangedCallback(stateObserver)
        }
    }

    private fun unregisterStateListeners() {
        if (viewModel is BaseViewModel<*>) {
            viewModel.unregisterLifecycleOwner(this)
        }
        if (viewModel.state is BaseState) {
            (viewModel.state as BaseState).removeOnPropertyChangedCallback(stateObserver)
        }
    }
}
