package co.yap.modules.location.tax

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import co.yap.countryutils.country.Country
import co.yap.modules.location.viewmodels.LocationChildViewModel
import co.yap.networking.customers.CustomersRepository
import co.yap.networking.customers.requestdtos.TaxInfoDetailRequest
import co.yap.networking.customers.requestdtos.TaxInfoRequest
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.R
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener

class TaxInfoViewModel(application: Application) :
    LocationChildViewModel<ITaxInfo.State>(application),
    ITaxInfo.ViewModel, IRepositoryHolder<CustomersRepository> {
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: ITaxInfo.State = TaxInfoState()
    override var taxInfoList: MutableList<TaxModel> = mutableListOf()
    //override var countries: ArrayList<Country>? = null
    override var taxInfoAdaptor: TaxInfoAdaptor = TaxInfoAdaptor(taxInfoList)
    override val repository: CustomersRepository = CustomersRepository
    override var reasonsList: ArrayList<String> = arrayListOf()
    override var options = arrayListOf("No", "Yes")
    private var rowTitles = arrayListOf(
        "Country of tax residence",
        "Select a second country of tax residence",
        "Select a third country of tax residence"
    )

    override
    fun onCreate() {
        super.onCreate()
        getReasonsList()
        setupRecycleView()
    }

    private fun setupRecycleView() {
        taxInfoAdaptor.setItemListener(listener)
    }

    override fun handleOnPressView(id: Int) {
        if (id == R.id.cbTermsAndConditions) {
            state.isAgreed.set(!(state.isAgreed.get() as Boolean))
            state.valid.set(isTaxInfoValid(taxInfoList))
        } else
            clickEvent.setValue(id)
    }

    override fun getReasonsList() {
        launch {
            state.loading = true
            when (val response = repository.getTaxReasons()) {
                is RetroApiResponse.Success -> {
                    reasonsList = response.data.reasons
                    if (!parentViewModel?.countries.isNullOrEmpty()) {
                        createModel(reasonsList, options, ObservableField(rowTitles[0]))
                        state.onSuccess.set(true)
                        state.loading = false

                    } else
                        getAllCountries {
                            parentViewModel?.countries = it
                            createModel(reasonsList, options, ObservableField(rowTitles[0]))
                            state.onSuccess.set(true)
                            state.loading = false
                        }
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.onSuccess.set(false)
                    state.toast = response.error.message
                }
            }
        }
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (view.id) {
                R.id.ivCross -> {
                    val index = taxInfoList.indexOf(data as TaxModel)
                    taxInfoList.removeAt(index)
                    taxInfoAdaptor.notifyItemRemoved(index)
                    taxInfoList.last().canAddMore.set(true)
                    taxInfoList.last().taxRowTitle.set(rowTitles[taxInfoList.size - 1])
                    state.valid.set(isTaxInfoValid(taxInfoList))
                }
                R.id.lyAddCountry -> {
                    createModel(
                        reasonsList,
                        options,
                        ObservableField(rowTitles[taxInfoList.size])
                    )
                    state.valid.set(isTaxInfoValid(taxInfoList))
                }
                R.id.spinner_container -> { // on country selected login
                    state.valid.set(isTaxInfoValid(taxInfoList))
                }
                R.id.etTinNumber -> { // on tin number change
                    state.valid.set(isTaxInfoValid(taxInfoList))
                }
                R.id.optionsSpinner -> { // on options selected
                    state.valid.set(isTaxInfoValid(taxInfoList))
                }
                R.id.reasonsSpinner -> { // on reason selected
                    state.valid.set(isTaxInfoValid(taxInfoList))
                }
            }
        }
    }

    override fun createModel(
        reasons: ArrayList<String>,
        options: ArrayList<String>, title: ObservableField<String>
    ) {
        taxInfoList.add(
            TaxModel(
                countries = parentViewModel?.countries ?: arrayListOf(),
                reasons = reasons,
                options = options,
                canAddMore = ObservableField(taxInfoList.size in 0..1),
                taxRowNumber = ObservableField(taxInfoList.isNotEmpty()),
                taxRowTitle = title
            )
        )
        taxInfoAdaptor.notifyItemInserted(taxInfoList.size)
    }

    private fun isTaxInfoValid(taxInfoList: MutableList<TaxModel>): Boolean {
        var valid = false
        for (taxInfo: TaxModel in taxInfoList) {
            valid = if (taxInfo.selectedCountry?.getName().equals("Select country")) {
                false
            } else {
                if (taxInfo.selectedOption.get().equals("No")) {
                    !taxInfo.selectedReason.isBlank()
                } else {
                    !taxInfo.tinNumber.get().isNullOrBlank()
                }
            }
            if (!valid) break
        }
        return valid && state.isAgreed.get() == true
    }

    override fun saveInfoDetails(isSubmit: Boolean, success: (pdfUrl: String?) -> Unit) {
        launch {
            state.loading = true
            when (val response = repository.saveTaxInfo(
                TaxInfoRequest(
                    usNationalForTax = !(state.isAgreed.get() ?: false),
                    submit = isSubmit,
                    taxInfoDetails = getTaxDetails(taxInfoList)
                )
            )) {
                is RetroApiResponse.Success -> {
                    success.invoke(response.data.pdf)
                    state.loading = false
                }

                is RetroApiResponse.Error -> {
                    state.loading = false
                    state.toast = response.error.message
                }
            }
        }
    }

    private fun getTaxDetails(taxInfoList: MutableList<TaxModel>): ArrayList<TaxInfoDetailRequest> {
        val taxList: ArrayList<TaxInfoDetailRequest> = ArrayList()
        for (taxInfo: TaxModel in taxInfoList) {
            taxList.add(
                TaxInfoDetailRequest(
                    country = taxInfo.selectedCountry?.getName() ?: "",
                    tinAvailable = taxInfo.selectedOption.get().equals("Yes"),
                    reasonInCaseNoTin = if (taxInfo.selectedOption.get().equals("Yes")) "" else taxInfo.selectedReason,
                    tinNumber = if (taxInfo.selectedOption.get().equals("Yes")) taxInfo.tinNumber.get()
                        ?: "" else ""
                )
            )
        }
        return taxList
    }

    override fun getAllCountries(success: (ArrayList<Country>) -> Unit) {
        if (!parentViewModel?.countries.isNullOrEmpty()) {
            success(parentViewModel?.countries ?: arrayListOf())
        } else {
            launch {
                when (val response = repository.getAllCountries()) {
                    is RetroApiResponse.Success -> {
                        success(Utils.parseCountryList(response.data.data) as ArrayList<Country>)

                    }

                    is RetroApiResponse.Error -> {
                        state.toast = response.error.message
                    }
                }
            }
        }
    }
}