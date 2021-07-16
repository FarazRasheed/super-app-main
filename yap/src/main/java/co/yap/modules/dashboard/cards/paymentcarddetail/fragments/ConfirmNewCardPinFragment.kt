package co.yap.modules.dashboard.cards.paymentcarddetail.fragments

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.activities.ChangeCardPinActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.activities.ForgotCardPinActivity
import co.yap.modules.otp.GenericOtpFragment
import co.yap.modules.otp.OtpDataModel
import co.yap.modules.setcardpin.pinflow.IPin
import co.yap.modules.setcardpin.pinflow.PINViewModel
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.databinding.FragmentPinBinding
import co.yap.yapcore.enums.OTPActions
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.helpers.extentions.startFragmentForResult
import co.yap.yapcore.managers.SessionManager

class ConfirmNewCardPinFragment : BaseBindingFragment<IPin.ViewModel>(), IPin.View {
    private val args: ConfirmNewCardPinFragmentArgs by navArgs()

    override val viewModel: IPin.ViewModel
        get() = ViewModelProviders.of(this).get(PINViewModel::class.java)

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_pin

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
        viewModel.setConfirmNewCardPinFragmentData()
        getBindings().dialer.hideFingerprintView()
        getBindings().dialer.upDatedDialerPad(viewModel.state.pincode)
        getBindings().dialer.updateDialerLength(4)
        loadData()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnAction -> {
                    if (viewModel.state.newPin == viewModel.state.pincode) {
                        when (viewModel.state.flowType) {
                            Constants.FORGOT_CARD_PIN_FLOW -> startOtpFragment()
                            else -> {
                                viewModel.changeCardPinRequest(
                                    viewModel.state.oldPin,
                                    viewModel.state.newPin,
                                    viewModel.state.pincode,
                                    viewModel.state.cardSerialNumber
                                ) {
                                  //  findNavController().navigate(R.id.action_confirmNewCardPinFragment_to_changePinSuccessFragment)
                                }
                            }
                        }
                    } else {
                        getBindings().dialer.startAnimation()
                        viewModel.state.dialerError =
                            getString(Strings.screen_confirm_card_pin_display_text_error_pins_not_same)
                    }

                }

                Constants.FORGOT_CARD_PIN_NAVIGATION -> {
                    findNavController().navigate(R.id.action_confirmNewCardPinFragment2_to_changePinSuccessFragment2)
                }
            }
        })
        viewModel.errorEvent.observe(this, Observer {
            getBindings().dialer.startAnimation()
        })
    }

    override fun loadData() {
        viewModel.state.oldPin = args.oldPinCode
        viewModel.state.newPin = args.newPinCode
        if (activity is ChangeCardPinActivity) {
            viewModel.state.cardSerialNumber = (activity as ChangeCardPinActivity).cardSerialNumber
        } else if (activity is ForgotCardPinActivity) {
            viewModel.state.cardSerialNumber =
                (activity as ForgotCardPinActivity).getCardSerialNumber()
            viewModel.state.flowType = args.flowType
        }
    }

    private fun startOtpFragment() {
        startFragmentForResult<GenericOtpFragment>(
            GenericOtpFragment::class.java.name,
            bundleOf(
                OtpDataModel::class.java.name to OtpDataModel(
                    OTPActions.FORGOT_CARD_PIN.name,
                    SessionManager.user?.currentCustomer?.getFormattedPhoneNumber(requireContext())
                        ?: ""
                )
            )
        ,showToolBar = true
        ) { resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                val token =
                    data?.getValue(
                        "token",
                        ExtraType.STRING.name
                    ) as? String

                token?.let {
                    viewModel.forgotCardPinRequest(
                        viewModel.state.cardSerialNumber,
                        viewModel.state.pincode, it
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroyView()
    }

    private fun getBindings(): FragmentPinBinding {
        return viewDataBinding as FragmentPinBinding
    }
}