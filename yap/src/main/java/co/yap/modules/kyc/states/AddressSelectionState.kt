package co.yap.modules.kyc.states

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.View.GONE
import androidx.databinding.Bindable
import co.yap.BR
import co.yap.R
import co.yap.modules.kyc.interfaces.IAddressSelection
import co.yap.translation.Translator
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.ThemeColorUtils
import com.google.android.gms.maps.GoogleMap


class AddressSelectionState(application: Application) : BaseState(), IAddressSelection.State {


    val mContext: Context = application.applicationContext
    val VISIBLE: Int = 0x00000000

    @get:Bindable
    override var googleMap: GoogleMap? = null
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.googleMap)
        }

    @get:Bindable
    override var placePhoto: Bitmap? =
        BitmapFactory.decodeResource(mContext.resources, R.drawable.location_place_holder)
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.placePhoto)
        }

    @get:Bindable
    override var placeTitle: String = ""
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.placeTitle)
        }

    @get:Bindable
    override var placeSubTitle: String = ""
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.placeSubTitle)
        }

    @get:Bindable
    override var closeCard: Boolean = false
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.closeCard)
        }

    @get:Bindable
    override var isMapOnScreen: Boolean = false
        set(value) {
            field = value
        }

    @get:Bindable
    override var isFromPersonalDetailView: Boolean = false
        set(value) {
            field = value
        }

    @get:Bindable
    override var isFromPhysicalCardsLayout: Boolean = false
        set(value) {
            field = value
        }


    @get:Bindable
    override var errorVisibility: Int = VISIBLE
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.handleBackPress)
        }

    @get:Bindable
    override var checkBoxLayoutVisibility: Int = GONE
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.checkBoxLayoutVisibility)
        }

    @get:Bindable
    override var errorChecked: Boolean = false
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorChecked)
        }

    @get:Bindable
    override var cardView: Boolean = false
        get() = field
        set(value) {
            if (value) {
                errorVisibility = VISIBLE
                notifyPropertyChanged(BR.errorVisibility)
            }
            field = value
            errorChecked = value
            notifyPropertyChanged(BR.cardView)
        }

    @get:Bindable
    override var confirmLocationButton: Boolean = false
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.confirmLocationButton)
        }

    @get:Bindable
    override var headingTitle: String =
        Translator.getString(application, R.string.screen_meeting_location_display_text_title)
        set(value) {

            field = value
            notifyPropertyChanged(BR.headingTitle)
        }

    @get:Bindable
    override var subHeadingTitle: String =
        Translator.getString(application, R.string.screen_meeting_location_display_text_subtitle)
        set(value) {
            field = value
            notifyPropertyChanged(BR.subHeadingTitle)
        }

    @get:Bindable
    override var nextActionBtnText: String =
        Translator.getString(application, R.string.screen_phone_number_button_send)
        set(value) {
            field = value
            notifyPropertyChanged(BR.nextActionBtnText)
        }

    @get:Bindable
    override var addressField: String = ""
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.addressField)

            if (!value.isNullOrEmpty()) {
                checkBoxLayoutVisibility = VISIBLE
                addressTitlesColor = mContext.resources.getColor(R.color.greyDark)
                notifyPropertyChanged(BR.addressTitlesColor)
            }
        }

    @get:Bindable
    override var landmarkField: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.landmarkField)
            if (!value.isNullOrEmpty()) {
                onDrawableClick = true
                setDrawable = mContext.resources.getDrawable(R.drawable.ic_clear_field)
                landMarkTitleColor = mContext.resources.getColor(R.color.greyDark)
            } else {
                landMarkTitleColor = ThemeColorUtils.colorPrimaryDarkAttribute(mContext)

                onDrawableClick = false
            }
        }


    @get:Bindable
    override var setDrawable: Drawable? = null
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.setDrawable)
        }

    @get:Bindable
    override var addressTitlesColor: Int = ThemeColorUtils.colorPrimaryDarkAttribute(mContext)
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.addressTitlesColor)
        }

    @get:Bindable
    override var landMarkTitleColor: Int = ThemeColorUtils.colorPrimaryDarkAttribute(mContext)
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.landMarkTitleColor)
        }


    @get:Bindable
    override var checked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.checked)
            valid = validateAddress()
        }

    @get:Bindable
    override var locationBtnText: String =
        Translator.getString(application, R.string.screen_meeting_location_button_confirm_location)
        set(value) {
            field = value
            notifyPropertyChanged(BR.locationBtnText)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var onDrawableClick: Boolean = false
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.onDrawableClick)
        }

    private fun validateAddress(): Boolean {
        return addressField.isNotEmpty() && addressField.length >= 2 && checked
    }

}