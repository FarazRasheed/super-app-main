package co.yap.modules.dashboard.more.profile.states

import android.graphics.Bitmap
import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.more.profile.intefaces.ISuccess
import co.yap.translation.Strings
import co.yap.yapcore.BaseState

class SuccessState : BaseState(), ISuccess.State {
    @get:Bindable
    override var topMainHeading: String = Strings.screen_email_address_success_display_text_title
        set(value) {
            field = value
            notifyPropertyChanged(BR.topMainHeading)
        }

    @get:Bindable
    override var topSubHeading: String = "Your email address has been changed to newemail@website.com"
        set(value) {
            field = value
            notifyPropertyChanged(BR.topSubHeading)
        }

    @get:Bindable
    override var address1: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.address1)
        }

    @get:Bindable
    override var address2: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.address2)
        }

    @get:Bindable
    override var buttonTitle: String = "Done"
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonTitle)
        }

    @get:Bindable
    override var placeBitmap: Bitmap? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.placeBitmap)
        }

}