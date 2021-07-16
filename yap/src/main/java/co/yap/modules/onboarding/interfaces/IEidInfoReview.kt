package co.yap.modules.onboarding.interfaces

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import co.yap.widgets.State
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import com.digitify.identityscanner.docscanner.models.IdentityScannerResult

interface IEidInfoReview {

    interface State : IBase.State {
        var firstName: String
        var middleName: String
        var lastName: String
        var nationality: String
        var dateOfBirth: String
        var gender: String
        var expiryDate: String
        var citizenNumber: String
        var caption: String
        var fullNameValid: Boolean
        var nationalityValid: Boolean
        var dateOfBirthValid: Boolean
        var genderValid: Boolean
        var expiryDateValid: Boolean
        var valid: Boolean
        var isShowMiddleName: ObservableBoolean
        var isShowLastName: ObservableBoolean
    }

    interface View : IBase.View<ViewModel> {
        fun showUnderAgeAlert()
        fun showExpiredEidAlert()
        fun showInvalidEidAlert()
        fun showUSACitizenAlert()
        fun openCardScanner()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val eventRescan: Int get() = 1
        val eventErrorUnderAge: Int get() = 2
        val eventErrorExpiredEid: Int get() = 3
        val eventErrorFromUsa: Int get() = 4
        val eventNextWithError: Int get() = 5
        val eventNext: Int get() = 6
        val eventFinish: Int get() = 7
        val eventErrorInvalidEid: Int get() = 8
        val eventAlreadyUsedEid: Int get() = 1041
        val eventEidUpdate: Int get() = 9
        val eventCitizenNumberIssue: Int get() = 10
        val eventEidExpiryDateIssue: Int get() = 11
        var eidStateLiveData: MutableLiveData<co.yap.widgets.State>
        val clickEvent: SingleClickEvent
        fun handlePressOnView(id: Int)
        fun updateLabels(title: String, body: String)
        fun onEIDScanningComplete(result: IdentityScannerResult)
        var sanctionedCountry: String
        var sanctionedNationality: String
        var errorTitle: String
        var errorBody: String
    }
}