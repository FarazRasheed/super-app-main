package co.yap.household.onboard.onboarding.viewmodels

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import co.yap.household.onboard.onboarding.interfaces.IHouseHoldNumberRegistration
import co.yap.household.onboard.onboarding.states.HouseHoldNumberRegistrationState
import co.yap.household.onboard.onboarding.main.viewmodels.OnboardingChildViewModel
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.VerifyHouseholdMobileRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.getCountryCodeForRegionWithZeroPrefix

class HouseHoldNumberRegistrationViewModel(application: Application) :
    OnboardingChildViewModel<IHouseHoldNumberRegistration.State>(application),
    IHouseHoldNumberRegistration.ViewModel, IRepositoryHolder<CustomersRepository> {
    override val repository: CustomersRepository = CustomersRepository
    override val state: HouseHoldNumberRegistrationState = HouseHoldNumberRegistrationState()
    override var clickEvent: SingleClickEvent? = SingleClickEvent()
    override var isParentMobileValid: MutableLiveData<Boolean>?= MutableLiveData(false)

    override fun onCreate() {
        populateState()
        super.onCreate()
        state.userName = parentViewModel?.state?.accountInfo?.currentCustomer?.firstName
        state.parentName = parentViewModel?.state?.accountInfo?.parentAccount?.currentCustomer?.firstName
        state.existingYapUser = parentViewModel?.state?.existingYapUser

    }

    override fun onResume() {
        super.onResume()
        updateBackground(Color.WHITE)
        setProgress(20)
    }

    override fun verifyHouseholdParentMobile() {
        launch {
            state.loading = true
            val request = VerifyHouseholdMobileRequest(
                countryCode = getCountryCodeForRegionWithZeroPrefix(state.countryCode),
                mobileNo = state.phoneNumber
            )
            when (val response = repository.verifyHouseholdParentMobile(state.phoneNumber, request )) {
                is RetroApiResponse.Success -> {
                    isParentMobileValid?.postValue(true)
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.loading = false
                    isParentMobileValid?.postValue(false)
                    state.toast = response.error.message

                }
            }
        }
    }

    override fun populateState() {
//        state.parentName = "Sufyan"
//        state.welcomeHeading =
//            getString(Strings.screen_house_hold_number_registration_display_text_heading).format(
//                state.parentName
//            )
//        state.numberConfirmationValue =
//            getString(Strings.screen_house_hold_number_registration_display_text_parent_description)
    }

    override fun handlePressOnConfirm(id: Int) {
        clickEvent?.setValue(id)
    }
}