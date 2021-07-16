package co.yap.modules.dashboard

import android.graphics.drawable.Drawable
import androidx.databinding.ObservableField
import co.yap.networking.cards.requestdtos.ChangeCardPinRequest
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IChangePin {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun handlePressView(id: Int)
        fun changePinRequest(request: ChangeCardPinRequest, success: () -> Unit)
    }

    interface State : IBase.State {
        var oldPin: String
        var newPin: String
        var confirmNewPin: String
        var pinFieldBackground: ObservableField<Drawable?>
        var pinFieldErrorIcon: ObservableField<Drawable?>
        var pinFieldBackgroundForNew: ObservableField<Drawable?>
        var pinFieldErrorIconForNew: ObservableField<Drawable?>
        var pinFieldBackgroundForConfirmNew: ObservableField<Drawable?>
        var pinFieldErrorIconConfirmNew: ObservableField<Drawable?>
        var errorMessageForPrevious: ObservableField<String>
        var errorMessageForNewConfiem: ObservableField<String>
        var errorMessageForNewPin: ObservableField<String>
        var isButtonEnabled: ObservableField<Boolean>


    }
}