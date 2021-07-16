package co.yap.modules.location

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import co.yap.networking.customers.responsedtos.City
import co.yap.yapcore.BaseBindingRecyclerAdapter
import co.yap.yapcore.R
import co.yap.yapcore.databinding.ItemCityBinding
import co.yap.yapcore.interfaces.OnItemClickListener

class CitiesAdapter(private val list: MutableList<City>) :
    BaseBindingRecyclerAdapter<City, CityItemViewHolder>(list) {

    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.item_city

    override fun onCreateViewHolder(binding: ViewDataBinding): CityItemViewHolder {
        return CityItemViewHolder(binding as ItemCityBinding)
    }

    override fun onBindViewHolder(holder: CityItemViewHolder, position: Int) {
        holder.onBind(list[position], position, onItemClickListener)
    }
}

class CityItemViewHolder(private val itemCityBinding: ItemCityBinding) :
    RecyclerView.ViewHolder(itemCityBinding.root) {

    fun onBind(city: City, position: Int, onItemClickListener: OnItemClickListener?) {
        itemCityBinding.viewModel = CityItemViewModel(city, position, onItemClickListener)
        itemCityBinding.executePendingBindings()
    }
}

class CityItemViewModel(
    val city: City,
    private val position: Int,
    private val onItemClickListener: OnItemClickListener?
) {
    fun onViewClicked(view: View) {
        onItemClickListener?.onItemClick(view, city, position)
    }
}