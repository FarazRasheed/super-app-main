package com.yap.yapandroid.main

import co.yap.modules.onboarding.models.SigningInData
import co.yap.yapcore.IBase
import co.yap.yapcore.helpers.SharedPreferenceManager

interface IMain {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        var signingInData: SigningInData
        val shardPrefs: SharedPreferenceManager
    }

    interface State : IBase.State
}