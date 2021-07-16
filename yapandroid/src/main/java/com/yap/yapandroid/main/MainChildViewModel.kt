package com.yap.yapandroid.main

import android.app.Application
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.IBase

abstract class MainChildViewModel<S : IBase.State>(application: Application) :
    BaseViewModel<S>(application) {

    var parentViewModel: IMain.ViewModel? = null
}