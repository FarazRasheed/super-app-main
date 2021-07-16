package co.yap.sendmoney.addbeneficiary.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.networking.customers.requestdtos.OtherBankQuery
import co.yap.networking.customers.responsedtos.sendmoney.RAKBank.Bank
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.IBankDetails
import co.yap.sendmoney.addbeneficiary.viewmodels.BankDetailsViewModel
import co.yap.sendmoney.fragments.SendMoneyBaseFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_add_bank_detail.*

class AddBankDetailsFragment : SendMoneyBaseFragment<IBankDetails.ViewModel>(),
    IBankDetails.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_add_bank_detail

    override val viewModel: BankDetailsViewModel
        get() = ViewModelProviders.of(this).get(BankDetailsViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListener()
    }

    private fun addListener() {
        viewModel.clickEvent.observe(this, observer)
        viewModel.bankList.observe(this, Observer {
            setupAdaptorBanks(it)
        })
        recycler.adapter = viewModel.paramsAdaptor
        recycler_banks.adapter = viewModel.adaptorBanks
    }

    private fun setIntentResult() {
        activity?.let { it ->
            val intent = Intent()
            intent.putExtra(Constants.BENEFICIARY_CHANGE, true)
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }


    private fun setupAdaptorBanks(list: MutableList<Bank>) {
        viewModel.state.txtCount.set(if (list.isEmpty()) "" else "Select your bank (${list.size} bank found)")
        viewModel.adaptorBanks.setItemListener(listener)
        recycler_banks.adapter = viewModel.adaptorBanks
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is Bank) {
                viewModel.parentViewModel?.beneficiary?.value?.also { beneficiary ->
                    beneficiary.bankName = data.other_bank_name
                    beneficiary.identifierCode1 = data.identifier_code1
                    beneficiary.identifierCode2 = data.identifier_code2
                    beneficiary.branchName = data.other_branch_name
                    beneficiary.branchAddress =
                        data.other_branch_addr1 + " " + data.other_branch_addr2
                    findNavController().navigate(R.id.action_addBankDetailsFragment_to_beneficiaryAccountDetailsFragment)
                }
            }
        }
    }

    val observer = Observer<Int> {
        when (it) {
            R.id.confirmButton -> {
                viewModel.parentViewModel?.beneficiary?.value?.beneficiaryType?.let { it ->
                    if (it.isNotEmpty())
                        when (SendMoneyBeneficiaryType.valueOf(it)) {
                            SendMoneyBeneficiaryType.RMT -> {
                                viewModel.searchRMTBanks(otherSearchParams())
                            }
                            SendMoneyBeneficiaryType.SWIFT -> {
                                findNavController().navigate(R.id.action_addBankDetailsFragment_to_beneficiaryAccountDetailsFragment)
                            }
                            else -> {

                            }
                        }
                }
            }
        }
    }

    private fun otherSearchParams(): OtherBankQuery {
        val query = OtherBankQuery()
        viewModel.parentViewModel?.selectedCountry?.let { country ->
            query.max_records = 200
            query.other_bank_country = country.value?.isoCountryCode2Digit
            for (field in viewModel.paramsAdaptor?.getDataList()!!.iterator()) {
                if (!field.data.isNullOrEmpty()) {
                    val bankParams = OtherBankQuery.Params()
                    bankParams.id = field.id
                    bankParams.value = field.data
                    query.params?.add(bankParams)
                }
            }
        }
        return query
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        viewModel.bankList.removeObservers(this)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        return super.onBackPressed()
    }
}