package co.yap.modules.others.note.interfaces

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface ITransactionNote {

    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
        fun addEditNote(transactionId: String?, transactionDetail: String?)
        val addEditNoteSuccess: MutableLiveData<Boolean>
    }

    interface State : IBase.State {
        var tvEnableState: ObservableField<Boolean>
        var noteValue: ObservableField<String>
        var addEditNote: ObservableField<String>
        var toolbarVisibility: ObservableBoolean
        var leftButtonVisibility: ObservableBoolean
        var rightTitleVisibility: ObservableBoolean
        var rightTitle: ObservableField<String>
    }
}