package co.yap.modules.dashboard

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.activities.ChangeCardPinActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.activities.ForgotCardPinActivity
import co.yap.networking.cards.requestdtos.ChangeCardPinRequest
import co.yap.yapcore.BaseBindingFragment


class ChangePinFragment : BaseBindingFragment<IChangePin.ViewModel>(), IChangePin.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_change_pin
    var cardSerialNumber:String = ""
    override val viewModel: IChangePin.ViewModel
        get() = ViewModelProviders.of(this).get(ChangePinViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        visiblityListener()
        cardSerialNumber = (activity as ChangeCardPinActivity).cardSerialNumber
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnAction -> {
                    viewModel.changePinRequest(
                        ChangeCardPinRequest(
                            viewModel.state.oldPin,
                            viewModel.state.newPin,
                            viewModel.state.confirmNewPin,
                            cardSerialNumber)

                    ) {
                        findNavController().navigate(R.id.action_changeCardPinFragment_to_changePinSuccessFragment)
                    }

                }
                R.id.tvForgotPasscode->
                {
                    startActivity(
                        ForgotCardPinActivity.newIntent(requireContext(), cardSerialNumber)
                    )
                }

            }
        })
    }

    private fun visiblityListener()
    {

    }
}