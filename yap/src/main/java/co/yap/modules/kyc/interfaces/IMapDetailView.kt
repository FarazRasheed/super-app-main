package co.yap.modules.kyc.interfaces

import android.widget.TextView
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IMapDetailView {
    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressOnNext(id: Int)
        fun handlePressOnSelectLocation(id: Int)
        fun onEditorActionListener(): TextView.OnEditorActionListener
    }

    interface State : IBase.State {
        var headingTitle: String
        var subHeadingTitle: String
        var addressField: String
        var landmarkField: String
        var locationBtnText: String
        var valid: Boolean
    }
}