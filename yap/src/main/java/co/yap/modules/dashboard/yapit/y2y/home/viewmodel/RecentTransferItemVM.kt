package co.yap.modules.dashboard.yapit.y2y.home.viewmodel

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import co.yap.R
import co.yap.modules.dashboard.yapit.y2y.home.fragments.YapToYapFragmentDirections
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.yapcore.BaseActivity
import co.yap.yapcore.BaseListItemViewModel
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.extentions.showBlockedFeatureAlert
import co.yap.yapcore.managers.FeatureProvisioning

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

    override fun layoutRes() = R.layout.item_recent_transfer
    override fun onItemClick(view: View, data: Any, pos: Int) {
        if (FeatureProvisioning.getFeatureProvisioning(FeatureSet.Y2Y_TRANSFER)) { // update otp as well in a single case
            if (view.context is BaseActivity<*>) {
                val activity = view.context as BaseActivity<*>
                showBlockedFeatureAlert(activity, FeatureSet.Y2Y_TRANSFER)
            }
        } else {
            navigation?.navigate(
                YapToYapFragmentDirections.actionYapToYapHomeToY2YTransferFragment(
                    (data as Beneficiary).beneficiaryPictureUrl ?: ""
                    , data.beneficiaryUuid ?: "", data.title ?: "", pos,data.beneficiaryCreationDate?:""
                )
            )
        }
    }
}