package co.yap.modules.dashboard.cards.analytics.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.analytics.interfaces.ICardAnalyticsDetails
import co.yap.modules.dashboard.cards.analytics.main.fragments.CardAnalyticsBaseFragment
import co.yap.modules.dashboard.cards.analytics.viewmodels.CardAnalyticsDetailsViewModel
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.translation.Strings
import co.yap.widgets.DividerItemDecoration
import co.yap.widgets.MultiStateView
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.extentions.dimen
import kotlinx.android.synthetic.main.fragment_card_analytics_details.*

class CardAnalyticsDetailsFragment : CardAnalyticsBaseFragment<ICardAnalyticsDetails.ViewModel>() {
    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.fragment_card_analytics_details

    override val viewModel: CardAnalyticsDetailsViewModel
        get() = ViewModelProviders.of(this).get(CardAnalyticsDetailsViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgument()
    }

    private fun getArgument() {
        arguments?.let { bundle ->
            bundle.getParcelable<TxnAnalytic>(Constants.TRANSACTION_DETAIL)?.let { txnAnalytics ->
                viewModel.state.title.set(txnAnalytics.title ?: "")
                viewModel.state.totalSpendings.set(txnAnalytics.totalSpending)
                viewModel.state.ImageUrl.set(txnAnalytics.logoUrl)
                viewModel.state.countWithDate.set(getConcatinatedString(txnAnalytics.txnCount ?: 0))
                viewModel.state.monthlyTotalPercentage.set("${txnAnalytics.totalSpendingInPercentage}%")
                viewModel.state.categories = txnAnalytics.categories
                if (txnAnalytics.title.equals("Other")) viewModel.state.percentCardVisibility  = false
            }
            bundle.getInt(Constants.TRANSACTION_POSITION).let { position ->
                viewModel.state.position = position
            }
        }
    }

    private fun getConcatinatedString(count: Int): String? {
        var concatenatedString = ""
        var date = viewModel.parentViewModel?.state?.currentSelectedMonth ?: ""
        if (date.contains(",")) date = date.replace(",", "")
        concatenatedString =
            "$date ・ $count ${getString(Strings.screen_yap_analytics_detail_transaction_count)}"
        return concatenatedString
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clickEvent?.observe(this, Observer {
            activity?.onBackPressed()
        })
        viewModel.viewState.observe(this, viewStateObserver)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                R.drawable.line_divider,
                false,
                false, dimen(R.dimen._52sdp)!!
            )
        )
        multiStateView.viewState = MultiStateView.ViewState.CONTENT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.viewState.removeObserver(viewStateObserver)
        viewModel.clickEvent?.removeObservers(this)
    }

    private val viewStateObserver = Observer<Int> {
        when (it) {
            Constants.EVENT_LOADING -> {
                multiStateView.viewState = MultiStateView.ViewState.LOADING
            }
            Constants.EVENT_EMPTY -> {
                multiStateView.viewState = MultiStateView.ViewState.EMPTY
            }
            Constants.EVENT_CONTENT -> {
                multiStateView.viewState = MultiStateView.ViewState.CONTENT
            }
        }
    }
}

