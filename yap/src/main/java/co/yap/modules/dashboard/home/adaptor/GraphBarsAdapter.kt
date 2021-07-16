package co.yap.modules.dashboard.home.adaptor

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemBarChartV2Binding
import co.yap.modules.dashboard.home.component.ChartViewV2
import co.yap.modules.dashboard.home.helpers.transaction.TransactionsViewHelper
import co.yap.modules.dashboard.home.interfaces.IYapHome
import co.yap.networking.transactions.responsedtos.transaction.HomeTransactionListData
import co.yap.yapcore.BaseBindingRecyclerAdapter
import kotlinx.android.synthetic.main.item_bar_chart_v2.view.*


class GraphBarsAdapter(
    private val listItems: MutableList<HomeTransactionListData>,
    val viewModel: IYapHome.ViewModel
) : BaseBindingRecyclerAdapter<HomeTransactionListData, GraphBarsAdapter.GraphViewHolder>(listItems),
    View.OnFocusChangeListener {

    private var checkedPosition = 0
    var helper: TransactionsViewHelper? = null

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_bar_chart_v2

    override fun onCreateViewHolder(binding: ViewDataBinding): GraphViewHolder {
        return GraphViewHolder(binding as ItemBarChartV2Binding, viewModel)
    }

    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        val transactionModel: HomeTransactionListData = listItems[position]
        holder.transactionBar.onFocusChangeListener = this
        holder.onBind(transactionModel)

        if (checkedPosition == -1) {
            holder.transactionBar.needAnimation = false
            holder.transactionBar.isSelected = false

        } else {
            holder.transactionBar.isSelected = checkedPosition == holder.adapterPosition
        }

        holder.itemView.setOnClickListener { v ->

            if (checkedPosition != holder.adapterPosition) {
                holder.transactionBar.needAnimation = true
                holder.transactionBar.isSelected = true
                helper?.addTooltip(v.findViewById(R.id.transactionBar), transactionModel)
                notifyItemChanged(checkedPosition)
                checkedPosition = holder.adapterPosition
                helper?.barSelectedPosition = checkedPosition
            }
        }
        holder.itemView.isClickable = false
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        //if (!viewHolder.transactionBar.hasFocus()) {
        //   viewHolder.transactionBar.unSelectHighlightedBarOnGraphClick(hasFocus)
        //}
    }

    override fun getItemCount(): Int {
        helper?.totalItemCount = listItems.size
        return listItems.size
    }

    class GraphViewHolder(
        itemBarChartBinding: ItemBarChartV2Binding,
        private val viewModel: IYapHome.ViewModel
    ) : RecyclerView.ViewHolder(itemBarChartBinding.root) {
        val transactionBar: ChartViewV2 = itemView.transactionBar

        fun onBind(transactionModel: HomeTransactionListData) {
            transactionModel.amountPercentage =
                calculatePercentagePerDayFromClosingBalance(transactionModel.closingBalance?:0.0)
            transactionBar.barHeight = transactionModel.amountPercentage.toFloat()
        }

        private fun calculatePercentagePerDayFromClosingBalance(closingBalance: Double): Double {
            closingBalance.toString().replace("-", "").toDouble()
            val percentage: Double = (closingBalance.toString().replace(
                "-",
                ""
            ).toDouble() / viewModel.MAX_CLOSING_BALANCE)
            return percentage
        }

    }


}