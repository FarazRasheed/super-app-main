package com.yap.yapandroid.modules.startup.fragments

import android.os.Bundle
import android.view.View
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.yap.yapandroid.modules.startup.interfaces.ISplash
import com.yap.yapandroid.modules.startup.viewmodels.SplashViewModel
import co.yap.yapcore.constants.Constants.KEY_IS_FIRST_TIME_USER
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.alert
import co.yap.yapcore.helpers.extentions.openPlayStore
import com.yap.yapandroid.R
import com.yap.yapandroid.main.MainChildFragment
import com.yap.yapandroid.BR

class SplashFragment : MainChildFragment<ISplash.ViewModel>(), ISplash.View {

    override fun getBindingVariable() = BR.viewModel
    override fun getLayoutId() = R.layout.fragment_splash

    override val viewModel: SplashViewModel
        get() = ViewModelProviders.of(this).get(SplashViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.splashComplete.observe(this, Observer {
            if (it) viewModel.getAppUpdate()
        })

        viewModel.appUpdate.observe(this, Observer {
            if (it != null && it.androidForceUpdate && Utils.checkForUpdate(
                    BuildConfig.VERSION_NAME,
                    it.androidAppVersionNumber
                )
            ) {
                activity?.alert(
                    getString(R.string.screen_splash_display_text_force_update),
                    getString(R.string.screen_splash_button_force_update),
                    getString(R.string.screen_splash_button_force_update),
                    false
                ) {
                    requireContext().openPlayStore()
                    activity?.finish()
                }
            } else {
                moveNext()
            }
        })
    }

    private fun moveNext() {
        if (viewModel.parentViewModel?.shardPrefs?.getValueBoolien(
                KEY_IS_FIRST_TIME_USER,
                true
            ) == true
        ) {
            viewModel.parentViewModel?.shardPrefs?.save(
                KEY_IS_FIRST_TIME_USER,
                false
            )
            findNavController().navigate(R.id.action_splashFragment_to_accountSelectionFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.splashComplete.removeObservers(this)
    }
}