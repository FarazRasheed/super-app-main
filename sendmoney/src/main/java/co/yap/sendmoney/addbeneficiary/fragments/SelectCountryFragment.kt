package co.yap.sendmoney.addbeneficiary.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.sendmoney.adapters.CountryAdapter
import co.yap.sendmoney.addbeneficiary.interfaces.ISelectCountry
import co.yap.sendmoney.addbeneficiary.viewmodels.SelectCountryViewModel
import co.yap.sendmoney.fragments.SendMoneyBaseFragment
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import kotlinx.android.synthetic.main.fragment_select_country.*


class SelectCountryFragment : SendMoneyBaseFragment<ISelectCountry.ViewModel>(),
    ISelectCountry.View {

    private var countryAdapter: CountryAdapter? = null

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_select_country

    override val viewModel: ISelectCountry.ViewModel
        get() = ViewModelProviders.of(this).get(SelectCountryViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.populateSpinnerData.observe(this, Observer {
            countriesSpinner.adapter = getCountryAdapter()
            countriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onCountrySelected(position)
                }
            }
        })
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.nextButton -> {
                    if (!isDefaultCurrencyExist()) {
                        viewModel.state.selectedCountry?.getCurrency()?.let { it ->
                            if (viewModel.state.selectedCountry?.isoCountryCode2Digit == "AE") {
                                findNavController().navigate(R.id.action_selectCountryFragment_to_DomesticFragment)
                            } else {
                                it.cashPickUp?.let { cashPickup ->
                                    if (cashPickup) {
                                        moveToTransferType()
                                    } else {
                                        moveToAddBeneficiary()
                                    }
                                }
                            }
                        }
                    } else {
                        showToast("No active currencies found for selected country")
                    }
                }
            }
        })
    }

    private fun isDefaultCurrencyExist(): Boolean {
        return null == viewModel.state.selectedCountry?.getCurrencySM()
    }

    private fun moveToAddBeneficiary() {
        findNavController().navigate(R.id.action_selectCountryFragment_to_addBeneficiaryFragment)
    }

    private fun moveToTransferType() {
        findNavController().navigate(R.id.action_selectCountryFragment_to_transferTypeFragment)
    }

    private fun getCountryAdapter(): CountryAdapter? {
        if (countryAdapter == null)
            countryAdapter =
                context?.let { CountryAdapter(it, viewModel.countries) }
        return countryAdapter
    }
}