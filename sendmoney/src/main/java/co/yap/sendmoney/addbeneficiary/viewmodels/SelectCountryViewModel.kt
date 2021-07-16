package co.yap.sendmoney.addbeneficiary.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.countryutils.country.Country
import co.yap.countryutils.country.utils.Currency
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.ISelectCountry
import co.yap.sendmoney.addbeneficiary.states.SelectCountryState
import co.yap.sendmoney.viewmodels.SendMoneyBaseViewModel
import co.yap.translation.Strings
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.helpers.Utils

class SelectCountryViewModel(application: Application) :
    SendMoneyBaseViewModel<ISelectCountry.State>(application), ISelectCountry.ViewModel,
    IRepositoryHolder<CustomersRepository> {

    override var populateSpinnerData: MutableLiveData<List<Country>> = MutableLiveData()
    override var countries: ArrayList<Country> = ArrayList()
    override val repository: CustomersRepository = CustomersRepository
    override val state: SelectCountryState = SelectCountryState(application)
    override var clickEvent: SingleClickEvent = SingleClickEvent()

    override fun onTransparentViewClick(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnSeclectCountry(id: Int) {
        if (id == R.id.nextButton) {
            parentViewModel?.selectedCountry?.value = state.selectedCountry
            parentViewModel?.selectedCountry?.value?.let { country ->
                if (country.isoCountryCode2Digit == "AE") {
                    parentViewModel?.beneficiary?.value?.beneficiaryType =
                        SendMoneyBeneficiaryType.DOMESTIC.name
                    clickEvent.setValue(id)
                } else {
                    if (country.getCurrency() != null) {
                        country.getCurrency()?.cashPickUp?.let { it ->
                            if (!it) {
                                country.getCurrency()?.rmtCountry?.let { isRmt ->
                                    if (isRmt) {
                                        parentViewModel?.beneficiary?.value?.beneficiaryType =
                                            SendMoneyBeneficiaryType.RMT.name
                                        clickEvent.setValue(id)
                                    } else {
                                        parentViewModel?.beneficiary?.value?.beneficiaryType =
                                            SendMoneyBeneficiaryType.SWIFT.name
                                        clickEvent.setValue(id)
                                    }
                                }
                            } else {
                                clickEvent.setValue(id)
                            }
                        }
                    } else {
                        state.toast = ("Invalid country found")
                    }
                }
            }
        } else {
            clickEvent.setValue(id)
        }
    }

    override fun onCreate() {
        super.onCreate()
        getAllCountries()
    }

    override fun onResume() {
        super.onResume()
        state.valid = false
        setToolBarTitle(getString(Strings.screen_add_beneficiary_display_text_title))
        parentViewModel?.state?.toolbarVisibility?.set(true)
        parentViewModel?.state?.leftIconVisibility?.set(true)

    }

    private fun getAllCountries() {

        if (!countries.isNullOrEmpty()) {
            populateSpinnerData.setValue(countries)
        } else {
            launch {
                state.loading = true
                when (val response = repository.getAllCountries()) {
                    is RetroApiResponse.Success -> {
                        val sortedList = response.data.data?.sortedWith(compareBy { it.name })
                        sortedList?.let { it ->
                            countries.clear()
                            countries.add(
                                0,
                                Country(name = getString(Strings.screen_add_beneficiary_display_text_select_country))
                            )
                            populateSpinnerData.value =  Utils.parseCountryList(it)
                            countries.addAll(it.map {
                                Country(
                                    id = it.id,
                                    isoCountryCode3Digit = it.isoCountryCode2Digit,
                                    isoCountryCode2Digit = it.isoCountryCode2Digit,
                                    supportedCurrencies = it.currencyList?.filter { curr -> curr.active == true }?.map { cur ->
                                        Currency(
                                            code = cur.code,
                                            default = cur.default,
                                            name = cur.name,
                                            active = cur.active,
                                            cashPickUp = cur.cashPickUp,
                                            rmtCountry = cur.rmtCountry
                                        )
                                    },
                                    active = it.active,
                                    isoNum = it.isoNum,
                                    signUpAllowed = it.signUpAllowed,
                                    name = it.name,
                                    currency = getDefaultCurrency(
                                        it.currencyList?.filter { curr -> curr.active == true },
                                        it.isoCountryCode2Digit
                                    ),
                                    ibanMandatory = it.ibanMandatory
                                )
                            })

                        }
                        parentViewModel?.countriesList = countries
                        state.loading = false
                    }

                    is RetroApiResponse.Error -> {
                        state.loading = false
                        state.toast = response.error.message

                    }
                }
            }
        }
    }

    private fun getDefaultCurrency(
        activeCurrencies: List<co.yap.networking.customers.responsedtos.sendmoney.Currency>?,
        isoCountryCode2Digit: String? = null
    ): Currency? {
        val defaultCurrency = activeCurrencies?.firstOrNull { it.default == true }
        return defaultCurrency?.let { item ->
            return Currency(
                code = item.code,
                default = item.default,
                name = item.name,
                active = item.active,
                cashPickUp = item.cashPickUp,
                rmtCountry = item.rmtCountry
            )
        } ?: getFirst(activeCurrencies)
    }

    private fun getFirst(activeCurrencies: List<co.yap.networking.customers.responsedtos.sendmoney.Currency>?): Currency? {
        return activeCurrencies?.firstOrNull { activeCurr -> activeCurr.active == true }
            ?.let { item ->
                return Currency(
                    code = item.code,
                    default = item.default,
                    name = item.name,
                    active = item.active,
                    cashPickUp = item.cashPickUp,
                    rmtCountry = item.rmtCountry
                )
            }
    }

    private fun getDefaultCurrencyIfNull(
        currencyList: List<co.yap.networking.customers.responsedtos.sendmoney.Currency>?,
        isoCountryCode2Digit: String?
    ): Currency? {
        var currency: Currency? = null
        currencyList?.let {
            for (item in it) {
                val curr2DigitCode = item.code?.substring(0..1)
                if (curr2DigitCode.equals(isoCountryCode2Digit, true)) {
                    currency = Currency(
                        code = item.code,
                        default = item.default,
                        name = item.name,
                        active = item.active,
                        cashPickUp = item.cashPickUp,
                        rmtCountry = item.rmtCountry
                    )
                    break
                }
            }
        }

        return currency
    }

    override fun onCountrySelected(pos: Int) {
        if (pos == 0) {
            state.selectedCountry = null
        } else {
            val country: Country = countries[pos]
            state.selectedCountry = country
        }
        parentViewModel?.selectedResidenceCountry = null
    }
}