package co.yap.sendmoney.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import co.yap.networking.transactions.responsedtos.InternationalFundsTransferReasonList
import co.yap.sendmoney.R

class ReasonListSpinnerAdapter(
    val context: Context,
    private val labels: List<InternationalFundsTransferReasonList.ReasonList>
) : BaseAdapter() {
    val inflater: LayoutInflater? = LayoutInflater.from(context)

    override fun getCount(): Int {
        return labels.size
    }

    override fun getItem(i: Int): Any? {
        return labels[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(i: Int, view: View?, parent: ViewGroup): View {
//        var view = view
        val view = inflater?.inflate(R.layout.item_reason_list, parent, false)
        val label = view?.findViewById<View>(R.id.textView) as TextView
        label.text = labels[i].reason

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater?.inflate(R.layout.item_reason_list, parent, false)
        }
        val rowItem = getItem(position)
        val txtTitle = convertView?.findViewById<View>(R.id.textView) as TextView
        txtTitle.text = labels[position].reason

        return convertView
    }
}