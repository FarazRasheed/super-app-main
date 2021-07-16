package co.yap.networking.customers.responsedtos

import android.os.Parcelable
import co.yap.networking.customers.responsedtos.sendmoney.Currency
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountInfo(
    @SerializedName("creationDate")
    var creationDate: String? = null,
    @SerializedName("createdBy")
    var createdBy: String? = "",
    @SerializedName("updatedDate")
    var updatedDate: String? = "",
    @SerializedName("uuid")
    var uuid: String? = "",
    @SerializedName("defaultProfile")
    var defaultProfile: Boolean? = false,
    @SerializedName("accountType")
    var accountType: String? = "",
    @SerializedName("isActive")
    var isActive: String,
    @SerializedName("accountNo")
    var accountNo: String? = null,
    @SerializedName("fssRequestRefNo")
    var fssRequestRefNo: String,
    @SerializedName("packageName")
    var packageName: String?,
    @SerializedName("status")
    var status: String,
    @SerializedName("onBoardingStatus")
    var onBoardingStatus: String,
    @SerializedName("customer")
    var currentCustomer: Customer,
    @SerializedName("bank")
    var bank: Bank? = Bank(),
    @SerializedName("currency")
    var currency: Currency = Currency(),
    @SerializedName("notificationStatuses")
    var notificationStatuses: String,
    @SerializedName("toClose")
    var toClose: Boolean? = false,
    @SerializedName("noOfSubAccounts")
    var noOfSubAccounts: Int? = 0,
    @SerializedName("parentUUID")
    var parentUUID: String? = null,
    @SerializedName("parentAccount")
    var parentAccount: AccountInfo? = null,
    @SerializedName("workItemNo")
    var workItemNo: String? = "",
    @SerializedName("partnerBankStatus")
    var partnerBankStatus: String? = null,
    @SerializedName("active")
    var active: Boolean? = false,
    @SerializedName("soleProprietary")
    var soleProprietary: Boolean,
    @SerializedName("iban")
    var iban: String? = null,
    @SerializedName("documentsVerified")
    var documentsVerified: Boolean? = false,
    @SerializedName("otpBlocked")
    var otpBlocked: Boolean? = false,
    @SerializedName("termsAndConditionTimeStamp")
    var termsAndConditionTimeStamp: String? = null,
    @SerializedName("csrDocumentTimeStamp")
    var csrDocumentTimeStamp: String? = null,
    @SerializedName("workItemCreated")
    var workItemCreated: Boolean? = false,
    @SerializedName("freezeCode")
    var severityLevel: String? = null,
    @SerializedName("freezeInitiator")
    var freezeInitiator: String? = null,
    @SerializedName("eidNotificationContent")
    var EIDExpiryMessage: String? = null,
    @SerializedName("encryptedAccountUUID")
    var encryptedAccountUUID: String? = null
) : Parcelable