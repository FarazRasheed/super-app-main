package co.yap.modules.dashboard.cards.analytics.main.states

import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import co.yap.BR
import co.yap.modules.dashboard.cards.analytics.main.interfaces.ICardAnalyticsMain
import co.yap.yapcore.BaseState


class CardAnalyticsMainState : BaseState(), ICardAnalyticsMain.State {
    override var toolbarVisibility: ObservableBoolean = ObservableBoolean()
    override var leftButtonVisibility: ObservableBoolean = ObservableBoolean()

    @get:Bindable
    override var currentSelectedMonth: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentSelectedMonth)
        }

    @get:Bindable
    override var currentSelectedDate: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentSelectedDate)
        }
}