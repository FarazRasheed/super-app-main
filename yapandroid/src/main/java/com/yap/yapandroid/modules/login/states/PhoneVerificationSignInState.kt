package com.yap.yapandroid.modules.login.states

import android.content.Context
import android.os.CountDownTimer
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.R
import com.yap.yapandroid.modules.login.interfaces.IPhoneVerificationSignIn
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.ThemeColorUtils

class PhoneVerificationSignInState : BaseState(),
    IPhoneVerificationSignIn.State {

    @get:Bindable
    override var color: Int = -1
        set(value) {
            field = value
            notifyPropertyChanged(BR.color)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var validateBtn: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.validateBtn)
        }

    @get:Bindable
    override var timer: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.timer)
        }


    override var otp: ObservableField<String> = ObservableField("")
        set(value) {
            if (isOtpBlocked.get() == false) {
                otp.set(value.get())
                field = value
            }
        }

    @get:Bindable
    override var passcode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.passcode)
        }

    @get:Bindable
    override var username: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.username)
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
                valid = true
                color = ThemeColorUtils.colorPrimaryAttribute(context)
                timer = "00:00"
            }
        }.start()
    }

    override var isOtpBlocked: ObservableField<Boolean> = ObservableField(false)
}
