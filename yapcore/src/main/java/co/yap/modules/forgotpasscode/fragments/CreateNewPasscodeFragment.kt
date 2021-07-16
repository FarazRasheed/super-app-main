package co.yap.modules.forgotpasscode.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.yap.modules.passcode.IPassCode
import co.yap.modules.passcode.PassCodeViewModel
import co.yap.modules.webview.WebViewFragment
import co.yap.translation.Strings
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.databinding.FragmentPassCodeBinding
import co.yap.yapcore.helpers.extentions.startFragment

class CreateNewPasscodeFragment : BaseBindingFragment<IPassCode.ViewModel>() {
    private val args: CreateNewPasscodeFragmentArgs by navArgs()

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_pass_code
    override val viewModel: IPassCode.ViewModel
        get() = ViewModelProviders.of(this).get(PassCodeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.forgotTextVisibility = false
        viewModel.setLayoutVisibility(false)
        viewModel.setTitles(
            title = getString(Strings.screen_create_passcode_display_text_title),
            buttonTitle = getString(Strings.screen_create_new_passcode_button_text)
        )

        viewModel.mobileNumber = args.mobileNumber
        viewModel.token = args.token
        viewModel.state.toolbarVisibility.set(args.navigationType == "VERIFY_PASSCODE_FRAGMENT")

        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.tvTermsAndConditions -> {
                    startFragment(
                        fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                            Constants.PAGE_URL to Constants.URL_TERMS_CONDITION
                        ), showToolBar = false
                    )
                }
                R.id.btnAction -> {
                    if (viewModel.isValidPassCode()) {
                        viewModel.forgotPassCodeRequest {
                            val action =
                                CreateNewPasscodeFragmentDirections.actionCreateNewPasscodeFragmentToForgotPasscodeSuccessFragment(
                                    navigationType = args.navigationType
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBindings().dialer.hideFingerprintView()
        getBindings().dialer.upDatedDialerPad(viewModel.state.passCode)
    }


    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    fun getBindings(): FragmentPassCodeBinding {
        return viewDataBinding as FragmentPassCodeBinding
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                navigateBack()
            }
        }
    }
}