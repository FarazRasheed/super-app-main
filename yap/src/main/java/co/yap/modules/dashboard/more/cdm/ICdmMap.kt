package co.yap.modules.dashboard.more.cdm

import android.location.Location
import androidx.lifecycle.MutableLiveData
import co.yap.networking.cards.responsedtos.AtmCdmData
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent
import com.google.android.gms.maps.GoogleMap

interface ICdmMap {
    interface State : IBase.State {
        var stateLiveData: MutableLiveData<co.yap.widgets.State>
        var atmCdmData: AtmCdmData?
        var locationType: MutableLiveData<String>?

    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var mMap: GoogleMap?
        var currentLocation: Location?
        fun handleClickEvent(id: Int)
        fun getCardsAtmCdm(type: String? = null)
    }

    interface View : IBase.View<ViewModel> {

    }
}
