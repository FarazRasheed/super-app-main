package co.yap.widgets.searchablespinner

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import co.yap.countryutils.country.Country
import co.yap.widgets.BaseArrayAdapter
import co.yap.yapcore.BaseViewHolder

class SearchableSpinnerAdapter(mValue: MutableList<Country> ,navigation: NavController): BaseArrayAdapter<Country, SearchableItemViewModel,SearchableSpinnerAdapter.ViewHolder>(mValue,navigation) {

    class ViewHolder(view: View, viewModel: SearchableItemViewModel, mDataBinding: ViewDataBinding) :
        BaseViewHolder<Country, SearchableItemViewModel>(view, viewModel, mDataBinding)



    override fun getViewHolder(
        view: View,
        viewModel: SearchableItemViewModel,
        mDataBinding: ViewDataBinding,
        viewType: Int
    )= ViewHolder(view, viewModel, mDataBinding)

    override fun getVariableId(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewModel(): SearchableItemViewModel {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(viewType: Int): Int {
        TODO("Not yet implemented")
    }
}


