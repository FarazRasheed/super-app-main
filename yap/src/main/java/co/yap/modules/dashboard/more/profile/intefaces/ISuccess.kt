package co.yap.modules.dashboard.more.profile.intefaces

import android.graphics.Bitmap
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface ISuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State> {
        fun handlePressOnDoneButton()
        fun placesApiCall(photoPlacedId: String,success: () -> Unit)
        val buttonClickEvent: SingleClickEvent
    }

    interface State : IBase.State {
        var topMainHeading: String
        var topSubHeading: String
        var placeBitmap: Bitmap?
        var buttonTitle: String
        var address1: String
        var address2: String

    }
}