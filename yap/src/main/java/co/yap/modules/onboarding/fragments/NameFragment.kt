package co.yap.modules.onboarding.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.onboarding.interfaces.IName
import co.yap.modules.onboarding.viewmodels.NameViewModel
import co.yap.yapcore.leanplum.trackEvent
import co.yap.yapcore.leanplum.SignupEvents
import kotlinx.android.synthetic.main.fragment_name.*

class NameFragment : OnboardingChildFragment<IName.ViewModel>(), IName.View {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_name

    override val viewModel: IName.ViewModel
        get() = ViewModelProviders.of(this).get(NameViewModel::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.nextButtonPressEvent.observe(this, nextButtonObserver)
        viewModel.state.firstNameError.observe(this, Observer {
            if (!it.isNullOrBlank()) {
                etFirstName.settingUIForError(it)
            } else {
                etFirstName.settingUIForNormal()
            }
        })

        viewModel.state.lastNameError.observe(this, Observer {
            if (!it.isNullOrBlank()) {
                etLastName.settingUIForError(it)
            } else {
                etLastName.settingUIForNormal()
            }
        })
    }

    override fun onDestroyView() {
        viewModel.nextButtonPressEvent.removeObservers(this)
        super.onDestroyView()
    }

    private val nextButtonObserver = Observer<Boolean> {
        trackEvent(
            SignupEvents.SIGN_UP_NAME.type,
            viewModel.state.firstName + " " + viewModel.state.firstName
        )
        navigate(R.id.emailFragment)
    }
}