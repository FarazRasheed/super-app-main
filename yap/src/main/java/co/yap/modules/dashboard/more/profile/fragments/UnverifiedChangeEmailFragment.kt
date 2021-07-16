package co.yap.modules.dashboard.more.profile.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.R
import co.yap.modules.dashboard.more.profile.intefaces.IChangeEmail
import co.yap.modules.dashboard.more.profile.viewmodels.UnverifiedChangeEmailViewModel
import co.yap.modules.dashboard.unverifiedemail.UnVerifiedEmailActivity
import co.yap.translation.Strings

class UnverifiedChangeEmailFragment : ChangeEmailFragment() {
    override val viewModel: IChangeEmail.ViewModel
        get() = ViewModelProviders.of(this).get(UnverifiedChangeEmailViewModel::class.java)


    override fun setObservers() {
        if (context is UnVerifiedEmailActivity)
            (context as UnVerifiedEmailActivity).viewModel.state.tootlBarTitle =
                getString(Strings.screen_change_email_display_text_heading)
        viewModel.success.observe(this, Observer {
            if (it) {
                val action =
                    UnverifiedChangeEmailFragmentDirections.actionUnverifiedChangeEmailFragmentToUnverifiedChangeEmailSuccessFragment()
                findNavController().navigate(action)
            }
        })
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.tbBtnBack -> {

                }
            }
        })

    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        viewModel.success.removeObservers(this)
        viewModel.changeEmailSuccessEvent.removeObservers(this)
        super.onDestroy()
    }
}