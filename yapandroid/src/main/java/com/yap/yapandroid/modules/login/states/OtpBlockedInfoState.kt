package com.yap.yapandroid.modules.login.states

import androidx.databinding.ObservableField
import com.yap.yapandroid.modules.login.interfaces.IOtpBlockedInfo
import co.yap.yapcore.BaseState

class OtpBlockedInfoState : BaseState(), IOtpBlockedInfo.State {
    override val userFirstName: ObservableField<String> = ObservableField()
    override val helpPhoneNo: ObservableField<String> = ObservableField()
    override var token: ObservableField<String> = ObservableField()

}