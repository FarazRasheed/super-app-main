package co.yap.networking.customers.responsedtos.sendmoney

import android.os.Parcelable
import co.yap.networking.models.ApiResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RAKBankModel(
    @SerializedName("data")
    var data: RAKBank? = null,
    @SerializedName("errors")
    var errors: String? = null
) : ApiResponse(), Parcelable

@Parcelize
data class RAKBank(
    @SerializedName("other_bank_country")
    var other_bank_country: String? = "",
    @SerializedName("has_more_records")
    var max_records: String? = "",
    @SerializedName("fields")
    val fields: ArrayList<Fields>? = ArrayList(),
    @SerializedName("data")
    val banks: ArrayList<Bank>? = ArrayList()
) : ApiResponse(), Parcelable {

    @Parcelize
    data class Bank(
        @SerializedName("other_bank_name")
        var other_bank_name: String? = null,
        @SerializedName("identifier_code1")
        var identifier_code1: String? = null,
        @SerializedName("identifier_code2")
        var identifier_code2: String? = null,
        @SerializedName("other_branch_addr1")
        var other_branch_addr1: String? = null,
        @SerializedName("other_branch_name")
        var other_branch_name: String? = null,
        @SerializedName("other_branch_addr2")
        var other_branch_addr2: String? = null
    ) : ApiResponse(), Parcelable

    @Parcelize
    data class Fields(
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("name")
        var name: String? = null
    ) : ApiResponse(), Parcelable
}