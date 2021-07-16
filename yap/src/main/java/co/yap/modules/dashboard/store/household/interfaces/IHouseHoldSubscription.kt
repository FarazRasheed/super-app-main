package co.yap.modules.dashboard.store.household.interfaces

import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import co.yap.modules.onboarding.models.WelcomeContent
import co.yap.networking.household.responsedtos.HouseHoldPlan
import co.yap.yapcore.IBase
import co.yap.yapcore.SingleClickEvent

interface IHouseHoldSubscription {
    interface State : IBase.State {
        var monthlyFee: String
        var annuallyFee: String
        var subscriptionFee: String
        var hasSelectedPackage: Boolean
        var planDiscount:String
        var valid: Boolean
    }

    interface ViewModel : IBase.ViewModel<State> {
        val clickEvent: SingleClickEvent
        var benefitsList: ArrayList<BenefitsModel>
        var plansList:ArrayList<HouseHoldPlan>
        fun handlePressOnCloseIcon(id: Int)
        fun handlePressOnMonthlyPackage(id: Int)
        fun handlePressOnYearlyPackage(id: Int)
        fun handlePressOnGetStarted(id: Int)
        fun loadDummyData(): ArrayList<BenefitsModel>
        fun getPages(): ArrayList<WelcomeContent>
        fun fetchHouseholdPackagesFee()

    }

    interface View : IBase.View<ViewModel>

}