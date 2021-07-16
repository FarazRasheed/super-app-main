package co.yap.modules.dashboard.more.profile.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.more.main.viewmodels.MoreBaseViewModel
import co.yap.modules.dashboard.more.profile.intefaces.IChangeEmail
import co.yap.modules.dashboard.more.profile.states.ChangeEmailState
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.SharedPreferenceManager


open class ChangeEmailViewModel(application: Application) :
    MoreBaseViewModel<IChangeEmail.State>(application), IChangeEmail.ViewModel,
    IRepositoryHolder<CustomersRepository> {
    override val changeEmailSuccessEvent: SingleClickEvent = SingleClickEvent()

    override val repository: CustomersRepository = CustomersRepository
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val success: MutableLiveData<Boolean> = MutableLiveData()
    override val sharedPreferenceManager = SharedPreferenceManager(context)
    override val state: ChangeEmailState = ChangeEmailState(application)


    override fun onHandlePressOnNextButton() {
        if (state.newEmailValidation() && state.confirmEmailValidation()) {
            if (state.newEmail == state.newConfirmEMail) {
                launch {
                    state.loading = true
                    when (val response =
                        repository.validateEmail(state.newEmail)) {
                        is RetroApiResponse.Success -> {
                            state.loading = false
                            success.value = true
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

    override fun changeEmail() {
        launch {
            state.loading = true
            when (val response =
                repository.changeVerifiedEmail(state.newEmail)) {
                is RetroApiResponse.Success -> {
                    changeEmailSuccessEvent.call()
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.setErrors(response.error.message)
                }
            }
            state.loading = false
        }
    }


    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_change_email_display_text_heading))
    }
}