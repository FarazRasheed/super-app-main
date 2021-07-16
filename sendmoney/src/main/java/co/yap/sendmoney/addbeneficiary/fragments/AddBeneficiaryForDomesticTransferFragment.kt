package co.yap.sendmoney.addbeneficiary.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.otp.GenericOtpFragment
import co.yap.modules.otp.OtpDataModel
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.IAddBeneficiary
import co.yap.sendmoney.addbeneficiary.viewmodels.AddBeneficiaryViewModel
import co.yap.sendmoney.fragments.SendMoneyBaseFragment
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.OTPActions
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.startFragmentForResult
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager

class AddBeneficiaryForDomesticTransferFragment :
    SendMoneyBaseFragment<IAddBeneficiary.ViewModel>(),
    IAddBeneficiary.View {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_add_beneficiary_domestic_transfer
    override val viewModel: AddBeneficiaryViewModel
        get() = ViewModelProviders.of(this).get(AddBeneficiaryViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.otpCreateObserver.observe(this, otpCreateObserver)
        viewModel.addBeneficiarySuccess.observe(this, Observer {
            if (it) {
                addBeneficiarySuccessDialog()
            }
        })
    }

    private val otpCreateObserver = Observer<Boolean> {
        if (it) {
           startOtpFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
        viewModel.otpCreateObserver.removeObservers(this)
    }

    private fun startOtpFragment() {
        startFragmentForResult<GenericOtpFragment>(
            GenericOtpFragment::class.java.name,
            bundleOf(
                OtpDataModel::class.java.name to OtpDataModel(
                    OTPActions.DOMESTIC_BENEFICIARY.name,//action,
                    SessionManager.user?.currentCustomer?.getFormattedPhoneNumber(requireContext())
                        ?: ""
                )
            ),
            showToolBar = true,
            toolBarTitle = getString(Strings.screen_cash_pickup_funds_display_otp_header)
        ) { resultCode, _ ->
            if (resultCode == Activity.RESULT_OK) {
                viewModel.addDomesticBeneficiary(viewModel.parentViewModel?.beneficiary?.value)
            }
        }
    }
    private fun addBeneficiarySuccessDialog() {
        context?.let { it ->
            Utils.confirmationDialog(it,
                Translator.getString(
                    it,
                    R.string.screen_add_beneficiary_detail_display_text_alert_title
                ),
                Translator.getString(
                    it,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_description
                ), Translator.getString(
                    it,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_yes
                ), Translator.getString(
                    it,
                    R.string.screen_add_beneficiary_detail_display_button_block_alert_no
                ),
                object : OnItemClickListener {
                    override fun onItemClick(view: View, data: Any, pos: Int) {
                        if (data is Boolean) {
                            if (data) {
                                activity?.let {
                                    setIntentResult(isMoneyTransfer = true)
                                }
                            } else {
                                activity?.let {
                                    setIntentResult()
                                }
                            }
                        }
                    }
                },false)
        }
    }

    private fun setIntentResult(isMoneyTransfer: Boolean = false) {
        val intent = Intent()
        intent.putExtra(Constants.BENEFICIARY_CHANGE, true)
        intent.putExtra(Constants.IS_TRANSFER_MONEY, isMoneyTransfer)
        intent.putExtra(Beneficiary::class.java.name, viewModel.beneficiary)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

}
