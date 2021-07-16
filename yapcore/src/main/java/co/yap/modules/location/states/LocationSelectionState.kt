package co.yap.modules.location.states

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import co.yap.modules.location.interfaces.ILocationSelection
import co.yap.yapcore.BaseState
import co.yap.yapcore.R

class LocationSelectionState(application: Application) : BaseState(), ILocationSelection.State {
    private val mContext: Context = application.applicationContext

    override var headingTitle: ObservableField<String> = ObservableField("")
    override var subHeadingTitle: ObservableField<String> = ObservableField("")
    override var placeTitle: ObservableField<String> = ObservableField("")
    override var placeSubTitle: ObservableField<String> = ObservableField("")
    override var placePhoto: ObservableField<Bitmap> = ObservableField(
        BitmapFactory.decodeResource(
            mContext.resources,
            R.drawable.location_place_holder
        )
    )
    override var isShowLocationCard: ObservableField<Boolean> = ObservableField(false)
    override var showTermsCondition: ObservableField<Boolean> = ObservableField(false)
    override var isLocationInAllowedCountry: ObservableField<Boolean> = ObservableField(false)
    override var addressSubtitle: ObservableField<String> = ObservableField("")
    override var addressTitle: ObservableField<String> = ObservableField("")
    override var city: ObservableField<String> = ObservableField("Select")
    override var isTermsChecked: ObservableField<Boolean> = ObservableField(false)
    override var valid: ObservableField<Boolean> = ObservableField(false)
    override var isUnNamed: ObservableField<Boolean> = ObservableField(false)
    override var isOnBoarding: ObservableField<Boolean> = ObservableField(false)

    @get:Bindable
    override var toolbarVisibility: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.toolbarVisibility)
        }

}