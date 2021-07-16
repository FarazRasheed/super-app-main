package co.yap.modules.dashboard.cards.analytics.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentCategoryAnalyticsBinding
import co.yap.modules.dashboard.cards.analytics.adaptors.CategoryAnalyticsAdaptor
import co.yap.modules.dashboard.cards.analytics.interfaces.ICategoryAnalytics
import co.yap.modules.dashboard.cards.analytics.main.fragments.CardAnalyticsBaseFragment
import co.yap.modules.dashboard.cards.analytics.viewmodels.CategoryAnalyticsViewModel
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.item_analytics.view.*


class CategoryAnalyticsFragment : CardAnalyticsBaseFragment<ICategoryAnalytics.ViewModel>(),
    ICategoryAnalytics.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_category_analytics

    override val viewModel: CategoryAnalyticsViewModel
        get() = ViewModelProviders.of(this).get(CategoryAnalyticsViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        initAdaptor()
    }

    private fun setObservers() {
        viewModel.parentViewModel?.categoryAnalyticsItemLiveData?.observe(
            this,
            Observer { txnanalytics ->
                if (txnanalytics == null) {
                    return@Observer
                }
                getAdaptor().setList(txnanalytics)
            })
        viewModel.parentViewModel?.selectedItemPositionParent?.observe(
            this,
            Observer { selectedPosition ->
                val view = getBinding().recycler.layoutManager?.findViewByPosition(selectedPosition)
                if (null != view) {
                    highlightSelectedItem(view, selectedPosition)
                } else {
                    getBinding().recycler.removeOnScrollListener(onScrollListener)
                    getBinding().recycler.addOnScrollListener(onScrollListener)
                    getBinding().recycler.smoothScrollToPosition(selectedPosition)
                }
            })
    }

    private fun initAdaptor() {
        getBinding().recycler.adapter = CategoryAnalyticsAdaptor(mutableListOf())
        getAdaptor().setItemListener(listener)
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            viewModel.parentViewModel?.selectedItemPosition?.value = pos
            navigateDetails(pos)

        }
    }

    private fun navigateDetails(pos: Int) {
        Constants.MERCHANT_TYPE = "merchant-category-name"
        val selectedItem = getAdaptor().getDataForPosition(pos)
        var category: ArrayList<String> = arrayListOf()
        category.clear()
        if (selectedItem.title?.contains("Other") == true) {
            category = selectedItem.categories ?: arrayListOf()
        } else {
            category.add(selectedItem.title ?: "")
        }
        navigate(
            R.id.cardAnalyticsDetailsFragment,
            bundleOf(
                Constants.TRANSACTION_DETAIL to TxnAnalytic(
                    title = selectedItem.title,
                    txnCount = selectedItem.txnCount,
                    totalSpending = selectedItem.totalSpending,
                    logoUrl = selectedItem.logoUrl,
                    totalSpendingInPercentage = selectedItem.totalSpendingInPercentage,
                    categories = category
                ),
                Constants.TRANSACTION_POSITION to pos
            )
        )
    }

    private fun highlightSelectedItem(view: View?, pos: Int) {
        val colors = resources.getIntArray(co.yap.yapcore.R.array.analyticsColors)
        if (getAdaptor().checkedPosition != pos) {
            view?.let { itemView ->
                itemView.isSelected = true
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.itemBackground
                    )
                )
                itemView.tvName.setTextColor(colors[pos % colors.size])
                getAdaptor().notifyItemChanged(getAdaptor().checkedPosition)
                getAdaptor().checkedPosition = pos
            }
        }
    }

    private val onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                when (newState) {
                    SCROLL_STATE_IDLE -> {
                        val pos = viewModel.parentViewModel?.selectedItemPositionParent?.value
                        pos?.let { position ->
                            val view =
                                getBinding().recycler.layoutManager?.findViewByPosition(position)
                            highlightSelectedItem(view, position)
                        }
                    }
                }
            }
        }

    private fun getAdaptor(): CategoryAnalyticsAdaptor {
        return getBinding().recycler.adapter as CategoryAnalyticsAdaptor
    }

    private fun getBinding(): FragmentCategoryAnalyticsBinding {
        return (viewDataBinding as FragmentCategoryAnalyticsBinding)
    }
}
