package co.yap.modules.dashboard.cards.analytics.adaptors

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import co.yap.BR
import co.yap.modules.dashboard.cards.analytics.viewmodels.CardAnalyticsDetailsItemVM
import co.yap.networking.models.ApiResponse
import co.yap.yapcore.BaseRVAdapter
import co.yap.yapcore.BaseViewHolder

class CardAnalyticsDetailsAdapter(mValue: MutableList<ApiResponse>, navigation: NavController?) :
    BaseRVAdapter<ApiResponse, CardAnalyticsDetailsItemVM, CardAnalyticsDetailsAdapter.ViewHolder>(
        mValue,
        navigation
    ) {
    override fun getLayoutId(viewType: Int) = getViewModel().layoutRes()
    override fun getViewHolder(
        view: View,
        viewModel: CardAnalyticsDetailsItemVM,
        mDataBinding: ViewDataBinding, viewType: Int
    ) = ViewHolder(
        view,
        viewModel,
        mDataBinding
    )

    override fun getViewModel() = CardAnalyticsDetailsItemVM()
    override fun getVariableId() = BR.cardAnalyticsDetailsItemViewModel

    class ViewHolder(
        view: View,
        viewModel: CardAnalyticsDetailsItemVM,
        mDataBinding: ViewDataBinding
    ) :
        BaseViewHolder<ApiResponse, CardAnalyticsDetailsItemVM>(view, viewModel, mDataBinding)

}