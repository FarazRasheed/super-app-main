package co.yap.sendmoney.fundtransfer.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.networking.customers.requestdtos.SMCoolingPeriodRequest
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.fundtransfer.interfaces.IBeneficiaryFundTransfer
import co.yap.sendmoney.fundtransfer.models.TransferFundData
import co.yap.sendmoney.fundtransfer.viewmodels.BeneficiaryFundTransferViewModel
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.helpers.cancelAllSnackBar
import co.yap.yapcore.helpers.extentions.getProductCode
import co.yap.yapcore.helpers.getSnackBarFromQueue
import co.yap.yapcore.helpers.showSnackBar
import co.yap.yapcore.helpers.updateSnackBarText
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator
import co.yap.yapcore.managers.SessionManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_beneficiary_cash_transfer.*


class BeneficiaryFundTransferActivity : BaseBindingActivity<IBeneficiaryFundTransfer.ViewModel>(),
    IFragmentHolder, INavigator {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_beneficiary_cash_transfer

    override val viewModel: IBeneficiaryFundTransfer.ViewModel
        get() = ViewModelProviders.of(this).get(BeneficiaryFundTransferViewModel::class.java)
    override val navigator: IBaseNavigator
        get() = DefaultNavigator(
            this@BeneficiaryFundTransferActivity,
            R.id.beneficiary_cash_transfer_nav_host_fragment
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBeneficiary()
        viewModel.errorEvent.observe(this, errorEvent)
    }

    val errorEvent = Observer<String> {
        if (!it.isNullOrEmpty())
            showErrorSnackBar(it)
        else
            hideErrorSnackBar()
    }

    private fun showErrorSnackBar(errorMessage: String) {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(errorMessage)
            }
        } ?: clFTSnackbar.showSnackBar(
            msg = errorMessage,
            viewBgColor = R.color.errorLightBackground,
            colorOfMessage = R.color.error, duration = Snackbar.LENGTH_INDEFINITE, marginTop = 0
        )
    }

    private fun hideErrorSnackBar() {
        cancelAllSnackBar()
    }

    private fun getBeneficiary() {
        if (intent != null) {
            viewModel.beneficiary.value =
                intent.getParcelableExtra(Constants.BENEFICIARY) as? Beneficiary?
            viewModel.transferData.value = TransferFundData()
            viewModel.transferData.value?.position = intent.getIntExtra(Constants.POSITION, 0)
            viewModel.isSameCurrency = viewModel.beneficiary.value?.currency == "AED"
            val request = SMCoolingPeriodRequest(
                beneficiaryId = viewModel.beneficiary.value?.id?.toString()!!,
                productCode = viewModel.beneficiary.value.getProductCode()
            )
            viewModel.getCoolingPeriod(request)
        }
    }

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.beneficiary_cash_transfer_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        viewModel.errorEvent.removeObservers(this)
        super.onDestroy()
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> onBackPressed()
            R.id.tvRightText -> {
                val i = Intent()
                intent?.let { inten ->
                    i.putExtra(
                        Constants.BENEFICIARY_CHANGE,
                        inten.getBooleanExtra(Constants.IS_NEW_BENEFICIARY, false)
                    )
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }
            }
        }
    }
}