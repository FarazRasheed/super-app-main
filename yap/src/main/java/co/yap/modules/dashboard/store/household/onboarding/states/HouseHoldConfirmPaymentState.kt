package co.yap.modules.dashboard.store.household.onboarding.states

import android.text.SpannableStringBuilder
import androidx.databinding.ObservableField
import co.yap.modules.dashboard.store.household.onboarding.interfaces.IHouseHoldConfirmPayment
import co.yap.yapcore.BaseState
import co.yap.yapcore.managers.SessionManager

class HouseHoldConfirmPaymentState : BaseState(), IHouseHoldConfirmPayment.State {
    override var selectedCardPlan: ObservableField<String> = ObservableField("")
    override var selectedPlanSaving: ObservableField<String> = ObservableField("")
    override var selectedPlanFee: ObservableField<String> = ObservableField("")
    override var availableBalance: ObservableField<SpannableStringBuilder> = ObservableField(SpannableStringBuilder.valueOf(""))
    override var currencyType: ObservableField<String> = ObservableField(SessionManager.getDefaultCurrency())
}