package co.yap.modules.onboarding.fragments

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.autoreadsms.MySMSBroadcastReceiver
import co.yap.modules.onboarding.activities.CreatePasscodeActivity
import co.yap.modules.onboarding.interfaces.IPhoneVerification
import co.yap.modules.onboarding.viewmodels.PhoneVerificationViewModel
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.firebase.FirebaseEvents
import co.yap.yapcore.firebase.FirebaseTagManagerModel
import co.yap.yapcore.firebase.firebaseTagManagerEvent
import co.yap.yapcore.helpers.extentions.getOtpFromMessage
import co.yap.yapcore.helpers.extentions.startSmsConsent
import co.yap.yapcore.leanplum.SignupEvents
import co.yap.yapcore.leanplum.trackEvent
import com.google.android.gms.auth.api.phone.SmsRetriever

class PhoneVerificationFragment : OnboardingChildFragment<IPhoneVerification.ViewModel>(),
    IPhoneVerification.View {
    private var intentFilter: IntentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
    private var appSMSBroadcastReceiver: MySMSBroadcastReceiver? = null

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_phone_verification

    override val viewModel: PhoneVerificationViewModel
        get() = ViewModelProviders.of(this).get(PhoneVerificationViewModel::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireContext().firebaseTagManagerEvent(FirebaseTagManagerModel(action = FirebaseEvents.VERIFY_NUMBER.event))
        viewModel.state.reverseTimer(10, requireContext())
    }

    override fun setObservers() {
        context?.startSmsConsent()
        initBroadcast()
        context?.registerReceiver(appSMSBroadcastReceiver, intentFilter)
        viewModel.state.otp.addOnPropertyChangedCallback(stateObserverOtp)
        viewModel.nextButtonPressEvent.observe(this, clickEvent)
    }

    private val clickEvent = Observer<Int> { id ->
        when (id) {
            R.id.done -> {
                viewModel.verifyOtp {
                    viewModel.parentViewModel?.isWaitingList?.value?.let { isWaitingList ->
                        if (isWaitingList) findNavController().navigate(R.id.action_phoneVerificationFragment_to_waitingListFragment) else startActivityForResult(
                            context?.let { CreatePasscodeActivity.newIntent(it, true) },
                            Constants.REQUEST_CODE_CREATE_PASSCODE
                        )
                    } ?: startActivityForResult(
                        context?.let { CreatePasscodeActivity.newIntent(it, true) },
                        Constants.REQUEST_CODE_CREATE_PASSCODE
                    )
                }
            }

        }
    }

    private val stateObserverOtp = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            if (viewModel.isValidOtpLength(viewModel.state.otp.get() ?: "")) {
                viewModel.nextButtonPressEvent.setValue(R.id.done)
            }
        }
    }

    private fun initBroadcast() {
        appSMSBroadcastReceiver =
            MySMSBroadcastReceiver(object : MySMSBroadcastReceiver.OnSmsReceiveListener {
                override fun onReceive(code: Intent?) {
                    startActivityForResult(
                        code,
                        Constants.SMS_CONSENT_REQUEST
                    )
                }
            })
    }

    override fun removeObservers() {
        viewModel.nextButtonPressEvent.removeObservers(this)
        viewModel.state.otp.removeOnPropertyChangedCallback(stateObserverOtp)
        context?.unregisterReceiver(appSMSBroadcastReceiver)
    }

    override fun onDestroyView() {
        removeObservers()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.REQUEST_CODE_CREATE_PASSCODE -> {
                if (null != data) {
                    //trackEvent(TrackEvents.OTP_CODE_ENTERED)
                    viewModel.setPasscode(data.getStringExtra("PASSCODE") ?: "")
                    findNavController().navigate(R.id.action_phoneVerificationFragment_to_nameFragment)
                    trackEvent(SignupEvents.SIGN_UP_PASSCODE_CREATED.type)
                }
            }
            Constants.SMS_CONSENT_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    val message = data?.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    viewModel.state.otp.set(context?.getOtpFromMessage(message ?: "") ?: "")
                }
        }
    }
}
