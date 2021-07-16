package co.yap.yapcore.defaults

import co.yap.yapcore.IBase

interface IDefault {
    interface State: IBase.State
    interface ViewModel: IBase.ViewModel<State>
    interface View: IBase.View<ViewModel>
}