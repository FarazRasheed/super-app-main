package co.yap.sendmoney.addbeneficiary.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.countryutils.country.Country
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.IBeneficiaryOverview
import co.yap.sendmoney.addbeneficiary.viewmodels.BeneficiaryOverviewViewModel
import co.yap.sendmoney.databinding.FragmentBeneficiaryOverviewBinding
import co.yap.sendmoney.fragments.SendMoneyBaseFragment
import co.yap.translation.Translator
import co.yap.yapcore.helpers.Utils
import kotlinx.android.synthetic.main.fragment_beneficiary_overview.*

class BeneficiaryOverviewFragment : SendMoneyBaseFragment<IBeneficiaryOverview.ViewModel>(),
    IBeneficiaryOverview.View {

    var isFromAddBeneficiary: Boolean = false

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_beneficiary_overview

    override val viewModel: BeneficiaryOverviewViewModel
        get() = ViewModelProviders.of(this).get(BeneficiaryOverviewViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun showResidenceCountries() {
        getBinding().spinner.setEnabledSpinner(false)
        getBinding().spinner.setAdapter(viewModel.parentViewModel?.countriesList)
        if (viewModel.parentViewModel?.selectedResidenceCountry != null) {
            getBinding().spinner.setSelectedItem(
                viewModel.parentViewModel?.countriesList?.indexOf(
                    viewModel.parentViewModel?.selectedResidenceCountry ?: Country()
                ) ?: 0
            )
        }
    }

    private fun editBeneficiaryScreen() {
        etnickName.isEnabled = true
        etFirstName.isEnabled = true
        etLastName.isEnabled = true
        etAccountIbanNumber.isEnabled = true
        etnickName.isEnabled = true
        etSwiftCode.isEnabled = true
        etBankREquiredFieldCode.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showResidenceCountries()
        viewModel.beneficiary =
            arguments?.let { BeneficiaryOverviewFragmentArgs.fromBundle(it).beneficiary }!!
        if (viewModel.beneficiary.accountNo!!.length >= 22) {
            viewModel.beneficiary.accountNo =
                Utils.formateIbanString(viewModel.beneficiary.accountNo!!)
        } else {
            viewModel.beneficiary.accountNo = viewModel.beneficiary.accountNo!!
        }
        viewModel.state.beneficiary = viewModel.beneficiary

        if (viewModel.beneficiary.id != null) {
            isFromAddBeneficiary = false
            viewModel.state.beneficiary = viewModel.beneficiary
        }

        if (!isFromAddBeneficiary) {
            editBeneficiaryScreen()
        }
//        getActivity()!!.getFragmentManager().popBackStack()

    }


    override fun onPause() {
        super.onPause()
        viewModel.clickEvent.removeObservers(this)
        viewModel.onDeleteSuccess.removeObservers(this)
    }

    override fun onResume() {
        super.onResume()
        observeEvents()
    }

    private fun observeEvents() {
        viewModel.onDeleteSuccess.observe(this, Observer {
            activity!!.onBackPressed()
        })

        viewModel.clickEvent.observe(this, Observer {
            when (it) {

                //                R.id.llBankDetail ->
                //                    findNavController().navigate(R.id.action_beneficiaryOverviewFragment_to_beneficiaryAccountDetailsFragment)

                R.id.confirmButton ->
                    if (!isFromAddBeneficiary) {
                        //                        ConfirmAddBeneficiary(activity!!)       //may be show a dialogue to confirm edit beneficairy call ???
                        viewModel.requestUpdateBeneficiary()


                        // also need to add validation on fields like iban mobile etc

                        // need to go back to main listing screen on succes


                    } else {
                        ConfirmAddBeneficiary(activity!!)
                    }
            }
        })
    }


    override fun onBackPressed(): Boolean {

        return false
    }

    private fun ConfirmAddBeneficiary(context: Context) {
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle(
                Translator.getString(
                    context,
                    R.string.screen_add_beneficiary_detail_display_text_alert_title
                )
            )
            .setMessage(
                Translator.getString(
                    context,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_description
                )
            )
            .setPositiveButton(
                Translator.getString(
                    context,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_yes
                ),
                DialogInterface.OnClickListener { dialog, which ->
                    //                    findNavController().navigate(R.id.action_addBeneficiaryFragment_to_addBankDetailsFragment)// start funds transfer screen
                    findNavController().navigate(R.id.action_beneficiaryOverviewFragment_to_transferSuccessFragment)// start funds transfer screen

                })

            .setNegativeButton(
                Translator.getString(
                    context,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_no
                ),
                DialogInterface.OnClickListener { dialog, which ->

                    super.onBackPressed() // finish this nav graph or all screens till here and start the required screen

                })
            .setCancelable(false)
            .show()
    }

    private fun getBinding(): FragmentBeneficiaryOverviewBinding {
        return (viewDataBinding as FragmentBeneficiaryOverviewBinding)
    }

}