package co.yap.modules.dashboard.yapit.topup.topupamount.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentVerifyCardCvvBinding
import co.yap.modules.dashboard.yapit.topup.topupamount.activities.TopUpCardActivity
import co.yap.modules.dashboard.yapit.topup.topupamount.interfaces.IVerifyCardCvv
import co.yap.modules.dashboard.yapit.topup.topupamount.viewModels.VerifyCardCvvViewModel
import co.yap.networking.customers.responsedtos.beneficiary.TopUpCard
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager

class VerifyCardCvvFragment : BaseBindingFragment<IVerifyCardCvv.ViewModel>(), IVerifyCardCvv.View {
    val args: VerifyCardCvvFragmentArgs by navArgs()
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_verify_card_cvv

    override val viewModel: IVerifyCardCvv.ViewModel
        get() = ViewModelProviders.of(this).get(VerifyCardCvvViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clickEvent.observe(this, clickEvent)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.cvvSpanableString.set(
            getString(Strings.screen_topup_card_cvv_display_text_cvv).format(
                args.currencyType,
                args.amount.toFormattedCurrency(showCurrency = false,currency = SessionManager.getDefaultCurrency())
            )
        )
        getBindings().tvTopUpDescription.text = Utils.getSppnableStringForAmount(
            requireContext(),
            viewModel.state.cvvSpanableString.get().toString(),
            args.currencyType,
            Utils.getFormattedCurrencyWithoutComma(args.amount)
        )

        if (context is TopUpCardActivity) {
            val cardInfo: TopUpCard? = (context as TopUpCardActivity).cardInfo
            viewModel.state.cardInfo.set(cardInfo)
            viewModel.state.formattedCardNo.set(Utils.getFormattedCardNumber(cardInfo?.number.toString()))
            when (cardInfo?.logo) {
                Constants.VISA,
                Constants.MASTER -> {
                    getBindings().cvvView.visibility = View.VISIBLE
                    getBindings().cvvAmericanView.visibility = View.GONE
                }
                Constants.AMEX -> {
                    getBindings().cvvView.visibility = View.GONE
                    getBindings().cvvAmericanView.visibility = View.VISIBLE
                }
            }
        }
    }

    var clickEvent = Observer<Int> {
        val action =
            VerifyCardCvvFragmentDirections.actionVerifyCardCvvFragmentToTopUpCardSuccessFragment(
                args.amount,
                args.currencyType
            )
        when (it) {
            R.id.btnAction ->
                if (context is TopUpCardActivity) {
                    viewModel.topUpTransactionRequest((context as TopUpCardActivity).topUpTransactionModel?.value)
                }

            Constants.TOP_UP_TRANSACTION_SUCCESS -> {
                // Send Broadcast for updating transactions list in `Home Fragment`
                val intent = Intent(Constants.BROADCAST_UPDATE_TRANSACTION)
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
                findNavController().navigate(action)
                // findNavController().navigate(R.id.action_verifyCardCvvFragment_to_topUpCardSuccessFragment)
            }
        }

    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> findNavController().navigateUp()
        }
    }

    private fun getBindings(): FragmentVerifyCardCvvBinding {
        return viewDataBinding as FragmentVerifyCardCvvBinding
    }
}