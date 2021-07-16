package co.yap.modules.dashboard.more.home.interfaces

import androidx.databinding.ObservableField
import co.yap.databinding.FragmentMoreHomeBinding
import co.yap.modules.dashboard.more.home.models.MoreOption
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IMoreHome {
    interface State : IBase.State{
        var image: ObservableField<String>
        var initials: ObservableField<String>
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
        fun handlePressOnYAPforYou(id: Int)
        fun getMoreOptions(): MutableList<MoreOption>
    }

    interface View : IBase.View<ViewModel>{
        fun getBinding(): FragmentMoreHomeBinding
        fun setObservers()
        fun removeObservers()

    }
}