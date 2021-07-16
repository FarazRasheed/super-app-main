package co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.passcode.IPassCode
import co.yap.modules.passcode.PassCodeViewModel
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.databinding.FragmentPassCodeBinding

class VerifyCurrentPasscodeFragment : BaseBindingFragment<IPassCode.ViewModel>(), IPassCode.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_pass_code
    override val viewModel: IPassCode.ViewModel
        get() = ViewModelProviders.of(this).get(PassCodeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setTitles(
            title = getString(Strings.screen_current_passcode_display_text_heading),
            buttonTitle = getString(Strings.screen_current_card_pin_display_button_next)
        )
        setObservers()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.forgotTextVisibility = false
        getBinding().dialer.hideFingerprintView()
        getBinding().dialer.upDatedDialerPad(viewModel.state.passCode)
    }

    fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnAction -> {
                    viewModel.validatePassCode { isValidate ->
                        if (isValidate) {
                            val action =
                                VerifyCurrentPasscodeFragmentDirections.actionVerifyCurrentPasscodeFragmentToSetNewCardPinFragment2(
                                    Constants.FORGOT_CARD_PIN_FLOW
                                )
                            findNavController().navigate(action)
                        } else
                            getBinding().dialer.startAnimation()
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    private fun getBinding(): FragmentPassCodeBinding {
        return (viewDataBinding as FragmentPassCodeBinding)
    }

}