package co.yap.networking.transactions.responsedtos.transaction

import com.google.gson.annotations.SerializedName


data class HomeTransactionListData(
    @SerializedName("type")
    var type: String,
    @SerializedName("totalAmountType")
    var totalAmountType: String,
    @SerializedName("date")
    var date: String?,
    @SerializedName("totalAmount")
    var totalAmount: String,
    @SerializedName("closingBalance")
    var closingBalance: Double?,
    @SerializedName("amountPercentage")
    var amountPercentage: Double,
    @SerializedName("content")
    var transaction: List<Transaction>,
    @SerializedName("first")
    var first: Boolean,
    @SerializedName("last")
    var last: Boolean,
    @SerializedName("number")
    var number: Int,
    @SerializedName("numberOfElements")
    var numberOfElements: Int,
    @SerializedName("pageable")
    var pageable: Pageable,
    @SerializedName("size")
    var size: Int,
    @SerializedName("sort")
    var sort: Sort,
    @SerializedName("totalElements")
    var totalElements: Int,
    @SerializedName("totalPages")
    var totalPages: Int,
    @Transient
    var originalDate: String? = ""

)