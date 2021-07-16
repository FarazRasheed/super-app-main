package co.yap.modules.dashboard.more.profile.viewmodels

import android.app.Application
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.more.main.viewmodels.MoreBaseViewModel
import co.yap.modules.dashboard.more.profile.intefaces.IChangePhoneNumber
import co.yap.modules.dashboard.more.profile.states.ChangePhoneNumberState
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class ChangePhoneNumberViewModel(application: Application) :
    MoreBaseViewModel<IChangePhoneNumber.State>(application), IChangePhoneNumber.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val changePhoneNumberSuccessEvent: SingleClickEvent= SingleClickEvent()
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val isPhoneNumberValid: MutableLiveData<Boolean> = MutableLiveData(false)
    override val state: ChangePhoneNumberState = ChangePhoneNumberState(application)
    override val repository: CustomersRepository = CustomersRepository

    override fun getCcp(etMobileNumber: EditText) {
        //  etMobileNumber.requestFocus()
        state.etMobileNumber = etMobileNumber
        state.etMobileNumber?.requestFocus()
        state.etMobileNumber?.setOnEditorActionListener(onEditorActionListener())
    }

    private fun validateMobileNumber(view: View) {
        launch {
            state.loading = true
            when (val response =
                repository.validatePhoneNumber("00971", state.mobile.replace(" ", ""))) {
                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.errorMessage = response.error.message
                }

                is RetroApiResponse.Success -> {
                    state.loading = false
                    isPhoneNumberValid.value = true
                }
            }
        }
    }


    override fun onEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                /*if (state.valid){
                            handlePressOnNext()
                        }*/
            }
            false
        }
    }

    override fun changePhoneNumber() {
        launch {
            state.loading = true
            when (val response =
                repository.changeMobileNumber(countryCode = "00${state.countryCode}", mobileNumber = state.mobile.replace(" ", ""))) {
                is RetroApiResponse.Success -> {
                    changePhoneNumberSuccessEvent.call()
                }
                is RetroApiResponse.Error -> {
                    state.toast = response.error.message
                    state.loading = false
                }
            }
            state.loading = false
        }
    }



    override fun onHandlePressOnNextButton(view: View) {
        validateMobileNumber(view)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_change_phone_number_display_text_heading))
    }
}