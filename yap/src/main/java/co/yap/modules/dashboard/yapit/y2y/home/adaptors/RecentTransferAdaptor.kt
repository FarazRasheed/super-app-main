package co.yap.modules.dashboard.yapit.y2y.home.adaptors

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import co.yap.BR
import co.yap.modules.dashboard.yapit.y2y.home.viewmodel.RecentTransferItemVM
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.BaseRVAdapter
import co.yap.yapcore.BaseViewHolder


class RecentTransferAdaptor(mValue: MutableList<Beneficiary>, navigation: NavController) :
    BaseRVAdapter<Beneficiary, RecentTransferItemVM, RecentTransferAdaptor.ViewHolder>(
        mValue,
        navigation
    ) {
    override fun getLayoutId(viewType: Int) = getViewModel().layoutRes()
    override fun getViewHolder(
        view: View,
        viewModel: RecentTransferItemVM,
        mDataBinding: ViewDataBinding, viewType: Int
    ) = ViewHolder(view, viewModel, mDataBinding)

    override fun getViewModel() = RecentTransferItemVM()
    override fun getVariableId() = BR.recentTransferItemVM

    class ViewHolder(view: View, viewModel: RecentTransferItemVM, mDataBinding: ViewDataBinding) :
        BaseViewHolder<Beneficiary, RecentTransferItemVM>(view, viewModel, mDataBinding)
}