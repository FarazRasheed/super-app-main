package co.yap.sendmoney.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import co.yap.countryutils.country.Country
import co.yap.sendmoney.R
import co.yap.widgets.CoreCircularImageView

class CountryAdapter (
    val context: Context,
    private val objects: List<Country>
) : BaseAdapter() {

    override fun getItem(position: Int): Country {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return objects.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.item_country, parent, false)
        val label = row.findViewById(R.id.textView) as TextView
        val flag = row.findViewById(R.id.flag_img) as CoreCircularImageView
        label.text = objects[position].getName()
        flag.setImageResource(objects[position].getFlagDrawableResId(label.context))

        if (position == 0) {//Special style for dropdown header
            label.setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
        }
        return row
    }
}