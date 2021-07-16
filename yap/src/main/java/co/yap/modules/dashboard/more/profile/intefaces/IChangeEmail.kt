package co.yap.modules.dashboard.more.profile.intefaces

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.helpers.SharedPreferenceManager

interface IChangeEmail {
    interface View : IBase.View<ViewModel> {
        fun setObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        fun onHandlePressOnNextButton()
        val success: MutableLiveData<Boolean>
        fun changeEmail()
        val changeEmailSuccessEvent: SingleClickEvent
        val sharedPreferenceManager: SharedPreferenceManager
    }

    interface State : IBase.State {
        var newEmail: String
        var newConfirmEMail: String

        var backgroundNew: Drawable?
        var backgroundConfirm: Drawable?


        var drawableConfirm: Drawable?
        var drawableNew: Drawable?

        //error
        var errorMessage: String
        var valid: Boolean
    }
}