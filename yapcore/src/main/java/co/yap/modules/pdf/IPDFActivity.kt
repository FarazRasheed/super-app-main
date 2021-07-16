package co.yap.modules.pdf

import androidx.databinding.ObservableField
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import java.io.File

interface IPDFActivity {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var file: File?
        fun handlePressView(id: Int)
        fun downloadFile(filePath: String, success: (file: File?) -> Unit)
    }

    interface State : IBase.State{
        var hideCross :Boolean?

    }
}