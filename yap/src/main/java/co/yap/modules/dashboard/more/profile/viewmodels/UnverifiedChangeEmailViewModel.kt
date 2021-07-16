package co.yap.modules.dashboard.more.profile.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.managers.SessionManager

class UnverifiedChangeEmailViewModel(application: Application) : ChangeEmailViewModel(application) {
    override val success: MutableLiveData<Boolean> = MutableLiveData()
    override val repository: CustomersRepository = CustomersRepository
    override val sharedPreferenceManager = SharedPreferenceManager(context)

    override fun onHandlePressOnNextButton() {
        if (state.newEmailValidation() && state.confirmEmailValidation()) {
            if (state.newEmail == state.newConfirmEMail) {
                launch {
                    state.loading = true
                    when (val response =
                        repository.validateEmail(state.newEmail)) {
                        is RetroApiResponse.Success -> {
                            changeUnverifiedEmailRequest()
                        }

                        is RetroApiResponse.Error -> {
                            state.loading = false
                            state.setErrors(response.error.message)

                        }
                    }
                }
            } else {
                state.setErrors("Email is not matched.")
            }
        }
    }

    private fun changeUnverifiedEmailRequest() {
        launch {
            when (val response =
                repository.changeUnverifiedEmail(state.newEmail)) {
                is RetroApiResponse.Success -> {
                    SessionManager.user?.currentCustomer?.email = state.newEmail
                    sharedPreferenceManager.saveUserNameWithEncryption(state.newEmail)
                    success.value = true
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.setErrors(response.error.message)

                }

            }
            state.loading = false
        }
    }
}