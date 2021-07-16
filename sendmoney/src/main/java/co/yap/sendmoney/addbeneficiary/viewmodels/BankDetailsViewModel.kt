package co.yap.sendmoney.addbeneficiary.viewmodels

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.OtherBankQuery
import co.yap.networking.customers.responsedtos.beneficiary.BankParams
import co.yap.networking.customers.responsedtos.sendmoney.RAKBank.Bank
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.adaptor.AddBeneficiariesAdaptor
import co.yap.sendmoney.addbeneficiary.adaptor.RAKBankAdaptor
import co.yap.sendmoney.addbeneficiary.interfaces.IBankDetails
import co.yap.sendmoney.addbeneficiary.states.BankDetailsState
import co.yap.sendmoney.viewmodels.SendMoneyBaseViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import kotlinx.coroutines.delay

class BankDetailsViewModel(application: Application) :
    SendMoneyBaseViewModel<IBankDetails.State>(application), IBankDetails.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override var bankParams: ObservableField<MutableList<BankParams>> = ObservableField()
    override var bankList: MutableLiveData<MutableList<Bank>> = MutableLiveData()

    override val repository: CustomersRepository = CustomersRepository
    override val state: BankDetailsState = BankDetailsState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()

    private val watcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            launch {
                delay(300)
                val mandatoryFields =
                    paramsAdaptor.getDataList().filter { it.isMandatory == "Y" }
                var isValid = false
                for (field in mandatoryFields.iterator()) {
                    if (field.minCharacters?.toInt() != null &&
                        field.minCharacters?.toInt()!! > field.data?.length ?: 0
                    ) {
                        isValid = false
                        break
                    } else {
                        isValid = true
                    }
                }
                if (!isValid && mandatoryFields.isNullOrEmpty()) {
                    for (field in paramsAdaptor.getDataList().iterator()) {
                        if (field.minCharacters?.toInt() != null &&
                            field.minCharacters?.toIntOrNull()?:0 > field.data?.length ?: 0
                        ) {
                            isValid = false

                        } else {
                            isValid = true
                            break
                        }
//                        if (field.data?.length ?: 0 > 0) {
//                            isValid = true
//                            break
//                        } else {
//                            isValid = false
//                        }

                    }
                }

                state.valid = isValid
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }
    }
    var adaptorBanks = RAKBankAdaptor(mutableListOf())
    var paramsAdaptor = AddBeneficiariesAdaptor(mutableListOf(), watcher)

    override fun onCreate() {
        super.onCreate()
        state.selectedBeneficiaryType = parentViewModel?.beneficiary?.value?.beneficiaryType
        parentViewModel?.beneficiary?.value?.beneficiaryType?.let {
            if (it.isNotEmpty())
                when (SendMoneyBeneficiaryType.valueOf(it)) {
                    SendMoneyBeneficiaryType.RMT -> {
                        state.isRmt.set(true)
                        state.buttonText = getString(Strings.screen_bank_details_button_find_bank)
                        state.hideSwiftSection = false
                        if (paramsAdaptor.getDataList().isNullOrEmpty())
                            parentViewModel?.selectedCountry?.value?.isoCountryCode2Digit?.let { code ->
                                getOtherBankParams(code)
                            }
                    }
                    SendMoneyBeneficiaryType.SWIFT -> {
                        state.isRmt.set(false)
//                        state.buttonText = getString(Strings.screen_bank_details_button_confirm)
                        state.buttonText = getString(Strings.screen_add_beneficiary_button_next)
                        state.hideSwiftSection = true
//                        state.valid = true  // don't remember why we set valid = true
                    }
                    else -> {
                        state.isRmt.set(false)
                    }
                }
        }
    }

    override fun handlePressOnView(id: Int) {
        if (id == R.id.confirmButton) {
            parentViewModel?.beneficiary?.value?.bankName = state.bankName
            parentViewModel?.beneficiary?.value?.branchName = state.bankBranch
            parentViewModel?.beneficiary?.value?.bankCity = state.bankCity
            parentViewModel?.beneficiary?.value?.swiftCode = state.swiftCode

            parentViewModel?.beneficiary?.value?.beneficiaryType?.let {
                if (it.isNotEmpty())
                    when (SendMoneyBeneficiaryType.valueOf(it)) {
                        SendMoneyBeneficiaryType.RMT -> {
                            clickEvent.setValue(id)
                        }
                        SendMoneyBeneficiaryType.SWIFT -> {
                            clickEvent.setValue(id)
                            //Swift changes
                        }
                        else -> {

                        }
                    }
            }
        } else {
            clickEvent.setValue(id)
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_add_beneficiary_display_text_title))
        parentViewModel?.state?.toolbarVisibility?.set(true)
        parentViewModel?.state?.leftIconVisibility?.set(true)
    }

    override fun searchRMTBanks(otherBankQuery: OtherBankQuery) {
        parentViewModel?.beneficiary?.value?.let {
            launch {
                state.loading = true
                when (val response = repository.findOtherBank(otherBankQuery)) {
                    is RetroApiResponse.Success -> {
                        state.loading = false
                        response.data.data?.banks?.let { it1 -> adaptorBanks.setList(it1) }
                        bankList.value = response.data.data?.banks
                    }

                    is RetroApiResponse.Error -> {
                        state.loading = false
                        state.toast = response.error.message
                        bankList.value = ArrayList()
                    }
                }
            }
        }
    }

    private fun getOtherBankParams(countryCode: String) {
        launch {
            state.loading = true
            when (val response = repository.getOtherBankParams(countryCode)) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    state.isRmt.set(true)
                    response.data.data?.params?.let { it1 -> paramsAdaptor.setList(it1) }
                    bankParams.set(response.data.data?.params as MutableList<BankParams>?)
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message
                }
            }
        }
    }

    override fun retry() {
    }

}