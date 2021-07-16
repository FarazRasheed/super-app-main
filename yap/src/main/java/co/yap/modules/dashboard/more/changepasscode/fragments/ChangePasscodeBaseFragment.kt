package co.yap.modules.dashboard.more.changepasscode.fragments

import android.os.Bundle
import co.yap.modules.dashboard.more.changepasscode.activities.ChangePasscodeActivity
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class ChangePasscodeBaseFragment<V : IBase.ViewModel<*>> :
    BaseBindingFragment<V>() {

    lateinit var parentActivity: ChangePasscodeActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null && activity is ChangePasscodeActivity) {
            parentActivity = activity as ChangePasscodeActivity
        }
    }
}