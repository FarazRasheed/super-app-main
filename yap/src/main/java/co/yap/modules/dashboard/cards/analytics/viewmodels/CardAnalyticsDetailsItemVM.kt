package co.yap.modules.dashboard.cards.analytics.viewmodels

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import co.yap.R
import co.yap.networking.models.ApiResponse
import co.yap.yapcore.BaseListItemViewModel

class CardAnalyticsDetailsItemVM : BaseListItemViewModel<ApiResponse>() {
    private lateinit var mItem: ApiResponse
    override fun setItem(item: ApiResponse, position: Int) {
        mItem = item
        notifyChange()
    }

    override fun getItem() = mItem

    override fun onFirsTimeUiCreate(bundle: Bundle?, navigation: NavController?) {
    }

    override fun layoutRes() = R.layout.item_card_analytics_details

    override fun onItemClick(view: View, data: Any, pos: Int) {
    }
}