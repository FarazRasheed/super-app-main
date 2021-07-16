package co.yap.modules.dashboard.store.household.onboarding.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IHouseHoldConfirmPayment
import co.yap.modules.dashboard.store.household.onboarding.states.HouseHoldConfirmPaymentState
import co.yap.modules.onboarding.enums.AccountType
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.HouseholdOnboardRequest
import co.yap.networking.household.responsedtos.HouseHoldPlan
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager

class HouseHoldConfirmPaymentViewModel(application: Application) :
    BaseOnboardingViewModel<IHouseHoldConfirmPayment.State>(application),
    IHouseHoldConfirmPayment.ViewModel, IRepositoryHolder<CustomersRepository> {
    override val repository: CustomersRepository = CustomersRepository
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var onBoardUserSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    override var plansList: MutableLiveData<ArrayList<HouseHoldPlan>> = MutableLiveData(ArrayList())
    override val state: IHouseHoldConfirmPayment.State = HouseHoldConfirmPaymentState()

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle("Confirm payment")
        toggleToolBarVisibility(true)
        plansList.value = parentViewModel?.plansList
        setAvailableBalance()
        initSelectedPlan()
    }

    private fun initSelectedPlan() {
        state.selectedPlanFee.set(parentViewModel?.selectedPlanType?.amount)
        state.selectedCardPlan.set(parentViewModel?.selectedPlanType?.type + " | " + state.selectedPlanFee.get())
        parentViewModel?.selectedPlanType?.discount?.let {
            if (it != 0) {
                state.selectedPlanSaving.set("Your saving $it%!")
            }
        }
    }

    private fun setAvailableBalance() {
        val balanceString = getString(Strings.screen_topup_transfer_display_text_available_balance)
            .format(
                state.currencyType.get().toString(),
                SessionManager.cardBalance.value?.availableBalance.toString()
                    .toFormattedCurrency(showCurrency = false, currency = SessionManager.getDefaultCurrency())
            )
        state.availableBalance.set(
            Utils.getSppnableStringForAmount(
                context,
                balanceString,
                state.currencyType.get().toString(),
                Utils.getFormattedCurrencyWithoutComma(SessionManager.cardBalance.value?.availableBalance.toString())
            )
        )
    }

    override fun addHouseholdUser() {
        launch {
            state.loading = true
            when (val response = repository.onboardHousehold(getOnboardRequest())) {
                is RetroApiResponse.Success -> {
                    parentViewModel?.tempPasscode = response.data.data?.passcode ?: "0000"
                    onBoardUserSuccess.value = true
                }

                is RetroApiResponse.Error -> state.toast = response.error.message
            }
            state.loading = false
        }
    }

    private fun getOnboardRequest(): HouseholdOnboardRequest {
        return HouseholdOnboardRequest(
            firstName = parentViewModel?.firstName,
            lastName = parentViewModel?.lastName,
            countryCode = "00" + parentViewModel?.countryCode,
            mobileNo = parentViewModel?.userMobileNo?.replace(" ", ""),
            accountType = AccountType.B2C_HOUSEHOLD.name,
            feeFrequency = parentViewModel?.selectedPlanType?.type.toString().toUpperCase()
        )
    }
}