package co.yap.modules.dashboard.store.states

import androidx.databinding.Bindable
import co.yap.BR
import co.yap.modules.dashboard.store.interfaces.IYapStoreDetail
import co.yap.yapcore.BaseState

class YapStoreDetailState : BaseState(), IYapStoreDetail.State {

    @get:Bindable
    override var title: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    override var subTitle: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.subTitle)
        }

    @get:Bindable
    override var image: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.image)
        }

    @get:Bindable
    override var storeIcon: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.storeIcon)
        }

    @get:Bindable
    override var storeHeading: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.storeHeading)
        }

    @get:Bindable
    override var storeDetail: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.storeDetail)
        }
}