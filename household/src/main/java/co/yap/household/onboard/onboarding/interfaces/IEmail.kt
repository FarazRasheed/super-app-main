package co.yap.household.onboard.onboarding.interfaces

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent


interface IEmail {

    interface View : IBase.View<ViewModel>

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        var hasDoneAnimation: Boolean
        var onEmailVerifySuccess: MutableLiveData<Boolean>
        val animationStartEvent: MutableLiveData<Boolean>
        fun handlePressOnView(id: Int)
        fun onEditorActionListener(): TextView.OnEditorActionListener
        fun postDemographicData()
        fun sendVerificationEmail()
        fun stopTimer()
    }

    interface State : IBase.State {
        var email: String
        var drawbleRight: Drawable?
        var emailError: String
        var emailHint: String
        var valid: Boolean

        //textwatcher
        var cursorPlacement: Boolean
        var refreshField: Boolean
        var setSelection: Int
        var handleBackPress: Int
        var twoWayTextWatcher: String
        var emailTitle: String
        var emailVerificationTitle: String
        var emailBtnTitle: String
        var deactivateField: Boolean

        var verificationCompleted: Boolean
    }
}