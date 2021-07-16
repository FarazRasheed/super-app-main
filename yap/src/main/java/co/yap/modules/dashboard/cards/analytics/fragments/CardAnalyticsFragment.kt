package co.yap.modules.dashboard.cards.analytics.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentCardAnalyticsBinding
import co.yap.modules.dashboard.cards.analytics.adaptors.CATEGORY_ANALYTICS
import co.yap.modules.dashboard.cards.analytics.adaptors.CardAnalyticsLandingAdaptor
import co.yap.modules.dashboard.cards.analytics.adaptors.MERCHANT_ANALYTICS
import co.yap.modules.dashboard.cards.analytics.interfaces.ICardAnalytics
import co.yap.modules.dashboard.cards.analytics.main.fragments.CardAnalyticsBaseFragment
import co.yap.modules.dashboard.cards.analytics.viewmodels.CardAnalyticsViewModel
import co.yap.networking.transactions.responsedtos.TxnAnalytic
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.widgets.pieview.*
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.helpers.spannables.color
import co.yap.yapcore.helpers.spannables.getText
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.collections.ArrayList


class CardAnalyticsFragment : CardAnalyticsBaseFragment<ICardAnalytics.ViewModel>(),
    ICardAnalytics.View, OnChartValueSelectedListener {

    lateinit var chart: PieChart
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_card_analytics

    override val viewModel: CardAnalyticsViewModel
        get() = ViewModelProviders.of(this).get(CardAnalyticsViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchCardCategoryAnalytics(
            DateUtils.dateToString(
                Calendar.getInstance().time,
                "yyyy-MM-dd"
            )
        )
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBindings()
        setupAdaptor()
        setupTabs()
    }

    private fun setupBindings() {
        getBindingView().rlDetails.setOnClickListener { }
        getBindingView().tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    /*
    * In this function set Pie View.
    * */

    private fun setPieView(data: ArrayList<TxnAnalytic>?) {
        chart = getBindingView().chart1
        if (::chart.isInitialized) {
            chart.setUsePercentValues(false)
            chart.description.isEnabled = false
            chart.dragDecelerationFrictionCoef = 0.95f
            chart.isDrawHoleEnabled = true
            chart.setHoleColor(Color.TRANSPARENT)
            chart.setTransparentCircleColor(Color.WHITE)
            chart.setTransparentCircleAlpha(200)
//            chart.holeRadius = // 78f  For Rounded corner graph with spaces
            chart.holeRadius = 70f
            chart.transparentCircleRadius = 70f
            chart.setDrawCenterText(true)
            chart.rotationAngle = -90f
            chart.isRotationEnabled = false
            chart.setOnChartValueSelectedListener(this)
            chart.animateY(1400, Easing.EaseInOutQuad)
            chart.legend.isEnabled = false // Hide the legend
            chart.setEntryLabelColor(Color.WHITE)
            chart.setEntryLabelTextSize(0f)
//            chart.setDrawRoundedSlices(true)  // For Rounded corner graph with spaces
            setData(data)
        }
    }

    /*
    * In this set Data in Pie View.
    * */

    private fun setData(txnAnalytics: List<TxnAnalytic>?) {
        val entries: ArrayList<PieEntry> = ArrayList()
        val colors = ArrayList<Int>()
        if (txnAnalytics.isNullOrEmpty()) {
            chart.isHighlightPerTapEnabled = false
            entries.add(PieEntry(100f))
            colors.add(ColorTemplate.getEmptyColor())
        } else {
            chart.isHighlightPerTapEnabled = true
            for (item in txnAnalytics.iterator())
                item.totalSpendingInPercentage?.toFloat()?.let { PieEntry(it) }?.let {
                    entries.add(it)
                }
        }
        colors.addAll(resources.getIntArray(co.yap.yapcore.R.array.analyticsColors).toTypedArray())
        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 0f
//        dataSet.sliceSpace = 5f   // For Rounded corner graph with spaces
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 20f
        dataSet.setDrawValues(false)
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        chart.data = data
        if (!txnAnalytics.isNullOrEmpty())
            chart.highlightValue(0f, 0)
        else
            chart.highlightValue(0f, -1)

        chart.invalidate()
    }


    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickEventObserver)
        viewModel.parentViewModel?.merchantAnalyticsItemLiveData?.observe(this, Observer {
            if (it != null && it.isNullOrEmpty())
                getBindingView().rlDetails.visibility = View.INVISIBLE
            else
                getBindingView().rlDetails.visibility = View.VISIBLE

            val selectedTabPos = getBindingView().tabLayout.selectedTabPosition
            setupPieChart(selectedTabPos)
            setSelectedTabData(selectedTabPos, 0)
        })
        viewModel.parentViewModel?.selectedItemPosition?.observe(this, Observer {
            when (getBindingView().tabLayout.selectedTabPosition) {
                CATEGORY_ANALYTICS -> {
                    viewModel.parentViewModel?.categoryAnalyticsItemLiveData?.value?.let { list ->
                        updatePieChartInnerData(list[it])
                        setState(list[it])
                    }
                    viewModel.state.selectedItemPosition = it
                    showPieView(it)
                }
                MERCHANT_ANALYTICS -> {
                    viewModel.parentViewModel?.merchantAnalyticsItemLiveData?.value?.let { list ->
                        updatePieChartInnerData(list[it])
                        setState(list[it])
                    }
                    viewModel.state.selectedItemPosition = it
                    showPieView(it)
                }
            }
        })
        viewModel.parentViewModel
    }

    /*
    * In this function show PieView.
    * */

    private fun showPieView(indexValue: Int) {
        when (indexValue) {
            0 -> {
                chart.highlightValue(0f, 0, true)
            }
            1 -> {
                chart.highlightValue(1f, 0, true)
            }
            2 -> {
                chart.highlightValue(2f, 0, true)
            }
            3 -> {
                chart.highlightValue(3f, 0, true)
            }
            4 -> {
                chart.highlightValue(4f, 0, true)
            }
        }
    }

    private val clickEventObserver = Observer<Int> {
        when (it) {
            R.id.ivPrevious -> {
            }
            Constants.CATEGORY_AVERAGE_AMOUNT_VALUE -> {
                getBindingView().tvMonthlyAverage.text = requireContext().resources.getText(
                    getString(Strings.screen_card_analytics_display_month_average_text),
                    requireContext().color(
                        R.color.colorPrimaryDark,
                        viewModel.state.monthlyCategoryAvgAmount.toString()
                            .toFormattedCurrency(true)
                    )
                )
            }
        }
    }
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {}

        override fun onTabUnselected(tab: TabLayout.Tab?) {}

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { tabs ->
                setSelectedTabData(tabs.position, 0)
                setupPieChart(tabs.position)
            }
        }
    }

    private fun setupAdaptor() {
        val adaptor = CardAnalyticsLandingAdaptor(this)
        getBindingView().viewPager.adapter = adaptor
    }

    private fun setupTabs() {
        TabLayoutMediator(getBindingView().tabLayout, getBindingView().viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = getTabTitle(position)
            }).attach()

        getBindingView().viewPager.isUserInputEnabled = false
        getBindingView().viewPager.offscreenPageLimit = 1
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            CATEGORY_ANALYTICS -> Translator.getString(
                requireContext(),
                Strings.screen_card_analytics_display_tab_title_category
            )
            MERCHANT_ANALYTICS -> Translator.getString(
                requireContext(),
                Strings.screen_card_analytics_display_tab_title_merchant
            )
            else -> null
        }
    }

    private fun updatePieChartInnerData(item: TxnAnalytic?) {
        item?.let { txnAnalytics ->
            viewModel.state.selectedItemName = txnAnalytics.title
            viewModel.state.selectedItemPercentage = "${txnAnalytics.totalSpendingInPercentage}%"
            viewModel.state.selectedItemSpentValue =
                "${viewModel.state.currencyType}${txnAnalytics.totalSpending}"
        }
    }

    private fun getBindingView(): FragmentCardAnalyticsBinding {
        return (viewDataBinding as FragmentCardAnalyticsBinding)
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

        val selectedItem = getBindingView().tabLayout.selectedTabPosition
        h?.let {highlight ->
            setSelectedTabData(selectedItem, highlight.x.toInt())
            viewModel.parentViewModel?.selectedItemPositionParent?.value = highlight.x.toInt()
        }
    }

    private fun setSelectedTabData(TabPosition: Int, contentPos: Int) {
        when (TabPosition) {
            CATEGORY_ANALYTICS -> {
                if (!viewModel.parentViewModel?.categoryAnalyticsItemLiveData?.value.isNullOrEmpty()) {
                    val txnItem =
                        viewModel.parentViewModel?.categoryAnalyticsItemLiveData?.value?.get(
                            contentPos
                        )
                    updatePieChartInnerData(txnItem)
                    setState(txnItem)
                }
            }
            MERCHANT_ANALYTICS -> {
                if (!viewModel.parentViewModel?.merchantAnalyticsItemLiveData?.value.isNullOrEmpty()) {
                    val txnItem =
                        viewModel.parentViewModel?.merchantAnalyticsItemLiveData?.value?.get(
                            contentPos
                        )
                    updatePieChartInnerData(txnItem)
                    setState(txnItem)
                }
            }
        }
        viewModel.state.selectedItemPosition = contentPos
    }

    private fun setupPieChart(TabPosition: Int) {
        when (TabPosition) {
            CATEGORY_ANALYTICS -> {
                setPieView(viewModel.parentViewModel?.categoryAnalyticsItemLiveData?.value)
                viewModel.state.totalSpent = viewModel.state.totalCategorySpent
                getBindingView().tvMonthlyAverage.text = requireContext().resources.getText(
                    getString(Strings.screen_card_analytics_display_month_average_text),
                    requireContext().color(
                        R.color.colorPrimaryDark,
                        viewModel.state.monthlyCategoryAvgAmount.toString()
                            .toFormattedCurrency(true)
                    )
                )
            }
            MERCHANT_ANALYTICS -> {
                setPieView(viewModel.parentViewModel?.merchantAnalyticsItemLiveData?.value)
                viewModel.state.totalSpent = viewModel.state.totalMerchantSpent
                getBindingView().tvMonthlyAverage.text = requireContext().resources.getText(
                    getString(Strings.screen_card_analytics_display_month_average_text),
                    requireContext().color(
                        R.color.colorPrimaryDark,
                        viewModel.state.monthlyMerchantAvgAmount.toString()
                            .toFormattedCurrency(true)
                    )
                )
            }
        }
    }

    private fun setState(txnAnalytic: TxnAnalytic?) {
        viewModel.state.selectedTxnAnalyticsItem = txnAnalytic
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.parentViewModel?.merchantAnalyticsItemLiveData?.removeObservers(this)
        viewModel.clickEvent.removeObservers(this)
        viewModel.parentViewModel?.selectedItemPosition?.removeObservers(this)
    }
}
