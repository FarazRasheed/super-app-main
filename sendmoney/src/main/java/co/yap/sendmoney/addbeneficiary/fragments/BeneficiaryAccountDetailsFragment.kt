package co.yap.sendmoney.addbeneficiary.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.countryutils.country.Country
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.IBeneficiaryAccountDetails
import co.yap.sendmoney.addbeneficiary.viewmodels.BeneficiaryAccountDetailsViewModel
import co.yap.sendmoney.editbeneficiary.activity.EditBeneficiaryActivity
import co.yap.sendmoney.fragments.SendMoneyBaseFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.helpers.extentions.launchActivity

class BeneficiaryAccountDetailsFragment :
    SendMoneyBaseFragment<IBeneficiaryAccountDetails.ViewModel>(),
    IBeneficiaryAccountDetails.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_beneficiary_account_detail

    override val viewModel: BeneficiaryAccountDetailsViewModel
        get() = ViewModelProviders.of(this).get(BeneficiaryAccountDetailsViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addObservers()
    }

    private fun addObservers() {
        viewModel.clickEvent.observe(this, observer)
    }

    private fun openEditBeneficiary(beneficiary: Beneficiary?) {
        beneficiary?.let {
            val bundle = Bundle()
            bundle.putBoolean(Constants.OVERVIEW_BENEFICIARY, true)
            bundle.putString(Constants.IS_IBAN_NEEDED, "Yes")
            bundle.putString(
                Constants.IS_IBAN_NEEDED,
                if (viewModel.parentViewModel?.selectedCountry?.value?.ibanMandatory == true) "Yes" else "No"
            )
            bundle.putParcelable(Beneficiary::class.java.name, beneficiary)
            bundle.putParcelable(
                Country::class.java.name,
                viewModel.parentViewModel?.selectedResidenceCountry
            )

            launchActivity<EditBeneficiaryActivity>(RequestCodes.REQUEST_NOTIFY_BENEFICIARY_LIST) {
                putExtra(Constants.EXTRA, bundle)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodes.REQUEST_NOTIFY_BENEFICIARY_LIST -> {
                    val isMoneyTransfer =
                        data?.getValue(Constants.IS_TRANSFER_MONEY, "BOOLEAN") as? Boolean
                    val isTerminateProcess =
                        data?.getValue(Constants.TERMINATE_ADD_BENEFICIARY, "BOOLEAN") as? Boolean
                    val beneficiary =
                        data?.getValue(Beneficiary::class.java.name, "PARCEABLE") as? Beneficiary
                    when {
                        isTerminateProcess == true -> setIntentResult(cancelFlow = true)
                        isMoneyTransfer == true -> beneficiary?.let {
                            setIntentResult(true, beneficiary = it)
                        }
                        else -> setIntentResult()
                    }
                }
            }
        }
    }

    private fun setIntentResult(
        isMoneyTransfer: Boolean = false,
        cancelFlow: Boolean = false,
        beneficiary: Beneficiary = Beneficiary()
    ) {
        activity?.let { it ->
            val intent = Intent()
            intent.putExtra(Constants.BENEFICIARY_CHANGE, true)
            intent.putExtra(Constants.IS_TRANSFER_MONEY, isMoneyTransfer)
            intent.putExtra(Constants.TERMINATE_ADD_BENEFICIARY, cancelFlow)
            intent.putExtra(Beneficiary::class.java.name, beneficiary)
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    private val observer = Observer<Int> {
        when (it) {
            R.id.confirmButton -> openEditBeneficiary(viewModel.parentViewModel?.beneficiary?.value)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
        viewModel.otpCreateObserver.removeObservers(this)
    }
}