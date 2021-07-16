package co.yap.modules.dashboard.cards.addpaymentcard.spare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.yap.R
import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import kotlinx.android.synthetic.main.item_benefits.view.*

class SpareCardsLandingAdapter(
    private val listItems: ArrayList<BenefitsModel>,
    private val listener: OnItemClickedListener?
) : RecyclerView.Adapter<SpareCardsLandingAdapter.ViewHolder>() {
    lateinit var viewHolder: ViewHolder


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_benefits, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder = holder
        val benefitsModel: BenefitsModel = listItems[position]
        holder.tvBenefit.text = benefitsModel.benfitTitle


        holder.itemView.setOnClickListener { listener?.onItemClick(benefitsModel) }
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvBenefit: TextView = itemView.tvBenefit
    }

    interface OnItemClickedListener {
        fun onItemClick(benefitsModel: BenefitsModel)

    }
}