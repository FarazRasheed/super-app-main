package co.yap.modules.dashboard.yapit.topup.topupamount.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentTopUpCardSuccessBinding
import co.yap.modules.dashboard.yapit.topup.topupamount.activities.TopUpCardActivity
import co.yap.modules.dashboard.yapit.topup.topupamount.interfaces.ITopUpCardSuccess
import co.yap.modules.dashboard.yapit.topup.topupamount.viewModels.TopUpCardSuccessViewModel
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager

class TopUpCardSuccessFragment : BaseBindingFragment<ITopUpCardSuccess.ViewModel>(),
    ITopUpCardSuccess.View {
    val args: VerifyCardCvvFragmentArgs by navArgs()
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_top_up_card_success
    override val viewModel: TopUpCardSuccessViewModel
        get() = ViewModelProviders.of(this).get(TopUpCardSuccessViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAccountBalanceRequest()
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.amount = args.amount
        viewModel.state.currencyType = args.currencyType
        viewModel.state.buttonTitle = (activity as? TopUpCardActivity)?.successButtonLabel ?: ""
        setUpData()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickEvent)
        SessionManager.cardBalance.observe(this, Observer {
            viewModel.state.availableBalanceSpanable.set(
                getString(Strings.screen_topup_success_display_text_account_balance_title).format(
                    args.currencyType,
                    it.availableBalance?.toFormattedCurrency(
                        showCurrency = false,
                        currency = args.currencyType
                    )
                )
            )

            getBindings().tvNewSpareCardBalance.text = Utils.getSpannableStringForLargerBalance(
                requireContext(),
                viewModel.state.availableBalanceSpanable.get().toString(),
                args.currencyType,
                Utils.getFormattedCurrencyWithoutComma(it.availableBalance)
            )
            getBindings().ivSuccessCheckMark.visibility = View.VISIBLE
            viewModel.state.loading = false
        })
    }

    val clickEvent = Observer<Int> {
        when (it) {
            R.id.btnAction -> {
                val intent = Intent()
                intent.putExtra(Constants.TOP_UP_VIA_EXTERNAL_CARD, true)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
    }

    private fun setUpData() {
        if (context is TopUpCardActivity) {
            val cardInfo: TopUpCard? = (context as TopUpCardActivity).cardInfo
            viewModel.state.cardInfo.set(cardInfo)
            viewModel.state.formattedCardNo.set(Utils.getFormattedCardNumber(viewModel.state.cardInfo.get()?.number.toString()))
        }
        viewModel.state.topUpSuccess =
            getString(Strings.screen_topup_success_display_text_success_transaction_message).format(
                viewModel.state.currencyType,
                viewModel.state.amount
            )

        getBindings().tvTopUp.text = Utils.getSppnableStringForAmount(
            requireContext(),
            viewModel.state.topUpSuccess,
            viewModel.state.currencyType,
            Utils.getFormattedCurrencyWithoutComma(viewModel.state.amount)
        )
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    private fun getBindings(): FragmentTopUpCardSuccessBinding {
        return viewDataBinding as FragmentTopUpCardSuccessBinding

    }

}