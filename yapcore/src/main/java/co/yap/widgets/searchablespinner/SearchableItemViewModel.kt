package co.yap.widgets.searchablespinner

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import co.yap.countryutils.country.Country
import co.yap.yapcore.BaseListItemViewModel
import co.yap.yapcore.R

class SearchableItemViewModel : BaseListItemViewModel<Country>() {
    override fun setItem(item: Country, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItem(): Country {
        TODO("Not yet implemented")
    }

    override fun onFirsTimeUiCreate(bundle: Bundle?, navigation: NavController?) {
        TODO("Not yet implemented")
    }

    override fun layoutRes(): Int = R.layout.item_list_country_code

    override fun onItemClick(view: View, data: Any, pos: Int) {
        TODO("Not yet implemented")
    }

}