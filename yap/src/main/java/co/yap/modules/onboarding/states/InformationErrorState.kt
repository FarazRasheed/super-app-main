package co.yap.modules.onboarding.states

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.BR
import co.yap.modules.onboarding.interfaces.IInformationError
import co.yap.yapcore.BaseState

class InformationErrorState(application: Application) : BaseState(), IInformationError.State {
    private val mContext = application.applicationContext
    @get:Bindable
    override var errorTitle: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.errorTitle)
        }

    @get:Bindable
    override var errorGuide: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.errorGuide)
        }
    @get:Bindable
    override var buttonTitle: String=""
        set(value) {
            field=value
            notifyPropertyChanged(BR.buttonTitle)
        }

    override var isUSACitizen: ObservableField<Boolean> = ObservableField(false)
}
