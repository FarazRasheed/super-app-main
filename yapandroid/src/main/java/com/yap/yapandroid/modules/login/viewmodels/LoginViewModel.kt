package com.yap.yapandroid.modules.login.viewmodels

import android.app.Application
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.yap.yapandroid.modules.login.interfaces.ILogin
import com.yap.yapandroid.modules.login.states.LoginState
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.ApiError
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleLiveEvent
import co.yap.yapcore.helpers.Utils
import com.yap.yapandroid.main.MainChildViewModel

class LoginViewModel(application: Application) :
    MainChildViewModel<ILogin.State>(application),
    ILogin.ViewModel,
    IRepositoryHolder<AuthRepository> {

    override val signInButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val signUpButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val state: LoginState = LoginState(application = application)
    override val repository: AuthRepository = AuthRepository
    private val customersRepository: CustomersRepository = CustomersRepository
    override var isAccountBlocked: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun handlePressOnLogin() {
        state.twoWayTextWatcher = Utils.verifyUsername(state.twoWayTextWatcher.trim())
        validateUsername()
    }

    override fun handlePressOnSignUp() {
   signUpButtonPressEvent.value = true

    }

    override fun onEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handlePressOnLogin()
            }
            false
        }
    }

    private fun validateUsername() {
        launch {
            state.loading = true
            when (val response = customersRepository.verifyUsername(state.twoWayTextWatcher)) {
                is RetroApiResponse.Success -> {
                    if (response.data.data) {
                        parentViewModel?.signingInData?.clientId = state.twoWayTextWatcher
                        signInButtonPressEvent.postValue(true)
                    } else {
                        state.emailError.value =
                            getString(Strings.screen_sign_in_display_text_error_text)
                    }
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    handleBlockedAccountError(response.error)
                    state.loading = false
                }
            }
        }
    }

    private fun handleBlockedAccountError(error: ApiError) {
        when (error.actualCode) {
            "AD-10018" -> {
                isAccountBlocked.value = true
            }
            else -> {
                state.error = error.message
                state.emailError.value = error.message
            }

        }
    }
}