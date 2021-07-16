package co.yap.modules.dashboard.home.helpers.transaction

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import co.yap.R
import co.yap.modules.dashboard.home.interfaces.IYapHome
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import co.yap.widgets.tooltipview.TooltipView
import co.yap.yapcore.helpers.DateUtils
import co.yap.yapcore.helpers.RecyclerTouchListener
import co.yap.yapcore.helpers.extentions.dimen
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager
import com.yarolegovich.discretescrollview.DiscreteScrollView
import kotlinx.android.synthetic.main.content_fragment_yap_home.view.*
import kotlinx.android.synthetic.main.fragment_yap_home.view.*
import kotlinx.android.synthetic.main.view_graph.view.*
import java.util.*

class TransactionsViewHelper(
    val context: Context, val transactionsView: View,
    val viewModel: IYapHome.ViewModel

) {
    private var tooltip: TooltipView? = null
    var checkScroll: Boolean = false
    var totalItemCount: Int = 0
    var barSelectedPosition: Int = 0
    private var toolbarCollapsed = false
    private var rvTransactionScrollListener: OnScrollListener? = null


    init {
        setOnGraphBarClickListeners()
        initCustomTooltip()
        //setTooltipOnZero()
        setRvTransactionScroll()
    }

    private fun setOnGraphBarClickListeners() {

        transactionsView.rvTransactionsBarChart.addOnItemTouchListener(
            RecyclerTouchListener(
                context, true, transactionsView.rvTransactionsBarChart,
                object : RecyclerTouchListener.ClickListener {
                    override fun onLeftSwipe(view: View, position: Int) {
                        val layoutManager =
                            transactionsView.rvTransactionsBarChart.layoutManager as LinearLayoutManager
                        if (position >= layoutManager.findLastVisibleItemPosition()) {
                            transactionsView.rvTransactionsBarChart.scrollToPosition(
                                layoutManager.findLastCompletelyVisibleItemPosition() + 1
                            )
                        }
                        checkScroll = true
                        view.performClick()
                        transactionsView.rvTransaction.smoothScrollToPosition(position)

                    }

                    override fun onRightSwipe(view: View, position: Int) {
                        val layoutManager =
                            transactionsView.rvTransactionsBarChart.layoutManager as LinearLayoutManager
                        if (position <= layoutManager.findFirstVisibleItemPosition()) {

                            transactionsView.rvTransactionsBarChart.scrollToPosition(
                                layoutManager.findFirstCompletelyVisibleItemPosition() - 1
                            )
                        }
                        checkScroll = true
                        view.performClick()
                        transactionsView.rvTransaction.smoothScrollToPosition(position)
                    }

                    override fun onClick(view: View, position: Int) {
                        checkScroll = true
                        view.performClick()
                        removeRvTransactionScroll()
                        transactionsView.rvTransaction.smoothScrollToPosition(position)
                        setRvTransactionScroll()
                        if (position == 0) {
                            transactionsView.appbar.setExpanded(true)

                        }

                    }
                }
            )
        )
    }

    private fun initCustomTooltip() {
        tooltip = transactionsView.findViewById(R.id.tooltip)
    }

    private fun addToolTipDelay(delay: Long, process: () -> Unit) {
        Handler().postDelayed({
            process()
        }, delay)
    }

    fun setTooltipOnZero() {
        setTooltipVisibility(View.VISIBLE)
        addToolTipDelay(300) {
            val newView =
                transactionsView.rvTransactionsBarChart.getChildAt(0)
            if (null != newView) {
                addTooltip(
                    newView.findViewById(R.id.transactionBar),
                    viewModel.transactionsLiveData.value!![0], true
                )
            }
        }
    }

    fun setTooltipVisibility(visibility: Int = View.VISIBLE) {
        transactionsView.tvTransactionDate?.visibility = visibility
        tooltip?.visibility = visibility
        tooltip?.arrowView = transactionsView.findViewById(R.id.arrowView)
        tooltip?.arrowView?.visibility = visibility
    }

    fun addTooltip(view: View?, data: HomeTransactionListData, firstTime: Boolean = false) {
        transactionsView.tvTransactionDate.visibility = View.VISIBLE
        transactionsView.tvTransactionDate.text = DateUtils.reformatStringDate(
            data.originalDate ?: "",
            "yyyy-MM-dd",
            DateUtils.FORMAT_MON_YEAR
        )

        view?.let {
            val text = String.format(
                Locale.US,
                "%s \nAED %s",
                DateUtils.reformatStringDate(
                    data.originalDate ?: "",
                    "yyyy-MM-dd",
                    DateUtils.FORMAT_DATE_MON_YEAR
                ),
                data.closingBalance.toString()
                    .toFormattedCurrency(showCurrency = false, currency = SessionManager.getDefaultCurrency())
            )
            tooltip?.apply {
                visibility = View.VISIBLE
                it.bringToFront()
                this.text.text = data.date?.let {
                    SpannableString(text).apply {
                        setSpan(
                            ForegroundColorSpan(ContextCompat.getColor(context, R.color.greyDark)),
                            0,
                            text.substring(0, text.indexOf("\n")).length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }

                val viewPosition = IntArray(2)
                val arrowViewPosition = IntArray(2)
                it.getLocationInWindow(viewPosition)
                tooltip?.arrowView?.getLocationInWindow(arrowViewPosition)
                val screen = DisplayMetrics()
                (context as Activity).windowManager.defaultDisplay.getMetrics(screen)

                if (viewPosition[0] + this.width >= screen.widthPixels) {
                    // It is the end of the screen so adjust X
//                    if((arrowViewPosition[0].minus(this.width))>screen.widthPixels/2){
//                        translationX =
//                            screen.widthPixels.toFloat() - this.width-10
//                    }else {
                    translationX =
                        screen.widthPixels.toFloat() - this.width
//                    }

                    // Adjust position of arrow of tooltip
                    arrowX = viewPosition[0] - x + (it.width / 2)
                    tooltip?.arrowView?.translationX =
                        ((viewPosition[0].toFloat() - (it.width / 2))) + context.dimen(R.dimen.tooltip_default_corner_radius)// translationX-it.width / 2//viewPosition[0] - x + (it.width / 2)
                } else {
                    val viewWidth = it.width + (context.dimen(R.dimen.margin_one_dp) * 2)
                    if ((viewPosition[0] - viewWidth) > 0) {
                        translationX =
                            viewPosition[0].toFloat() - tooltip?.arrowView?.width!!//context.dimen(R.dimen._2sdp)
                        arrowX =
                            viewPosition[0] - x + (it.width / 2)
                        tooltip?.arrowView?.translationX =
                            viewPosition[0].toFloat() - (it.width / 2) + context.dimen(R.dimen.tooltip_default_corner_radius)
                    } else {
                        translationX = 0f
                        arrowX = 0f
                        tooltip?.arrowView?.translationX = viewPosition[0].toFloat()
                    }
                }
                val notificationView =
                    transactionsView.findViewById<DiscreteScrollView>(R.id.rvNotificationList)
                if (firstTime) {
                    addToolTipDelay(10) {
                        y = (it.y - this.height) - (tooltip?.arrowView?.height?.div(2)
                            ?: context.dimen(R.dimen._6sdp))
                        if (notificationView.adapter?.itemCount ?: 0 > 0) {
                            y += notificationView.height
                        }
                    }
                } else {
                    y = (it.y - this.height) - (tooltip?.arrowView?.height?.div(2) ?: context.dimen(
                        R.dimen._6sdp
                    ))
                    if (notificationView.adapter?.itemCount ?: 0 > 0) {
                        y += notificationView.height
                    }
                }
            }
        }
    }

    private fun removeRvTransactionScroll() {
        rvTransactionScrollListener?.let { transactionsView.rvTransaction.removeOnScrollListener(it) }

    }

    private fun setRvTransactionScroll() {
        rvTransactionScrollListener =
            object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        SCROLL_STATE_IDLE -> {
                            checkScroll = false
                        }
                        SCROLL_STATE_DRAGGING -> {

                        }
                        SCROLL_STATE_SETTLING -> {

                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstVisibleItemPosition()
                    if (!checkScroll) {
                        val graphLayoutManager =
                            transactionsView.rvTransactionsBarChart.layoutManager as LinearLayoutManager
                        val view =
                            transactionsView.rvTransactionsBarChart.layoutManager?.findViewByPosition(
                                position
                            )
                        view?.performClick()
                        if (dy > 0) {
                            if (position >= graphLayoutManager.findLastVisibleItemPosition()) {
                                transactionsView.rvTransactionsBarChart.scrollToPosition(
                                    graphLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                                )
                            }
                        } else if (dy < 0) {
                            if (position <= graphLayoutManager.findFirstVisibleItemPosition()) {

                                transactionsView.rvTransactionsBarChart.scrollToPosition(
                                    graphLayoutManager.findFirstCompletelyVisibleItemPosition() - 1
                                )

                            }
                        }
                    }
                }
            }
        rvTransactionScrollListener?.let { transactionsView.rvTransaction.addOnScrollListener(it) }
    }

    fun onToolbarCollapsed() {
        toolbarCollapsed = true
    }

    fun onToolbarExpanded() {
        toolbarCollapsed = false
    }
}
