package com.yap.yapandroid.modules.login.fragments

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.yap.yapandroid.modules.login.interfaces.ILogin
import com.yap.yapandroid.modules.login.viewmodels.LoginViewModel
import co.yap.widgets.keyboardvisibilityevent.KeyboardVisibilityEvent
import co.yap.widgets.keyboardvisibilityevent.KeyboardVisibilityEventListener
import co.yap.yapcore.constants.Constants.KEY_IS_REMEMBER
import co.yap.yapcore.constants.Constants.KEY_IS_USER_LOGGED_IN
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.extentions.scrollToBottomWithoutFocusChange
import co.yap.yapcore.managers.SessionManager
import com.yap.yapandroid.R
import com.yap.yapandroid.databinding.FragmentLogInBinding
import com.yap.yapandroid.main.MainChildFragment
import kotlinx.android.synthetic.main.fragment_log_in.*
import com.yap.yapandroid.BR


class LoginFragment : MainChildFragment<ILogin.ViewModel>(), ILogin.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_log_in

    private val sharedPreferenceManager: SharedPreferenceManager by lazy {
        SharedPreferenceManager.getInstance(requireContext())
    }

    override val viewModel: LoginViewModel
        get() = ViewModelProviders.of(this).get(LoginViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isAccountBlocked.observe(this, accountBlockedObserver)
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(requireContext())
        if (sharedPreferenceManager.getValueBoolien(
                KEY_IS_USER_LOGGED_IN,
                false
            )
        ) {
            val action =
                LoginFragmentDirections.actionLoginFragmentToVerifyPasscodeFragment("")
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.parentViewModel?.shardPrefs?.getValueBoolien(
                KEY_IS_USER_LOGGED_IN,
                false
            ) == false
        ) {
            etEmailField.requestKeyboard()
        }
        SessionManager.isRemembered.value =
            sharedPreferenceManager.getValueBoolien(KEY_IS_REMEMBER, true)
        SessionManager.isRemembered.value?.let {
            etEmailField.editText.setText(if (it) sharedPreferenceManager.getDecryptedUserName() else "")
            if (etEmailField.editText.length() > 1) etEmailField.editText.setSelection(etEmailField.editText.length())
        }
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel.signInButtonPressEvent.observe(this, signInButtonObserver)
        viewModel.signUpButtonPressEvent.observe(this, signUpButtonObserver)
        viewModel.state.emailError.observe(this, Observer {
            if (!it.isNullOrBlank()) {
                etEmailField.settingUIForError(it)
                etEmailField.settingErrorColor(R.color.error)
            }
        })
        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner, object :
            KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                clSignUp?.post {
                    if (isOpen)
                        scrollView?.scrollToBottomWithoutFocusChange()
                    clSignUp?.visibility = if (isOpen) GONE else VISIBLE
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isAccountBlocked.removeObservers(this)
    }

    override fun onDestroyView() {
        viewModel.signInButtonPressEvent.removeObservers(this)
        viewModel.signUpButtonPressEvent.removeObservers(this)
        viewModel.state.emailError.removeObservers(this)
        super.onDestroyView()
    }

    private val signInButtonObserver = Observer<Boolean> {
        navigateToPassCode()
    }

    private fun navigateToPassCode() {
        val action =
            LoginFragmentDirections.actionLoginFragmentToVerifyPasscodeFragment(
                viewModel.state.twoWayTextWatcher,
                isAccountBlocked = false
            )
        NavHostFragment.findNavController(this).navigate(action)
        viewModel.state.twoWayTextWatcher = ""
    }

    private val accountBlockedObserver = Observer<Boolean> { isAccountBlocked ->
        if (isAccountBlocked) {
            val action =
                LoginFragmentDirections.actionLoginFragmentToVerifyPasscodeFragment(
                    viewModel.state.twoWayTextWatcher,
                    isAccountBlocked
                )
            NavHostFragment.findNavController(this).navigate(action)
            viewModel.state.twoWayTextWatcher = ""
        }
    }

    private val signUpButtonObserver = Observer<Boolean> {
        findNavController().navigate(R.id.action_loginFragment_to_accountSelectionFragment)
    }

    private fun getBindings(): FragmentLogInBinding = viewDataBinding as FragmentLogInBinding
}