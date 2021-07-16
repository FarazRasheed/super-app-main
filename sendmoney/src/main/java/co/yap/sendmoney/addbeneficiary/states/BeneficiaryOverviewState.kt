package co.yap.sendmoney.addbeneficiary.states

import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import co.yap.sendmoney.BR
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.addbeneficiary.interfaces.IBeneficiaryOverview
import co.yap.yapcore.BaseState
import co.yap.yapcore.helpers.Utils.formateIbanString
import co.yap.yapcore.managers.SessionManager

class BeneficiaryOverviewState : BaseState(), IBeneficiaryOverview.State {

    @get:Bindable
    override var beneficiary: Beneficiary? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.beneficiary)
            updateAllFields(beneficiary!!)
        }

    private fun updateAllFields(beneficiary: Beneficiary) {
//A we are not sure about updated response so placing these checks in order to avoid crash

        if (!beneficiary.country.isNullOrEmpty()) country = beneficiary.country!!
        if (!beneficiary.beneficiaryType.isNullOrEmpty()) transferType =
            beneficiary.beneficiaryType!!
        if (!beneficiary.currency.isNullOrEmpty()) currency = beneficiary.currency!!
        if (!beneficiary.title.isNullOrEmpty()) nickName = beneficiary.title!!
        if (!beneficiary.firstName.isNullOrEmpty()) firstName = beneficiary.firstName!!
        if (!beneficiary.lastName.isNullOrEmpty()) lastName = beneficiary.lastName!!
        if (!beneficiary.mobileNo.isNullOrEmpty()) phoneNumber = beneficiary.mobileNo!!
        if (!beneficiary.mobileNo.isNullOrEmpty()) mobile = beneficiary.mobileNo!!
        if (!beneficiary.accountNo.isNullOrEmpty()) accountIban = formateIban(beneficiary)
        if (!beneficiary.mobileNo.isNullOrEmpty()) swiftCode = beneficiary.swiftCode!!

// now again not sure about required field including bank details, so consider it to work on...

        if (!beneficiary.mobileNo.isNullOrEmpty()) countryBankRequirementFieldCode =
            beneficiary.country!!

    }

    private fun formateIban(beneficiary: Beneficiary): String {
        if (beneficiary.accountNo!!.length >= 22) {
            return formateIbanString(beneficiary.accountNo!!).toString()
        } else {
            return beneficiary.accountNo!!
        }
    }
//    fun formateIban(){
//
//    }

    @get:Bindable
    override var flagDrawableResId: Int = -1
        set(value) {
            field = value
            notifyPropertyChanged(BR.flagDrawableResId)
        }

    @get:Bindable
    override var valid: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)

        }


    @get:Bindable
    override var country: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.country)

            beneficiary?.country = field
        }

    @get:Bindable
    override var transferType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferType)
//            beneficiary!!.beneficiaryType=value
        }


    @get:Bindable
    override var currency: String = SessionManager.getDefaultCurrency()
        set(value) {
            field = value
            notifyPropertyChanged(BR.currency)
            beneficiary?.currency = field
        }

    @get:Bindable
    override var nickName: String = "nick name"
        set(value) {
            field = value
            notifyPropertyChanged(BR.nickName)
            beneficiary?.title = field
        }

    @get:Bindable
    override var firstName: String = "first name"
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
            beneficiary?.firstName = field
        }

    @get:Bindable
    override var lastName: String = "last name"
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
            beneficiary?.lastName = field
        }


    @get:Bindable
    override var phoneNumber: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.phoneNumber)
            beneficiary?.mobileNo = field
        }

// for phone number field validation to be work on

    @get:Bindable
    override var drawbleRight: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawbleRight)

        }

    @get:Bindable
    override var mobile: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.mobile)
            if (mobile!!.length < 9) {
                mobileNoLength = 11

            }
            beneficiary?.mobileNo = field
        }

    @get:Bindable
    override var mobileNoLength: Int = 11
        set(value) {
            field = value
            notifyPropertyChanged(BR.mobileNoLength)
        }

    //

    @get:Bindable
    override var accountIban: String = "1234"
        set(value) {
            field = value
            notifyPropertyChanged(BR.accountIban)
//           right now in old beneficiary item there is no field for iban so we need to check

        }

    @get:Bindable
    override var swiftCode: String = "5467"
        set(value) {
            field = value
            notifyPropertyChanged(BR.swiftCode)
            beneficiary?.swiftCode = field
        }

    @get:Bindable
    override var countryBankRequirementFieldCode: String = "11344"
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryBankRequirementFieldCode)

        }

}