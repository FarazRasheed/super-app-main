package co.yap.modules.forgotpasscode.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import co.yap.modules.forgotpasscode.interfaces.IForgotPasscodeSuccess
import co.yap.modules.forgotpasscode.viewmodels.ForgotPasscodeSuccessViewModel
import co.yap.translation.Strings
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.R
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.Constants.KEY_IS_USER_LOGGED_IN
import co.yap.yapcore.helpers.SharedPreferenceManager


class ForgotPasscodeSuccessFragment : BaseBindingFragment<IForgotPasscodeSuccess.ViewModel>() {
    val args: ForgotPasscodeSuccessFragmentArgs by navArgs()
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_forgot_passcode_success

    override val viewModel: IForgotPasscodeSuccess.ViewModel
        get() = ViewModelProviders.of(this).get(ForgotPasscodeSuccessViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferenceManager = SharedPreferenceManager(requireContext())

        viewModel.handlePressOnButtonEvent.observe(this, Observer {
            if (args.navigationType == Constants.FORGOT_PASSCODE_FROM_CHANGE_PASSCODE) {
                activity?.finish()
            } else {
                sharedPreferenceManager.save(KEY_IS_USER_LOGGED_IN, false)
                //val intent=Intent(context,Class.forName("co.yap.app.activities.MainActivity"))
                val intent = Intent("co.yap.app.OPEN_LOGIN")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                requireContext().startActivity(intent)
                activity?.finish()
            }
            // findNavController().popBackStack(R.id.loginFragment,false)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // if (args.navigationType==Constants.FORGOT_PASSCODE_FROM_CHANGE_PASSCODE){
        viewModel.state.buttonTitle =
            getString(Strings.screen_add_spare_card_completion_button_done)
        // }
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}