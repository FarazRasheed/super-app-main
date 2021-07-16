package co.yap.modules.onboarding.states

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.R
import co.yap.modules.onboarding.interfaces.IPhoneVerification
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.ThemeColorUtils

open class PhoneVerificationState(application: Application) : BaseState(),
    IPhoneVerification.State {
    @get:Bindable
    override var verificationTitle: String = "I am your title"
        set(value) {
            field = value
            notifyPropertyChanged(BR.verificationTitle)
        }

    @get:Bindable
    override var verificationDescription: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.verificationDescription)
        }

    val mContext = application.applicationContext
    val mobileNumber: Array<String?> = arrayOfNulls(1)

    override var otp: ObservableField<String> = ObservableField("")

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var validResend: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.validResend)
        }

    @get:Bindable
    override var timer: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.timer)
            validate()
        }


    @get:Bindable
    override var color: Int = mContext.resources.getColor(R.color.disabled)
        set(value) {
            field = value
            notifyPropertyChanged(BR.color)
        }

    private fun validate(): Boolean {
        var validateOtp: Boolean = false
        return if (isOtpBlocked.get() == false) {
            if (!otp.get().isNullOrEmpty() && otp.get()?.length == 6) {
                validateOtp = true
                valid = true
            }
            validateOtp
        } else
            validateOtp
    }

    override fun reverseTimer(Seconds: Int, context: Context) {
        color = context.resources.getColor(R.color.disabled)
        object : CountDownTimer((Seconds * 1000 + 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000).toInt()
                val minutes = seconds / 60
                seconds %= 60
                val timerMsg: String
                if (seconds == 10) {
                    timerMsg = "00:$seconds"
                } else {
                    timerMsg = "00:0$seconds"
                }
                timer = timerMsg
            }

            override fun onFinish() {
                validResend = true
                color = ThemeColorUtils.colorPrimaryAttribute(context)
                timer = "00:00"
            }
        }.start()
    }

    override var isOtpBlocked: ObservableField<Boolean> = ObservableField(false)
}
