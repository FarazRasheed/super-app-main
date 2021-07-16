package co.yap.sendmoney.home.adapters

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
 import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.R
import co.yap.yapcore.BaseListItemViewModel

class RecentTransferItemVM : BaseListItemViewModel<Beneficiary>() {
    private lateinit var mItem: Beneficiary
    private var navigation: NavController? = null
    var position: Int = -1

    override fun setItem(item: Beneficiary, position: Int) {
        this.mItem = item
        this.position = position
        notifyChange()
    }

    override fun getItem() = mItem

    override fun onFirsTimeUiCreate(bundle: Bundle?, navigation: NavController?) {
        this.navigation = navigation
    }

    override fun layoutRes() = R.layout.item_recent_beneficiaries_transfer
    override fun onItemClick(view: View, data: Any, pos: Int) {
//        navigation?.navigate(
//            YapToYapFragmentDirections.actionYapToYapHomeToY2YTransferFragment(
//                (data as Beneficiary).beneficiaryPictureUrl!!
//                , data.accountUuid!!, data.title!!, pos
//            )
//        )
    }
}