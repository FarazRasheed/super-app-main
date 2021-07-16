package co.yap.modules.dashboard.store.household.onboarding.interfaces

import android.graphics.drawable.Drawable
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseHoldUserContact {

    interface State : IBase.State {

        var confirmMobileNumber: String
        var countryCode: String
        var mobile: String
        var drawbleRight: Drawable?
        var drawbleRightConfirmMobile: Drawable?
        var background: Drawable?
        var backgroundConfirmMobile: Drawable?
        var mobileNoLength: Int
        var confirmMobileNoLength: Int
        var etMobileNumber: EditText?
        var etMobileNumberConfirmMobile: EditText?
        var errorMessage: String
        var errorMessageConfirmMobile: String

        var valid: Boolean

    }

    interface ViewModel : IBase.ViewModel<State> {

        val clickEvent: SingleClickEvent
        var verifyMobileSuccess:MutableLiveData<Boolean>
        var verifyMobileError:MutableLiveData<String>
        fun handlePressOnAdd(id: Int)
        fun handlePressOnBackButton()
        fun getCcp(etMobileNumber: EditText)
        fun getConfirmCcp(etMobileNumber: EditText)
        fun verifyMobileNumber()

//        val backButtonPressEvent: SingleLiveEvent<Boolean>
    }

    interface View : IBase.View<ViewModel>

}