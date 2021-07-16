package co.yap.modules.dashboard.more.notification.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.modules.dashboard.more.notification.viewmodels.NotificationsBaseViewModel
import co.yap.modules.dashboard.more.notification.viewmodels.NotificationsViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class NotificationsBaseFragment<V : IBase.ViewModel<*>> :
    BaseBindingFragment<V>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel is NotificationsBaseViewModel<*>) {
            (viewModel as NotificationsBaseViewModel<*>).parentViewModel =
                ViewModelProviders.of(activity!!).get(NotificationsViewModel::class.java)
        }
    }
}