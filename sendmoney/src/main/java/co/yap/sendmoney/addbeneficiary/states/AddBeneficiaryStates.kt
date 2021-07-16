package co.yap.sendmoney.addbeneficiary.states

import android.graphics.drawable.Drawable
import androidx.databinding.Bindable
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.addbeneficiary.interfaces.IAddBeneficiary
import co.yap.sendmoney.addbeneficiary.viewmodels.AddBeneficiaryViewModel
import co.yap.yapcore.BaseState
import co.yap.yapcore.enums.SendMoneyBeneficiaryType.*
import co.yap.yapcore.helpers.StringUtils

class AddBeneficiaryStates(val viewModel: AddBeneficiaryViewModel) : BaseState(),
    IAddBeneficiary.State {

    @get:Bindable
    override var otpType: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.otpType)
        }

    @get:Bindable
    override var selectedBeneficiaryType: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.selectedBeneficiaryType)
        }

    @get:Bindable
    override var countryCode: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryCode)
        }


    @get:Bindable
    override var flagDrawableResId: Int = -1
        set(value) {
            field = value
            notifyPropertyChanged(BR.flagDrawableResId)
        }

    @get:Bindable
    override var valid: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }


    @get:Bindable
    override var country: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.country)

        }


    @get:Bindable
    override var transferType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.transferType)

        }


    @get:Bindable
    override var currency: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.currency)

        }

    @get:Bindable
    override var nickName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.nickName)
            validate()
        }

    @get:Bindable
    override var firstName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.firstName)
            validate()
        }

    @get:Bindable
    override var lastName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastName)
            validate()
        }

    @get:Bindable
    override var drawbleRight: Drawable? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.drawbleRight)

        }

    @get:Bindable
    override var mobileNo: String = ""
        set(value) {
            field = value.replace(" ", "")
            notifyPropertyChanged(BR.mobile)
            if (mobileNo.length < 9) {
                mobileNoLength = 11
            }
            validate()
        }

    @get:Bindable
    override var mobileNoLength: Int = 11
        set(value) {
            field = value
            notifyPropertyChanged(BR.mobileNoLength)
        }

    @get:Bindable
    override var country2DigitIsoCode: String = "AE"
        set(value) {
            field = value
            notifyPropertyChanged(BR.country2DigitIsoCode)
        }

    @get:Bindable
    override var iban: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.iban)
            validate()
        }

    @get:Bindable
    override var confirmIban: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.confirmIban)
            validate()
        }

    @get:Bindable
    override var id: Int = 11
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }

    @get:Bindable
    override var beneficiaryId: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.beneficiaryId)
        }

    @get:Bindable
    override var accountUuid: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.accountUuid)
        }

    @get:Bindable
    override var beneficiaryType: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.beneficiaryType)
        }

    @get:Bindable
    override var title: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    override var accountNo: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.accountNo)
        }

    @get:Bindable
    override var lastUsedDate: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.lastUsedDate)
        }

    @get:Bindable
    override var swiftCode: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.swiftCode)
        }

    @get:Bindable
    override var bankName: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.bankName)
        }

    @get:Bindable
    override var branchName: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.branchName)
        }

    @get:Bindable
    override var branchAddress: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.branchAddress)
        }

    @get:Bindable
    override var identifierCode1: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.identifierCode1)
        }

    @get:Bindable
    override var identifierCode2: String? = " "
        set(value) {
            field = value
            notifyPropertyChanged(BR.identifierCode2)
        }

    @get:Bindable
    override var countryOfResidence: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.countryOfResidence)
            validate()
        }

    private fun validate() {
        val beneficiaryLength = viewModel.context.resources.getInteger(R.integer.beneficiary_length)
        selectedBeneficiaryType?.let {
            when (valueOf(it)) {
                RMT -> {
                    valid =
                        StringUtils.validateRegix(nickName, "^[a-zA-Z]{1}[a-zA-Z ]{1,50}\$", 2) &&
                                StringUtils.validateRegix(
                                    firstName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                ) &&
                                StringUtils.validateRegix(
                                    lastName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                )
                }
                SWIFT -> {
                    valid =
                        StringUtils.validateRegix(nickName, "^[a-zA-Z]{1}[a-zA-Z ]{1,50}\$", 2) &&
                                StringUtils.validateRegix(
                                    firstName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                ) &&
                                StringUtils.validateRegix(
                                    lastName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                ) && !countryOfResidence.isNullOrBlank()
                }
                DOMESTIC -> {
                    valid =
                        StringUtils.validateRegix(nickName, "^[a-zA-Z]{1}[a-zA-Z ]{1,50}\$", 2) &&
                                StringUtils.validateRegix(
                                    firstName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                ) &&
                                StringUtils.validateRegix(
                                    lastName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                ) && iban.isNotEmpty() &&
                                confirmIban.isNotEmpty() &&
                                StringUtils.isValidIBAN(
                                    iban.replace(" ", ""),
                                    viewModel.parentViewModel?.selectedCountry?.value?.isoCountryCode2Digit
                                ) &&
                                iban == confirmIban
                }
                CASHPAYOUT -> {
                    valid =
                        StringUtils.validateRegix(nickName, "^[a-zA-Z]{1}[a-zA-Z ]{1,50}\$", 2) &&
                                StringUtils.validateRegix(
                                    firstName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                ) &&
                                StringUtils.validateRegix(
                                    lastName,
                                    "^[a-zA-Z]{1}[a-zA-Z ]{1,$beneficiaryLength}\$",
                                    2
                                ) && mobileNo.length > 1
                }
                UAEFTS -> {
                }
                INTERNAL_TRANSFER -> {
                }
                else -> {
                }
            }
        }
    }

}