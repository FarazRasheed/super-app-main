package co.yap.modules.dashboard.yapit.topup.topupamount.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentTopUpCardFundsBinding
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.interfaces.IFundActions
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.viewmodels.FundActionsViewModel
import co.yap.modules.dashboard.yapit.topup.addtopupcard.activities.AddTopUpCardActivityV2
import co.yap.modules.dashboard.yapit.topup.topupamount.activities.TopUpCardActivity
import co.yap.modules.dashboard.yapit.topup.topupamount.viewModels.TopUpCardFundsViewModel
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.TransactionProductCode
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.cancelAllSnackBar
import co.yap.yapcore.helpers.extentions.getValueWithoutComa
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.showTextUpdatedAbleSnackBar
import co.yap.yapcore.managers.SessionManager
import com.google.android.material.snackbar.Snackbar

class TopUpCardFundsFragment : BaseBindingFragment<IFundActions.ViewModel>(),
    IFundActions.View {
    private var parentViewModel: FundActionsViewModel? = null
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_top_up_card_funds

    override val viewModel: IFundActions.ViewModel
        get() = ViewModelProviders.of(this).get(TopUpCardFundsViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel =
            this.let { ViewModelProviders.of(it).get(FundActionsViewModel::class.java) }
        parentViewModel?.getTransferFees(TransactionProductCode.TOP_UP_VIA_CARD.pCode)

        setObservers()
        viewModel.firstDenominationClickEvent.observe(this, Observer {
            Utils.hideKeyboard(view)
            getBindings().etAmount.setText("")
            getBindings().etAmount.append(viewModel.state.denominationAmount)
            val position = getBindings().etAmount.length()
            getBindings().etAmount.setSelection(position)
            getBindings().etAmount.clearFocus()
        })
        viewModel.secondDenominationClickEvent.observe(this, Observer {
            Utils.hideKeyboard(view)
            getBindings().etAmount.setText("")
            getBindings().etAmount.append(viewModel.state.denominationAmount)
            val position = getBindings().etAmount.length()
            getBindings().etAmount.setSelection(position)
            getBindings().etAmount.clearFocus()
        })
        viewModel.thirdDenominationClickEvent.observe(this, Observer {
            Utils.hideKeyboard(view)
            getBindings().etAmount.setText("")
            getBindings().etAmount.append(viewModel.state.denominationAmount)
            val position = getBindings().etAmount.length()
            getBindings().etAmount.setSelection(position)
            getBindings().etAmount.clearFocus()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is TopUpCardActivity)
            (activity as TopUpCardActivity).cardInfo?.let {
                viewModel.initateVM(it)
            }
        setupData()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickEvent)
        viewModel.errorEvent.observe(this, Observer {
            showTextUpdatedAbleSnackBar(
                viewModel.state.errorDescription,
                Snackbar.LENGTH_INDEFINITE
            )
        })
        viewModel.enteredAmount.observe(this, enterAmountObserver)
        viewModel.htmlLiveData.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                launchActivity<AddTopUpCardActivityV2>(requestCode = Constants.EVENT_TOP_UP_CARD_TRANSACTION) {
                    putExtra(Constants.KEY, it)
                    putExtra(Constants.TYPE, Constants.TYPE_TOP_UP_TRANSACTION)
                }
            } else {
                return@Observer
            }

        })

        viewModel.topUpTransactionModelLiveData?.observe(this, Observer {
            if (context is TopUpCardActivity) {
                (context as TopUpCardActivity).topUpTransactionModel =
                    viewModel.topUpTransactionModelLiveData
                val action =
                    TopUpCardFundsFragmentDirections.actionTopUpCardFundsFragmentToVerifyCardCvvFragment(
                        viewModel.enteredAmount.value.toString(),
                        viewModel.state.currencyType
                    )
                findNavController().navigate(
                    action
                )
            }
        })
        parentViewModel?.isFeeReceived?.observe(this, Observer {
            if (it) parentViewModel?.updateFees(viewModel.state.amount ?: "")
        })
        parentViewModel?.updatedFee?.observe(this, Observer {
            if (it.isNotBlank()) setUpFeeData(it)
        })
    }

    var clickEvent = Observer<Int> {
        when (it) {
            R.id.btnAction -> {
                if (viewModel.enteredAmount.value?.getValueWithoutComa().parseToDouble() ?: 0.0 < viewModel.state.minLimit) {
                    viewModel.state.amountBackground =
                        resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds_error, null)
                    showUpperLowerLimitError()
                } else
                    viewModel.createTransactionSession()
            }
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> activity?.finish()
        }
    }

    private val enterAmountObserver = Observer<String> {
        parentViewModel?.updateFees(it.getValueWithoutComa(), isTopUpFee = true)
        if (it.isNotBlank()) {
            when {
                isMaxMinLimitReached(it.getValueWithoutComa()) -> {
                    viewModel.state.valid = false
                    viewModel.state.amountBackground =
                        resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds_error, null)
                    showUpperLowerLimitError()
                }
                it.getValueWithoutComa().toDoubleOrNull() ?: 0.0 < viewModel.state.minLimit -> {
                    viewModel.state.valid = true
                }
                else -> {
                    viewModel.state.valid = true
                    viewModel.state.amountBackground =
                        resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
                    cancelAllSnackBar()
                }
            }
        } else {
            viewModel.state.valid = false
            viewModel.state.amountBackground =
                resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
            cancelAllSnackBar()
        }
    }

    private fun isMaxMinLimitReached(amount: String): Boolean {
        return if (amount.isNotBlank() || amount.toDoubleOrNull() ?: 0.0 > 0.0)
            amount.toDoubleOrNull() ?: 0.0 > viewModel.state.maxLimit
        else
            false
    }

    fun showUpperLowerLimitError() {
        viewModel.state.errorDescription = getString(
            Strings.common_display_text_min_max_limit_error_transaction
        ).format(
            viewModel.state.minLimit.toString().toFormattedCurrency(),
            viewModel.state.maxLimit.toString().toFormattedCurrency()
        )
        showTextUpdatedAbleSnackBar(
            viewModel.state.errorDescription,
            Snackbar.LENGTH_INDEFINITE
        )
    }

    private fun setupData() {
        if (context is TopUpCardActivity) {
            viewModel.state.cardNumber =
                (context as TopUpCardActivity).cardInfo?.number.toString()
            viewModel.state.cardName = (context as TopUpCardActivity).cardInfo?.alias.toString()
        }

        viewModel.state.availableBalance =
            SessionManager.cardBalance.value?.availableBalance.toString()

        getBindings().tvAvailableBalanceGuide.text = Utils.getSppnableStringForAmount(
            requireContext(),
            viewModel.state.availableBalanceGuide,
            viewModel.state.currencyType,
            Utils.getFormattedCurrencyWithoutComma(viewModel.state.availableBalance)
        )
    }

    private fun setUpFeeData(transactionFee: String) {
        if (transactionFee == getString(Strings.screen_topup_transfer_display_text_transaction_no_fee)) {
            viewModel.state.transactionFeeSpannableString =
                getString(Strings.screen_topup_transfer_display_text_transaction_fee)
                    .format(transactionFee)
            getBindings().tvFeeDescription.text = Utils.getSpannableString(
                requireContext(),
                viewModel.state.transactionFeeSpannableString, transactionFee
            )
        } else if (transactionFee.toDouble() >= 0) {
            viewModel.state.transactionFeeSpannableString =
                getString(Strings.screen_topup_transfer_display_text_transaction_fee)
                    .format(
                        viewModel.state.currencyType + " " + transactionFee.toFormattedCurrency(
                            showCurrency = false,
                            currency = SessionManager.getDefaultCurrency()
                        )
                    )
            getBindings().tvFeeDescription.text = Utils.getSppnableStringForAmount(
                requireContext(),
                viewModel.state.transactionFeeSpannableString ?: "",
                viewModel.state.currencyType,
                transactionFee
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.EVENT_TOP_UP_CARD_TRANSACTION && resultCode == Activity.RESULT_OK) {
            if (true == data?.let {
                    it.getBooleanExtra(Constants.START_POOLING, false)
                }) {
                viewModel.startPooling(true)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.enteredAmount.removeObservers(this)
    }

    private fun getBindings(): FragmentTopUpCardFundsBinding {
        return viewDataBinding as FragmentTopUpCardFundsBinding
    }
}