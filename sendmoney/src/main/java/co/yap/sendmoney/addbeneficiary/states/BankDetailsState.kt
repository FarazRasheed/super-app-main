package co.yap.sendmoney.addbeneficiary.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import co.yap.sendmoney.BR
import co.yap.sendmoney.addbeneficiary.interfaces.IBankDetails
import co.yap.yapcore.BaseState
import co.yap.yapcore.enums.SendMoneyBeneficiaryType
import co.yap.yapcore.helpers.StringUtils

class BankDetailsState : BaseState(), IBankDetails.State {

    @get:Bindable
    override var selectedBeneficiaryType: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedBeneficiaryType)
        }

    @get:Bindable
    override var buttonText: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.buttonText)
            validate()
        }

    @get:Bindable
    override var bankName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bankName)
            validate()
        }

    @get:Bindable
    override var swiftCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.swiftCode)
            validate()
        }


    @get:Bindable
    override var bankCity: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bankCity)
            validate()
        }


    @get:Bindable
    override var bankBranch: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.bankBranch)
            validate()
        }

    override var isRmt: ObservableField<Boolean> = ObservableField(false)
    override var txtCount: ObservableField<String> = ObservableField("")

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
//            validate()
        }

    @get:Bindable
    override var hideSwiftSection: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.hideSwiftSection)
        }

//    fun validate() {
//        if (!hideSwiftSection) {
//            if (bankBranch.isNotEmpty() && bankCity.isNotEmpty() && bankName.isNotEmpty()) {
//                valid = true
//                notifyPropertyChanged(BR.valid)
//                return
//            }
//        }
//
//        if (!bankBranch.isNullOrEmpty() && !bankCity.isNullOrEmpty() && !swiftCode.isNullOrEmpty() && !bankName.isNullOrEmpty()) {
//            valid = true
//            notifyPropertyChanged(BR.valid)
//        }
//    }


    fun validate() {
        if (!selectedBeneficiaryType.isNullOrEmpty()) {
            when (SendMoneyBeneficiaryType.valueOf(selectedBeneficiaryType!!)) {
                SendMoneyBeneficiaryType.RMT -> {
                }
                SendMoneyBeneficiaryType.SWIFT -> {
                    valid =
                        bankBranch.isNotBlank() && bankCity.isNotBlank() && swiftCode.isNotBlank() && bankName.isNotBlank() && StringUtils.isValidSwift(swiftCode)
                }
                SendMoneyBeneficiaryType.DOMESTIC -> {
                }
                SendMoneyBeneficiaryType.CASHPAYOUT -> {
                }
                SendMoneyBeneficiaryType.UAEFTS -> {
                }
                SendMoneyBeneficiaryType.INTERNAL_TRANSFER -> {
                }
            }
        }
    }

}