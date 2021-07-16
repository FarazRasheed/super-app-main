package co.yap.sendmoney.addbeneficiary.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.IBeneficiaryAccountDetails
import co.yap.sendmoney.addbeneficiary.states.BeneficiaryAccountDetailsState
import co.yap.sendmoney.viewmodels.SendMoneyBaseViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.SingleLiveEvent
import co.yap.yapcore.enums.SendMoneyBeneficiaryType

class BeneficiaryAccountDetailsViewModel(application: Application) :
    SendMoneyBaseViewModel<IBeneficiaryAccountDetails.State>(application),
    IBeneficiaryAccountDetails.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override val backButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override val success: MutableLiveData<Boolean> = MutableLiveData()
    override val isBeneficiaryValid: MutableLiveData<Boolean> = MutableLiveData()
    override val state: BeneficiaryAccountDetailsState = BeneficiaryAccountDetailsState(this)
    override val repository: CustomersRepository = CustomersRepository
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var beneficiary: Beneficiary? = Beneficiary()
    override val otpCreateObserver: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        state.isIbanMandatory.set(parentViewModel?.selectedCountry?.value?.ibanMandatory)
        parentViewModel?.beneficiary?.value?.beneficiaryType?.let { beneficiaryType ->
            if (beneficiaryType.isNotEmpty())
                when (SendMoneyBeneficiaryType.valueOf(beneficiaryType)) {
                    SendMoneyBeneficiaryType.SWIFT -> {
                        state.showlyIban.set(true)
                    }
                    SendMoneyBeneficiaryType.RMT -> {
                        state.showlyIban.set(true)
                    }
                    else -> {

                    }
                }
        }
        parentViewModel?.beneficiary?.value?.let {
            state.bankName = it.bankName ?: ""
            if (it.identifierCode1.isNullOrEmpty()) {
                if (it.identifierCode2.isNullOrEmpty()) {
                    state.idCode = ""
                } else {
                    state.idCode = "ID Code: " + it.identifierCode2
                }
            } else {
                state.idCode = "ID Code: " + it.identifierCode1
            }

            state.bankAddress = it.branchName ?: "" + it.branchAddress ?: ""
            state.bankPhoneNumber = ""
            it.countryOfResidence = parentViewModel?.selectedResidenceCountry?.isoCountryCode2Digit
            it.countryOfResidenceName = parentViewModel?.selectedResidenceCountry?.getName()
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_add_beneficiary_display_text_title))
        parentViewModel?.state?.toolbarVisibility?.set(true)
        parentViewModel?.state?.leftIconVisibility?.set(true)
    }

    override fun handlePressOnAddBank(id: Int) {
        if (id == R.id.confirmButton) {
            parentViewModel?.beneficiary?.value?.also { it ->
                it.accountNo = state.accountIban.replace(" ", "")
                clickEvent.setValue(id)
            }
        } else
            clickEvent.setValue(id)
    }

    override fun retry() {
    }
}