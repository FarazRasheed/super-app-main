package co.yap.sendmoney.editbeneficiary.states

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.countryutils.country.Country
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.BR
import co.yap.sendmoney.editbeneficiary.interfaces.IEditBeneficiary
import co.yap.yapcore.BaseState
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.managers.SessionManager

class EditBeneficiaryStates(val application: Application) : BaseState(), IEditBeneficiary.State {
    override var rightButtonText: ObservableField<String> = ObservableField("Cancel")


    @get:Bindable
    override var country: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.country)
        }
    @get:Bindable
    override var transferType: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferType)
            // beneficiary?.beneficiaryType = field
        }
    @get:Bindable
    override var currency: String? = SessionManager.getDefaultCurrency()
        set(value) {
            field = value
            notifyPropertyChanged(BR.currency)
            beneficiary?.currency = field
        }
    @get:Bindable
    override var nickName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.nickName)
            beneficiary?.title = field
            validate()
        }


    @get:Bindable
    override var firstName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
            beneficiary?.firstName = field
        }

    @get:Bindable
    override var lastName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
            beneficiary?.lastName = field
        }
    @get:Bindable
    override var phoneNumber: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.phoneNumber)
            beneficiary?.mobileNo = field?.replace(" ", "")
        }
    @get:Bindable
    override var accountNumber: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.accountNumber)
            //beneficiary?.accountNo = field
        }
    @get:Bindable
    override var swiftCode: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.swiftCode)
            //beneficiary?.swiftCode = field
        }
    @get:Bindable
    override var countryBankRequirementFieldCode: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryBankRequirementFieldCode)
        }
    @get:Bindable
    override var needOverView: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.needOverView)
        }
    @get:Bindable
    override var needIban: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.needIban)
        }
    @get:Bindable
    override var showIban: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.showIban)
        }

    @get:Bindable
    override var beneficiary: Beneficiary? = Beneficiary()
        set(value) {
            field = value
            notifyPropertyChanged(BR.beneficiary)
            updateAllFields(beneficiary)
        }
    @get:Bindable
    override var countryCode: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryCode)

        }

    @get:Bindable
    override var valid: Boolean? = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    @get:Bindable
    override var selectedCountryOfResidence: Country? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedCountryOfResidence)
            beneficiary?.countryOfResidenceName = field?.getName()
            beneficiary?.countryOfResidence = field?.isoCountryCode2Digit
            validate()
        }

    private fun updateAllFields(beneficiary: Beneficiary?) {

        beneficiary?.let { it ->
            it.country?.let {
                country = it
                countryCode = it
            }
            it.lastName?.let { lastName = it }
            it.title?.let { nickName = it }
            it.firstName?.let { firstName = it }
            it.mobileNo?.let {
                phoneNumber =
                    Utils.getPhoneWithoutCountryCode(Utils.getDefaultCountryCode(application), it)
                //phoneNumber = it
            }

            it.accountNo?.let {
                accountNumber = it
//                if (it.length >= 22) {
//                    val no = StringBuilder()
//                    it.forEachIndexed{index, c ->
//                        no.append(c)
//                        if(index%4==0)
//                        {
//                            no.append(" ")
//                        }
//                    }
//                    accountNumber = Utils.formateIbanString(it)
//                } else {
//                    accountNumber = it
//                }
            }
            it.swiftCode?.let { swiftCode = it }
            it.beneficiaryType?.let { transferType = it }
        }
    }

    private fun validate() {
        valid = when (transferType) {
            SendMoneyBeneficiaryType.SWIFT.type, SendMoneyBeneficiaryType.RMT.type -> {
                !nickName.isNullOrBlank() && selectedCountryOfResidence != null
            }
            else -> !nickName.isNullOrBlank()
        }

    }

}