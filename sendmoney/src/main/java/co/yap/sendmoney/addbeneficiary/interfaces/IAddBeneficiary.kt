package co.yap.sendmoney.addbeneficiary.interfaces

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IAddBeneficiary {

    interface State : IBase.State {
        var country: String
        var countryCode: String
        var transferType: String
        var currency: String
        var nickName: String
        var firstName: String
        var lastName: String
        var flagDrawableResId: Int
        var mobileNo: String
        var iban: String
        var confirmIban: String
        var drawbleRight: Drawable?
        var mobileNoLength: Int
        var valid: Boolean
        var country2DigitIsoCode: String

        var id: Int
        var beneficiaryId: String?
        var accountUuid: String?
        var beneficiaryType: String?
        var title: String?
        var accountNo: String?
        var lastUsedDate: String?
        var swiftCode: String?
        var bankName: String?
        var branchName: String?
        var branchAddress: String?
        var identifierCode1: String?
        var identifierCode2: String?
        var selectedBeneficiaryType: String?
        var otpType: String?
        var countryOfResidence: String?

    }

    interface ViewModel : IBase.ViewModel<State> {
        var clickEvent: SingleClickEvent
        fun handlePressOnAddNow(id: Int)
        fun handlePressOnAddDomestic(id: Int)
        fun addCashPickupBeneficiary()
        fun validateBeneficiaryDetails(objBeneficiary: Beneficiary, otpType: String)
        fun addDomesticBeneficiary(objBeneficiary: Beneficiary?)
        var addBeneficiarySuccess: MutableLiveData<Boolean>
        var beneficiary: Beneficiary?
        fun createOtp(action: String)
        val otpCreateObserver: MutableLiveData<Boolean>

    }

    interface View : IBase.View<ViewModel>
}