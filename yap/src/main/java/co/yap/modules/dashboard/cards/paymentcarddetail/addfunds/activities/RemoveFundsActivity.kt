package co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.activities

import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.animation.addListener
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.databinding.ActivityRemoveFundsBinding
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.interfaces.IRemoveFunds
import co.yap.modules.dashboard.cards.paymentcarddetail.addfunds.viewmodels.RemoveFundsViewModel
import co.yap.networking.cards.responsedtos.Card
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.helpers.*
import co.yap.yapcore.helpers.extentions.afterTextChanged
import co.yap.yapcore.helpers.extentions.parseToDouble
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import co.yap.yapcore.managers.SessionManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_remove_funds.*

class RemoveFundsActivity : BaseBindingActivity<IRemoveFunds.ViewModel>(), IRemoveFunds.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_remove_funds
    override val viewModel: RemoveFundsViewModel
        get() = ViewModelProviders.of(this).get(RemoveFundsViewModel::class.java)

    companion object {
        private const val CARD = "card"
        fun newIntent(context: Context, card: Card): Intent {
            val intent = Intent(context, RemoveFundsActivity::class.java)
            intent.putExtra(CARD, card)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.card.set(intent.getParcelableExtra(CARD))
        viewModel.showSpareCardAvailableBalance() // do not move this line to above set card
        addObservers()
        val display = this.windowManager.defaultDisplay
        display.getRectSize(Rect())
        getBinding().clBottomNew.children.forEach { it.alpha = 0f }
    }

    override fun addObservers() {
        setEditTextWatcher()
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.isFeeReceived.observe(this, Observer {
            if (it) viewModel.updateFees(viewModel.state.amount)
        })
        viewModel.updatedFee.observe(this, Observer {
            if (it.isNotBlank()) setSpannableFee(it)
        })
    }

    private fun setSpannableFee(feeAmount: String?) {
        viewModel.state.transferFee.set(
            resources.getText(
                getString(Strings.common_text_fee), this.color(
                    R.color.colorPrimaryDark,
                    "${feeAmount?.toFormattedCurrency()}"
                )
            )
        )
    }

    private fun setDenominationValue(denominationAmount: String) {
        hideKeyboard()
        getBinding().etAmount.setText("")
        getBinding().etAmount.setText(denominationAmount)
        val position = getBinding().etAmount.length()
        getBinding().etAmount.setSelection(position)
        getBinding().etAmount.clearFocus()
    }

    private fun setEditTextWatcher() {
        etAmount.afterTextChanged {
            if (!viewModel.state.amount.isBlank() && viewModel.state.amount.parseToDouble() > 0) {
                checkOnTextChangeValidation()
            } else {
                setAmountBg()
            }
            viewModel.updateFees(viewModel.state.amount)
        }
    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.btnAction -> {
                when (getBinding().btnAction.text) {
                    getString(Strings.screen_success_funds_transaction_display_text_button) -> {
                        setupActionsIntent()
                    }
                    else -> {
                        viewModel.removeFunds {
                            setUpSuccessData()
                            performSuccessOperations()
                        }
                    }
                }
            }
            R.id.tvDominationFirstAmount -> setDenominationValue(
                viewModel.state.firstDenomination.get() ?: ""
            )
            R.id.tvDominationSecondAmount -> setDenominationValue(
                viewModel.state.secondDenomination.get() ?: ""
            )
            R.id.tvDominationThirdAmount -> setDenominationValue(
                viewModel.state.thirdDenomination.get() ?: ""
            )
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> finish()
        }
    }

    private fun checkOnTextChangeValidation() {
        when {
            !isBalanceAvailable() -> {
                setAmountBg(true)
                showBalanceNotAvailableError()
            }

            viewModel.state.amount.parseToDouble() < viewModel.state.minLimit -> {
                viewModel.state.valid.set(false)
            }

            viewModel.state.amount.parseToDouble() > viewModel.state.maxLimit -> {
                setAmountBg(true)
                showUpperLowerLimitError()
                showErrorSnackBar(viewModel.errorDescription)
            }
            else -> {
                setAmountBg(isValid = true)
            }
        }
    }

    private fun showUpperLowerLimitError() {
        viewModel.errorDescription = Translator.getString(
            this,
            Strings.common_display_text_min_max_limit_error_transaction,
            viewModel.state.minLimit.toString().toFormattedCurrency(),
            viewModel.state.maxLimit.toString().toFormattedCurrency()
        )
    }

    private fun showBalanceNotAvailableError() {
        viewModel.errorDescription = Translator.getString(
            this,
            Strings.common_display_text_available_balance_error
        ).format(viewModel.state.amount.toFormattedCurrency())
        showErrorSnackBar(viewModel.errorDescription)
    }

    private fun isBalanceAvailable(): Boolean {
        val availableBalance =
            viewModel.state.card.get()?.availableBalance?.toDoubleOrNull()
        return if (availableBalance != null) {
            (availableBalance >= viewModel.getAmountWithFee())
        } else
            false
    }

    private fun setAmountBg(isError: Boolean = false, isValid: Boolean = false) {
//        getBinding().etAmount.background =
//            this.resources.getDrawable(
//                if (isError) co.yap.yapcore.R.drawable.bg_funds_error else co.yap.yapcore.R.drawable.bg_funds,
//                null
//            )
        if (!isError) cancelAllSnackBar()
        viewModel.state.valid.set(isValid)
    }

    private fun showErrorSnackBar(errorMessage: String) {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(errorMessage)
            }
        } ?: getBinding().clSnackBar.showSnackBar(
            msg = errorMessage,
            viewBgColor = R.color.errorLightBackground,
            colorOfMessage = R.color.error, duration = Snackbar.LENGTH_INDEFINITE, marginTop = 0
        )
    }

    private fun setUpSuccessData() {
        viewModel.state.topUpSuccessMsg.set(
            resources.getText(
                getString(Strings.screen_success_remove_funds_transaction_display_text_moved_success),
                this.color(
                    R.color.colorPrimaryDark,
                    viewModel.state.amount.toFormattedCurrency()
                )
            )
        )

        viewModel.state.debitCardUpdatedBalance.set(
            resources.getText(
                getString(Strings.screen_success_funds_transaction_display_text_primary_balance),
                this.color(
                    R.color.colorPrimaryDark,
                    SessionManager.cardBalance.value?.availableBalance.toString()
                        .toFormattedCurrency()
                )
            )
        )

        viewModel.state.spareCardUpdatedBalance.set(
            resources.getText(
                getString(Strings.screen_success_funds_transaction_display_text_success_updated_prepaid_card_balance),
                this.color(
                    R.color.colorPrimaryDark,
                    (viewModel.state.card.get()?.availableBalance.parseToDouble() - viewModel.state.amount.parseToDouble()).toString()
                        .toFormattedCurrency()
                )
            )
        )
    }

    private fun performSuccessOperations() {
        runOnUiThread {
            getBinding().etAmount.visibility = View.GONE
            getBinding().btnAction.text =
                getString(Strings.screen_success_funds_transaction_display_text_button)
            YoYo.with(Techniques.FadeOut)
                .duration(300)
                .repeat(0)
                .playOn(getBinding().toolbar.getChildAt(0))
            getBinding().clBottom.children.forEach { it.alpha = 0f }
            getBinding().btnAction.alpha = 0f
            getBinding().cardInfoLayout.clRightData.children.forEach { it.alpha = 0f }
            Handler(Looper.getMainLooper()).postDelayed({ runAnimations() }, 1500)
            runCardAnimation()
        }
    }


    private fun runAnimations() {
        AnimationUtils.runSequentially(
            AnimationUtils.runTogether(
                AnimationUtils.jumpInAnimation(getBinding().tvCardNameSuccess),
                AnimationUtils.jumpInAnimation(getBinding().tvCardNumberSuccess)
                    .apply { startDelay = 100 },
                AnimationUtils.jumpInAnimation(getBinding().tvTopUp).apply { startDelay = 200 },
                AnimationUtils.jumpInAnimation(getBinding().tvPrimaryCardBalance)
                    .apply { startDelay = 300 },
                AnimationUtils.jumpInAnimation(getBinding().tvNewSpareCardBalance)
                    .apply { startDelay = 400 },
                AnimationUtils.jumpInAnimation(getBinding().btnAction).apply { startDelay = 600 }

            )
        ).apply {
            addListener(onEnd = {
                startCheckMarkAnimation()
            })
        }.start()
    }

    private fun startCheckMarkAnimation() {
        YoYo.with(Techniques.BounceIn)
            .duration(1000)
            .repeat(0)
            .playOn(getBinding().ivSuccessCheckMark)
    }


    private fun runCardAnimation() {
        Handler(Looper.getMainLooper()).postDelayed({
            cardAnimation().apply {
                addListener(onEnd = {
                })
            }.start()
        }, 500)
    }


    private fun cardAnimation(): AnimatorSet {
        val checkBtnEndPosition =
            (cardInfoLayout.measuredWidth / 2) - (getBinding().cardInfoLayout.ivCustomCard.width / 2)
        return AnimationUtils.runSequentially(
            AnimationUtils.slideHorizontal(
                view = getBinding().cardInfoLayout.ivCustomCard,
                from = getBinding().cardInfoLayout.ivCustomCard.x,
                to = checkBtnEndPosition.toFloat(),
                duration = 500
            ),
            AnimationUtils.pulse(getBinding().cardInfoLayout.ivCustomCard)
        )
    }

    private fun setupActionsIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra(
            "newBalance",
            (viewModel.state.card.get()?.availableBalance.parseToDouble() - viewModel.state.amount.parseToDouble()).toString()
        )
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    override fun removeObservers() {
        viewModel.clickEvent.removeObserver(clickObserver)
    }

    private fun getBinding(): ActivityRemoveFundsBinding {
        return (viewDataBinding as ActivityRemoveFundsBinding)
    }

    override fun onBackPressed() {
        if (getBinding().btnAction.text != getString(Strings.screen_success_funds_transaction_display_text_button)) {
            super.onBackPressed()
        }
    }
}