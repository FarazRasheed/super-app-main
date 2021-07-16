package co.yap.modules.dashboard.home.models


data class TransactionAdapterModel(
    var viewType: String,
    var vendor: String,
    var type: String,
    var imageUrl: String,
    var time: String,
    var category: String,
    var amount: String,
    var currency: String
    )
