package co.yap.modules.kyc.viewmodels

import android.app.Application
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import co.yap.R
import co.yap.modules.kyc.interfaces.IMapDetailView
import co.yap.modules.kyc.states.AddressSelectionState
import co.yap.modules.kyc.states.MapDetailViewState
import co.yap.translation.Translator
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleClickEvent

class MapDetailViewModel(application: Application) : BaseViewModel<IMapDetailView.State>(application),
    IMapDetailView.ViewModel {

    override val state: MapDetailViewState = MapDetailViewState(application)

    fun onLocatioenSelected() {
        // aalso visible faade in location button
        state.headingTitle = Translator.getString(getApplication(), R.string.screen_meeting_location_display_text_title)
        state.subHeadingTitle =
            Translator.getString(getApplication(), R.string.screen_meeting_location_display_text_selected_subtitle)
        state.locationBtnText =
            Translator.getString(getApplication(), R.string.screen_meeting_location_button_change_location)
    }

    override val clickEvent: SingleClickEvent = SingleClickEvent()


    override fun handlePressOnSelectLocation(id: Int) {
        clickEvent.setValue(id)
        onLocatioenSelected()
    }

    override fun handlePressOnNext(id: Int) {
        clickEvent.setValue(id)
        //            onLocatioenSelected()
//           start new fragment in sequeence
    }

    fun handlePressOnChangeLocation() {
        state.locationBtnText = getString(R.string.screen_meeting_location_button_change_location)
    }

    override fun onEditorActionListener(): TextView.OnEditorActionListener {
        return object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (state.valid) {
//           start new fragment in sequeence
                    }
                }
                return false
            }
        }
    }

}