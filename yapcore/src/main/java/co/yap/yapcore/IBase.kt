package co.yap.yapcore

import android.content.Context
import co.yap.yapcore.helpers.NetworkConnectionManager

interface IBase {
    interface View<V : ViewModel<*>> : NetworkConnectionManager.OnNetworkStateChangeListener {
        val viewModel: V
        fun showLoader(isVisible: Boolean)
        fun showToast(msg: String)
        fun showInternetSnack(isVisible: Boolean)
        fun isPermissionGranted(permission: String): Boolean
        fun requestPermissions()
        fun getString(resourceKey: String): String
    }

    interface ViewModel<S : State> : ILifecycle {
        val state: S
        val context: Context
        val toolBarClickEvent: SingleClickEvent
        fun getString(resourceId: Int): String
        fun getString(resourceId: String): String
    }

    interface State {
        var toast: String
        var loading: Boolean
        var toolbarTitle: String
        var error: String
        fun reset()
        // fun getString(key: String): String
        fun destroy()

        fun init()
        fun resume()
        fun pause()
    }
}
