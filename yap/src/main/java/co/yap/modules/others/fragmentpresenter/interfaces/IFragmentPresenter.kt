package co.yap.modules.others.fragmentpresenter.interfaces

import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IFragmentPresenter {

    interface State : IBase.State {


    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent

    }

    interface View : IBase.View<ViewModel> {

    }
}