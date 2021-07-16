package co.yap.modules.dashboard.store.household.onboarding.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IHouseHoldUserContact
import co.yap.modules.dashboard.store.household.onboarding.states.HouseHoldUserContactState
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.VerifyHouseholdMobileRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent

class HouseHoldUserContactViewModel(application: Application) :
    BaseOnboardingViewModel<IHouseHoldUserContact.State>(application),
    IHouseHoldUserContact.ViewModel,
    IRepositoryHolder<CustomersRepository> {
    override val repository: CustomersRepository = CustomersRepository
    override val state: HouseHoldUserContactState = HouseHoldUserContactState(application)
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override var verifyMobileSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    override var verifyMobileError: MutableLiveData<String> = MutableLiveData()

    override fun handlePressOnAdd(id: Int) {
        parentViewModel?.countryCode = state.countryCode
        parentViewModel?.userMobileNo = state.etMobileNumberConfirmMobile?.text.toString()
        clickEvent.setValue(id)
    }

    override fun handlePressOnBackButton() {

    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_yap_house_hold_user_info_display_text_title))
    }

    override fun getCcp(editText: EditText) {
        editText.requestFocus()
        state.etMobileNumber = editText
        state.etMobileNumber!!.requestFocus()
    }

    override fun getConfirmCcp(editText: EditText) {
        state.etMobileNumberConfirmMobile = editText
    }

    override fun verifyMobileNumber() {
        launch {
            state.loading = true
            val request = VerifyHouseholdMobileRequest(
                countryCode = "00${parentViewModel?.countryCode}",
                mobileNo = parentViewModel?.userMobileNo?.replace(" ", "") ?: ""
            )
            when (val response = repository.verifyHouseholdMobile(request)) {
                is RetroApiResponse.Success -> {
                    verifyMobileSuccess.value = true
                }
                is RetroApiResponse.Error -> {
                   verifyMobileError.value = response.error.message
//                    state.toast = response.error.message
                }
            }
            state.loading = false
        }
    }

}
