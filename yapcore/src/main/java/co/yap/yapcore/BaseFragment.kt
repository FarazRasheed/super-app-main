package co.yap.yapcore

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import co.yap.translation.Translator
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnBackPressedListener


abstract class BaseFragment<V : IBase.ViewModel<*>> : BaseNavFragment(), IBase.View<V>,
    OnBackPressedListener {
    private var progress: Dialog? = null
    open fun onToolBarClick(id: Int) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        context?.let { progress = Utils.createProgressDialog(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerStateListeners()
        viewModel.toolBarClickEvent.observe(this, Observer {
            onToolBarClick(it)
        })
    }

    override fun onDestroyView() {
        unregisterStateListeners()
        progress?.dismiss()
        viewModel.toolBarClickEvent.removeObservers(this)
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentHolder) {
            context.onFragmentAttached()
        } else {
            throw IllegalStateException("Could not find reference to IFragmentHolder. Make sure parent activity implements IFragmentHolder interface")
        }

        if (context !is IBase.View<*>) {
            throw IllegalStateException("Could not find reference to IBase.View. Make sure parent activity implements IBase.View interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (getFragmentHolder() != null) {
            getFragmentHolder()?.onFragmentDetached("")
        }
    }


    override fun showLoader(isVisible: Boolean) {
        if (isVisible) {
            if (isResumed && userVisibleHint) {
                if (progress == null) {
                    context?.let { progress = Utils.createProgressDialog(it) }
                    progress?.show()
                } else {
                    progress?.show()
                }
            }
        } else {
            progress?.dismiss()
        }
        Utils.hideKeyboard(this.view)
        //getBaseView()?.showLoader(isVisible)
    }

    override fun showToast(msg: String) {
        getBaseView()?.showToast(msg)
    }

    private fun getFragmentHolder(): IFragmentHolder? {
        if (context is IFragmentHolder) {
            return context as IFragmentHolder
        }

        return null
    }

    private fun getBaseView(): IBase.View<*>? {
        if (context is IBase.View<*>) {
            return context as IBase.View<*>
        }

        return null
    }

    override fun showInternetSnack(isVisible: Boolean) {
        getBaseView()?.showInternetSnack(isVisible)
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
        getBaseView()?.onNetworkStateChanged(isConnected)
    }

    override fun isPermissionGranted(permission: String): Boolean {
        return getBaseView()?.isPermissionGranted(permission)!!
    }

    override fun requestPermissions() {
        getBaseView()?.requestPermissions()
    }

    override fun getString(resourceKey: String): String =
        Translator.getString(requireContext(), resourceKey)

    fun getString(resourceKey: String, vararg arg: String): String =
        Translator.getString(requireContext(), resourceKey, *arg)

    override fun onBackPressed(): Boolean = false

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
