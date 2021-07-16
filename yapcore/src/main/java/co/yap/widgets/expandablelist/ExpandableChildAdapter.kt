package co.yap.widgets.expandablelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.yapcore.R
import co.yap.yapcore.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.child_row.view.*

class ExpandableChildAdapter(
    var context: Context,
    var popList: ArrayList<PurposeOfPayment>,
    var mListener: OnItemClickListener
) : RecyclerView.Adapter<ExpandableChildAdapter.PopViewHolder>() {
    override fun onCreateViewHolder(container: ViewGroup, i: Int): PopViewHolder {
        return PopViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.child_row, container, false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: PopViewHolder, i: Int) {
        holder.bind(i)
    }

    override fun getItemCount(): Int {
        return popList.size
    }

    abstract inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(pos: Int)
    }

    inner class PopViewHolder(itemView: View, var mListener: OnItemClickListener) :
        ViewHolder(itemView) {
        override fun bind(pos: Int) {
            val pop = popList[pos]
            itemView.tvC.text = pop.purposeDescription
            itemView.tvC.setOnClickListener {
                mListener.onItemClick(it, pop, pos)
            }
        }
    }

}