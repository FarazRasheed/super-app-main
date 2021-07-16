package co.yap.modules.kyc.states

import android.app.Application
import android.content.Context
import androidx.databinding.Bindable
import co.yap.BR
import co.yap.R
import co.yap.modules.kyc.interfaces.IMapDetailView
import co.yap.translation.Translator
import co.yap.yapcore.BaseState

class MapDetailViewState(application: Application) : BaseState(), IMapDetailView.State {

    val mContext: Context = application.applicationContext

    @get:Bindable
    override var headingTitle: String =
        Translator.getString(application, R.string.screen_meeting_location_display_text_title)
        get() = field
        set(value) {

            field = value
            notifyPropertyChanged(BR.headingTitle)
        }

    @get:Bindable
    override var subHeadingTitle: String =
        Translator.getString(application, R.string.screen_meeting_location_display_text_subtitle)
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.subHeadingTitle)
        }

    @get:Bindable
    override var addressField: String = ""
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.addressField)
        }

    @get:Bindable
    override var landmarkField: String = ""
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.landmarkField)
        }

    @get:Bindable
    override var locationBtnText: String =
        Translator.getString(application, R.string.screen_meeting_location_button_confirm_location)
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.locationBtnText)
        }
    @get:Bindable
    override var valid: Boolean = true
        get() = validateAddress()

    private fun validateAddress(): Boolean {
        if (!addressField.isNullOrEmpty() && addressField.length >= 2 && !landmarkField.isNullOrEmpty() && landmarkField.length >= 2 /*&& addressField.length <= 100*/) {

            return true
        } else {
            return false

        }
    }

}