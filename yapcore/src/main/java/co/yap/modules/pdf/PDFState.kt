package co.yap.modules.pdf

import androidx.databinding.Bindable
import co.yap.yapcore.BR
import co.yap.yapcore.BaseState

open class PDFState : BaseState(), IPDFActivity.State {

    @get:Bindable
    override var hideCross: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.hideCross)
        }

}
