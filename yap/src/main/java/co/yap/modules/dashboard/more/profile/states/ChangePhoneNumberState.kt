package co.yap.modules.dashboard.more.profile.states

import android.app.Application
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.databinding.Bindable
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.profile.intefaces.IChangePhoneNumber
import co.yap.widgets.mobile.CountryCodePicker
import co.yap.yapcore.BaseState

class ChangePhoneNumberState(application: Application) : BaseState(), IChangePhoneNumber.State {

    val context = application.applicationContext
    @get:Bindable
    override var background: Drawable? = context.getDrawable(R.drawable.bg_phone_number_under_line)
        set(value) {
            field = value
            notifyPropertyChanged(BR.background)
        }


    @get:Bindable
    override var drawbleRight: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawbleRight)

        }

    @get:Bindable
    override var countryCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryCode)
        }

    @get:Bindable
    override var mobile: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.mobile)
            if (mobile.length < 9) {
                mobileNoLength = 11
            }
            errorMessage = ""

        }

    @get:Bindable
    override var etMobileNumber: EditText? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.etMobileNumber)
            findKeyBoardFocus()
            registerCarrierEditText()
        }


    @get:Bindable
    override var mobileNoLength: Int = 11
        set(value) {
            field = value
            notifyPropertyChanged(BR.mobileNoLength)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var errorMessage: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.errorMessage)
            if (errorMessage.isNotEmpty()) {
                background = context.getDrawable(R.drawable.bg_edit_text_red_under_line)
                drawbleRight =
                    context!!.resources.getDrawable(co.yap.yapcore.R.drawable.ic_error, null)
            }else{
                background = context.getDrawable(R.drawable.bg_phone_number_under_line)
                drawbleRight = null
            }
        }


    private fun registerCarrierEditText() {

        val ccpLoadNumber: CountryCodePicker? = CountryCodePicker(context)
        ccpLoadNumber!!.registerCarrierNumberEditText(this.etMobileNumber!!)

        ccpLoadNumber.setPhoneNumberValidityChangeListener(object :
            CountryCodePicker.PhoneNumberValidityChangeListener {
            override fun onValidityChanged(isValidNumber: Boolean) {
                if (isValidNumber) {
                    mobileNoLength = 11
                    if (mobile.length == 11) {
                        setSuccessUI()
                        drawbleRight =
                            context!!.resources.getDrawable(co.yap.yapcore.R.drawable.path, null)
                        valid = true

                    } else {
                        setSuccessUI()
                    }
                } else {
//                    mobileNoLength=9
                    setSuccessUI()
                    if (mobile.toString().replace(" ", "").trim().length >= 9) {
                        setErrorLayout()

                    } else {
                        setSuccessUI()

                    }

                }
            }
        })
    }

    private fun findKeyBoardFocus() {
        etMobileNumber!!.getViewTreeObserver().addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    /* if (etMobileNumber!!.isFocused()) {
                         if (!keyboardShown(etMobileNumber!!.getRootView())) {
 //                            activeFieldValue = false
                         } else {
 //                            activeFieldValue = true
                         }
                     }*/
                    return
                }
            })
    }

    fun keyboardShown(rootView: View): Boolean {
        val softKeyboardHeight = 100
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - r.bottom
        return heightDiff > softKeyboardHeight * dm.density
    }

    private fun setSuccessUI() {
        drawbleRight = null
        background =
            context!!.resources.getDrawable(R.drawable.bg_edit_text_active_under_line, null)
//        activeFieldValue = true
//        mobileError = ""
        valid = false

    }

    private fun setErrorLayout() {
        drawbleRight = context.getDrawable(R.drawable.ic_error)
        background = context!!.resources.getDrawable(R.drawable.bg_edit_text_red_under_line, null)
    }
}