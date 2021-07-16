package co.yap.sendmoney.addbeneficiary.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.countryutils.country.Country
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.IAddBeneficiary
import co.yap.sendmoney.addbeneficiary.states.AddBeneficiaryStates
import co.yap.sendmoney.viewmodels.SendMoneyBaseViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.OTPActions
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.managers.SessionManager

class AddBeneficiaryViewModel(application: Application) :
    SendMoneyBaseViewModel<IAddBeneficiary.State>(application), IAddBeneficiary.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val repository: CustomersRepository = CustomersRepository
    override val state: AddBeneficiaryStates = AddBeneficiaryStates(this)
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var addBeneficiarySuccess: MutableLiveData<Boolean> = MutableLiveData()
    override val otpCreateObserver: MutableLiveData<Boolean> = MutableLiveData()
    override var beneficiary: Beneficiary? = null

    override fun onCreate() {
        super.onCreate()
        state.selectedBeneficiaryType = parentViewModel?.beneficiary?.value?.beneficiaryType
        parentViewModel?.selectedCountry?.value?.let {
            state.country = it.getName()
            state.country2DigitIsoCode = it.isoCountryCode2Digit ?: "AE"
            state.flagDrawableResId =
                Country(isoCountryCode2Digit = it.isoCountryCode2Digit).getFlagDrawableResId(context)

            parentViewModel?.beneficiary?.value?.beneficiaryType?.let { beneficiaryType ->
                if (beneficiaryType.isEmpty()) return@let
                when (SendMoneyBeneficiaryType.valueOf(beneficiaryType)) {
                    SendMoneyBeneficiaryType.CASHPAYOUT -> {
                        state.transferType = "Cash Pickup"
                    }
                    else -> {
                        state.transferType = "Bank Transfer"
                    }
                }
            }
            it.isoCountryCode2Digit?.let { str ->
                state.countryCode = Utils.getCountryCodeFormString(str)
            }
            state.currency = it.getCurrency()?.code ?: ""
        }
    }
    override fun handlePressOnAddDomestic(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnAddNow(id: Int) {
        if (id == R.id.confirmButton) {
            setBeneficiaryDetail()
            parentViewModel?.beneficiary?.value?.beneficiaryType?.let { beneficiaryType ->
                if (beneficiaryType.isEmpty()) return@let
                when (SendMoneyBeneficiaryType.valueOf(beneficiaryType)) {
                    SendMoneyBeneficiaryType.CASHPAYOUT -> {
                        parentViewModel?.beneficiary?.value?.let {
                            validateBeneficiaryDetails(
                                objBeneficiary = it,
                                otpType = OTPActions.CASHPAYOUT_BENEFICIARY.name
                            )
                        }
                    }
                    SendMoneyBeneficiaryType.DOMESTIC -> {
                        parentViewModel?.beneficiary?.value?.let {
                            if (!isLoggedinUserIBAN(it))
                                validateBeneficiaryDetails(
                                    objBeneficiary = it,
                                    otpType = OTPActions.DOMESTIC_BENEFICIARY.name
                                )
                            else {
                                state.toast = "${getString(Strings.screen_add_beneficiary_detail_display_text_error_iban_current_user)}^${AlertType.DIALOG.name}"

                            }
                        }
                    }
                    else -> {
                        clickEvent.setValue(id)
                    }
                }
            }
        } else {
            clickEvent.setValue(id)
        }
    }

    private fun isLoggedinUserIBAN(beneficiary: Beneficiary): Boolean {
        SessionManager.user?.iban?.let {
            return beneficiary.accountNo.equals(it, true)
        } ?: return false

    }

    override fun createOtp(action: String) {
        otpCreateObserver.value = true
    }

    override fun validateBeneficiaryDetails(objBeneficiary: Beneficiary, otpType: String) {
        launch {
            state.loading = true
            when (val response = repository.validateBeneficiary(objBeneficiary)) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    createOtp(otpType)
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                }
            }
        }
    }

    private fun setBeneficiaryDetail() {
        parentViewModel?.beneficiary?.value?.title = state.nickName
        parentViewModel?.beneficiary?.value?.firstName = state.firstName
        parentViewModel?.beneficiary?.value?.lastName = state.lastName
        parentViewModel?.beneficiary?.value?.mobileNo = state.mobileNo
        parentViewModel?.beneficiary?.value?.accountNo = state.iban.replace(" ", "")
        parentViewModel?.selectedCountry?.value?.let {
            parentViewModel?.beneficiary?.value?.currency = it.getCurrency()?.code
            parentViewModel?.beneficiary?.value?.country = it.isoCountryCode2Digit
        }
        parentViewModel?.beneficiary?.value?.countryOfResidence =
            parentViewModel?.selectedResidenceCountry?.isoCountryCode2Digit
        parentViewModel?.beneficiary?.value?.countryOfResidenceName =
            parentViewModel?.selectedResidenceCountry?.getName()
    }

    override fun addCashPickupBeneficiary() {
        parentViewModel?.beneficiary?.value?.also {
            it.accountNo = null
            launch {
                state.loading = true
                when (val response = repository.addBeneficiary(it)) {
                    is RetroApiResponse.Success -> {
                        state.loading = false
                        beneficiary = response.data.data
                        addBeneficiarySuccess.value = true
                    }

                    is RetroApiResponse.Error -> {
                        state.loading = false
                        state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    }
                }
            }
        }
    }

    override fun addDomesticBeneficiary(objBeneficiary: Beneficiary?) {
        objBeneficiary?.let {
            launch {
                state.loading = true
                when (val response = repository.addBeneficiary(it)) {
                    is RetroApiResponse.Success -> {
                        state.loading = false
                        beneficiary = response.data.data
                        addBeneficiarySuccess.value = true
                    }

                    is RetroApiResponse.Error -> {
                        state.loading = false
                        state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_add_beneficiary_display_text_title))
        parentViewModel?.state?.toolbarVisibility?.set(true)
        parentViewModel?.state?.leftIconVisibility?.set(true)
    }
}