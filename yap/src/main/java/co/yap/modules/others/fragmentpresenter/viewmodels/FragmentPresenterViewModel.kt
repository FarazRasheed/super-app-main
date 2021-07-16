package co.yap.modules.others.fragmentpresenter.viewmodels

import android.app.Application
import co.yap.modules.others.fragmentpresenter.interfaces.IFragmentPresenter
import co.yap.modules.others.fragmentpresenter.states.FragmentPresenterState
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class FragmentPresenterViewModel(application: Application) :
    BaseViewModel<IFragmentPresenter.State>(application), IFragmentPresenter.ViewModel {


    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val state: FragmentPresenterState = FragmentPresenterState()


}