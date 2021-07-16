package co.yap.modules.onboarding.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.onboarding.interfaces.IMeetingConfirmation
import co.yap.modules.onboarding.viewmodels.MeetingConfirmationViewModel
import co.yap.modules.others.fragmentpresenter.activities.FragmentPresenterActivity
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.firebase.FirebaseEvents
import co.yap.yapcore.firebase.FirebaseTagManagerModel
import co.yap.yapcore.firebase.firebaseTagManagerEvent
import co.yap.yapcore.managers.SessionManager

class MeetingConfirmationFragment : BaseBindingFragment<IMeetingConfirmation.viewModel>() {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_meeting_confirmation

    override val viewModel: IMeetingConfirmation.viewModel
        get() = ViewModelProviders.of(this).get(MeetingConfirmationViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().firebaseTagManagerEvent(FirebaseTagManagerModel(action = FirebaseEvents.DELIVERY_STARTED.event))
        SessionManager.getAccountInfo()
        viewModel.goToDashboardButtonPressEvent.observe(this, Observer {
            if (activity is FragmentPresenterActivity) {
                setIntentData()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.goToDashboardButtonPressEvent.removeObservers(this)
    }

    override fun onBackPressed(): Boolean {
        if (activity is FragmentPresenterActivity) {
            return true
        }
        return false
    }

    private fun setIntentData() {
        val intent = Intent()
        intent.putExtra("dashboard", true)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }
}