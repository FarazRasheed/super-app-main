package co.yap.networking.transactions.responsedtos

import com.google.gson.annotations.SerializedName

class TransactionFilters (@SerializedName("minAmount") var minAmount: Double?,
                          @SerializedName("maxAmount") var maxAmount: Double?)