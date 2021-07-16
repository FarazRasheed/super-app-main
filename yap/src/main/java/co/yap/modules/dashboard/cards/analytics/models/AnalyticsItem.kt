package co.yap.modules.dashboard.cards.analytics.models

data class AnalyticsItem(
    var title: String,
    var txnCount: Int,
    var totalSpending: String,
    var totalSpendingInPercentage: Double,
    var logoUrl: String = ""
)