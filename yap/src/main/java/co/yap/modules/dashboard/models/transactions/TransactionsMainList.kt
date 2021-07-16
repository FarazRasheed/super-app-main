package co.yap.modules.dashboard.models.transactions

import androidx.annotation.Nullable

data class TransactionsMainList(
    var type: String,//txnType
    var totalAmountType: String,
    var date: String,//txnDate
    var totalAmount: String,// sum of txnAmount
    var closingBalance: String,//  closingBalance of last transaction of this day or the minimum 1
    var amountPercentage: Double,
    @Nullable var transactionItems: ArrayList<TransactionData>
)