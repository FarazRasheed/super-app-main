package co.yap.household.dashboard.home.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.household.dashboard.home.interfaces.IHouseholdHome
import co.yap.household.dashboard.home.states.HouseholdHomeState
import co.yap.household.dashboard.main.viewmodels.HouseholdDashboardBaseViewModel
import co.yap.modules.yapnotification.models.Notification
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.PartnerBankStatus
import co.yap.yapcore.managers.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class HouseholdHomeViewModel(application: Application) :
    HouseholdDashboardBaseViewModel<IHouseholdHome.State>(application),
    IHouseholdHome.ViewModel {
    override val state: HouseholdHomeState =
        HouseholdHomeState()
    override var clickEvent: SingleClickEvent = SingleClickEvent()
    override var viewState: MutableLiveData<Int> = MutableLiveData(Constants.EVENT_LOADING)
    override var notificationList: MutableLiveData<ArrayList<Notification>> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        requestTransactions()
    }

    override fun onResume() {
        super.onResume()
        if (Constants.USER_STATUS_CARD_ACTIVATED == SessionManager.user?.notificationStatuses)
            checkUserStatus()
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    private fun checkUserStatus() {
        when (SessionManager.user?.notificationStatuses) {
            Constants.USER_STATUS_ON_BOARDED -> {
                viewState.value = Constants.EVENT_EMPTY
                notificationList.value?.add(
                    Notification(
                        "Set your card PIN",
                        "Now create a unique 4-digit PIN code to be able to use your debit card for purchases and withdrawals",
                        "",
                        Constants.NOTIFICATION_ACTION_SET_PIN,
                        "",
                        ""
                    )
                )

            }
            Constants.USER_STATUS_MEETING_SUCCESS -> {
                viewState.value = Constants.EVENT_EMPTY
                notificationList.value?.add(
                    Notification(
                        "Complete Verification",
                        "Complete verification to activate your account",
                        "",
                        Constants.NOTIFICATION_ACTION_COMPLETE_VERIFICATION,
                        "",
                        ""
                    )
                )
            }
            Constants.USER_STATUS_MEETING_SCHEDULED -> {
                viewState.value = Constants.EVENT_EMPTY
                notificationList.value?.clear()
            }
            Constants.USER_STATUS_CARD_ACTIVATED -> {
                viewState.value = Constants.EVENT_EMPTY
                notificationList.value?.clear()
            }
        }

        if (PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus) {
            viewState.value = Constants.EVENT_CONTENT
            notificationList.value?.clear()
        } else {
            viewState.value = Constants.EVENT_EMPTY
        }
    }

    override fun requestTransactions() {
        launch {
            viewState.value = Constants.EVENT_LOADING
            delay(2000)
            viewModelBGScope.async(Dispatchers.IO) {
                viewModelBGScope.close()
            }
            viewState.value = Constants.EVENT_EMPTY
        }
    }
}