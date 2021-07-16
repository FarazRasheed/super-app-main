package co.yap.modules.dashboard.yapit.topup.topupbankdetails

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.yapcore.BaseState

class TopUpBankDetailsState : BaseState(), ITopUpBankDetails.State {
    @get:Bindable
    override var toolBarVisibility: Boolean = true
        set(value) {
            field = value
            notifyPropertyChanged(BR.toolBarVisibility)
        }

    @get:Bindable
    override var pictureUrl: String? = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.pictureUrl)
        }

    @get:Bindable
    override var position: Int? = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.position)
        }
}