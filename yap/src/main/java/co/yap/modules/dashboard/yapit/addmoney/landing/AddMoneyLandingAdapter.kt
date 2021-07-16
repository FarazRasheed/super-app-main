package co.yap.modules.dashboard.yapit.addmoney.landing

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.databinding.ItemYapItAddMoneyGooglePayBinding
import co.yap.databinding.ItemYapItAddMoneyLandingBinding
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener

class AddMoneyLandingAdapter(
    context: Context,
    private val list: MutableList<AddMoneyLandingOptions>
) :
    BaseBindingRecyclerAdapter<AddMoneyLandingOptions, RecyclerView.ViewHolder>(list) {

    private val iconType = 1
    private val paymentType = 2

    private var dimensions: IntArray = Utils.getCardDimensions(context, 43, 20)
    override fun getLayoutIdForViewType(viewType: Int): Int =
        if (viewType == iconType) R.layout.item_yap_it_add_money_landing else R.layout.item_yap_it_add_money_google_pay

    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return if (binding is ItemYapItAddMoneyLandingBinding) ViewHolder(
            binding
        ) else PaymentTypeViewHolder(
            binding as ItemYapItAddMoneyGooglePayBinding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            holder.onBind(list[position], position, dimensions, onItemClickListener)
        } else if (holder is PaymentTypeViewHolder) {
            holder.onBind(list[position], position, dimensions, onItemClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].id == Constants.ADD_MONEY_GOOGLE_PAY || list[position].id == Constants.ADD_MONEY_SAMSUNG_PAY) paymentType else iconType
    }

    class ViewHolder(private val itemYapItAddMoneyBinding: ItemYapItAddMoneyLandingBinding) :
        RecyclerView.ViewHolder(itemYapItAddMoneyBinding.root) {
        fun onBind(
            addMoneyOptions: AddMoneyLandingOptions,
            position: Int,
            dimensions: IntArray,
            onItemClickListener: OnItemClickListener?
        ) {
            val params =
                itemYapItAddMoneyBinding.clMain.layoutParams as GridLayoutManager.LayoutParams
            params.width = dimensions[0]
            params.height = dimensions[1]
            itemYapItAddMoneyBinding.clMain.layoutParams = params
            itemYapItAddMoneyBinding.viewModel =
                YapItAddMoneyLandingItemVM(
                    addMoneyOptions,
                    position,
                    onItemClickListener
                )
            itemYapItAddMoneyBinding.executePendingBindings()
        }
    }

    class PaymentTypeViewHolder(private val itemYapItAddMoneyGooglePayBinding: ItemYapItAddMoneyGooglePayBinding) :
        RecyclerView.ViewHolder(itemYapItAddMoneyGooglePayBinding.root) {

        fun onBind(
            addMoneyOptions: AddMoneyLandingOptions,
            position: Int,
            dimensions: IntArray,
            onItemClickListener: OnItemClickListener?
        ) {
            val params =
                itemYapItAddMoneyGooglePayBinding.clMain.layoutParams as GridLayoutManager.LayoutParams
            params.width = dimensions[0]
            params.height = dimensions[1]
            itemYapItAddMoneyGooglePayBinding.clMain.layoutParams = params
            itemYapItAddMoneyGooglePayBinding.viewModel =
                YapItAddMoneyLandingItemVM(
                    addMoneyOptions,
                    position,
                    onItemClickListener
                )
            itemYapItAddMoneyGooglePayBinding.executePendingBindings()
        }
    }
}

