package co.yap.modules.dashboard.yapit.topup.landing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.modules.dashboard.yapit.topup.cardslisting.TopUpBeneficiariesActivity
import co.yap.modules.dashboard.yapit.topup.topupbankdetails.TopUpBankDetailsFragment
import co.yap.translation.Strings
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.extentions.startFragment


class TopUpLandingActivity : BaseBindingActivity<ITopUpLanding.ViewModel>() {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_topup_landing
    override val viewModel: ITopUpLanding.ViewModel
        get() = ViewModelProviders.of(this).get(
            TopUpLandingViewModel::class.java
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clickEvent.observe(this, clickEventObserver)
    }

    private val clickEventObserver = Observer<Int> {
        when (it) {
            R.id.llBankTransferType -> {
                startBankFragment()
            }
            R.id.llCardsTransferType -> {
                startActivityForResult(
                    TopUpBeneficiariesActivity.newIntent(
                        this,
                        getString(Strings.screen_topup_success_display_text_dashboard_action_button_title)
                    ),
                    RequestCodes.REQUEST_SHOW_BENEFICIARY
                )
            }
        }
    }

    private fun startBankFragment() {
        startFragment<TopUpBankDetailsFragment>(
            TopUpBankDetailsFragment::class.java.name, false,
            bundleOf(
            )
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCodes.REQUEST_SHOW_BENEFICIARY) {
                if (RequestCodes.REQUEST_SHOW_BENEFICIARY == data?.getIntExtra(
                        RequestCodes.REQUEST_SHOW_BENEFICIARY.toString(),
                        0
                    )
                ) {
                    finish()
                }
            }
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                onBackPressed()
            }
        }
    }
}