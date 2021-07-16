package co.yap.modules.onboarding.viewmodels

import android.app.Application
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import co.yap.modules.onboarding.interfaces.IName
import co.yap.modules.onboarding.states.NameState
import co.yap.yapcore.SingleLiveEvent

class NameViewModel(application: Application) : OnboardingChildViewModel<IName.State>(application), IName.ViewModel {

    override val state: NameState = NameState(application)
    override val nextButtonPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    override fun onResume() {
        super.onResume()
        setProgress(60)
    }

    override fun handlePressOnNext() {
        parentViewModel?.onboardingData?.firstName = state.firstName
        parentViewModel?.onboardingData?.lastName = state.lastName
        nextButtonPressEvent.value = true
    }

    override fun onEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (state.valid) {
                    handlePressOnNext()
                }
            }
            false
        }
    }
}