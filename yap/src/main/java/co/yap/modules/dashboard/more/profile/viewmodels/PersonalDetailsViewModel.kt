package co.yap.modules.dashboard.more.profile.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.R
import co.yap.modules.dashboard.more.main.viewmodels.MoreBaseViewModel
import co.yap.modules.dashboard.more.profile.intefaces.IPersonalDetail
import co.yap.modules.dashboard.more.profile.states.PersonalDetailState
import co.yap.networking.cards.CardsRepository
import co.yap.networking.cards.responsedtos.Address
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.models.RetroApiResponse
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.EIDStatus
import co.yap.yapcore.managers.SessionManager

class PersonalDetailsViewModel(application: Application) :
    MoreBaseViewModel<IPersonalDetail.State>(application), IPersonalDetail.ViewModel,
    IRepositoryHolder<CardsRepository> {

    override var UPDATE_ADDRESS_UI: Int = 10
    override var onUpdateAddressSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    override val orderCardSuccess: MutableLiveData<Boolean> = MutableLiveData()



    override val repository: CardsRepository = CardsRepository
    var address: Address? = null

    override fun onCreate() {
        super.onCreate()
        requestGetAddressForPhysicalCard()
    }

    override fun handlePressOnScanCard(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnEditPhone(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnEditEmail(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnEditAddress(id: Int) {
        clickEvent.setValue(id)
    }

    override fun handlePressOnDocumentCard(id: Int) {
        clickEvent.setValue(id)
    }

    override val state: PersonalDetailState = PersonalDetailState(application)

    override val clickEvent: SingleClickEvent = SingleClickEvent()

    override fun handlePressOnBackButton() {

    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(Strings.screen_personal_detail_display_text_title))
        state.fullName = SessionManager.user?.currentCustomer?.getFullName() ?: ""
        state.phoneNumber =
            SessionManager.user?.currentCustomer?.getFormattedPhoneNumber(context) ?: ""
        state.email = SessionManager.user?.currentCustomer?.email ?: ""
        state.fullName = SessionManager.user?.currentCustomer?.getFullName() ?: ""
        state.phoneNumber =
            SessionManager.user?.currentCustomer?.getFormattedPhoneNumber(context) ?: ""
        state.email = SessionManager.user?.currentCustomer?.email ?: ""
        setUpVerificationLayout()
    }

    private fun requestGetAddressForPhysicalCard() {
        state.loading = true
        launch {
            when (val response = repository.getUserAddressRequest()) {
                is RetroApiResponse.Success -> {

                    address = response.data.data
                    setUpAddressFields()
                    SessionManager.userAddress = address
                    clickEvent.setValue(UPDATE_ADDRESS_UI)
                    state.loading = false
                }
                is RetroApiResponse.Error -> {
                    state.toast =
                        "${response.error.message}^${AlertType.DIALOG.name}"
                    state.loading = false
                }
            }
        }
    }

    private fun setUpAddressFields() {
        state.address = address?.getCompleteAddress() ?: ""
//        state.address = when {
//            address?.address1 == address?.address2 -> address?.address1 ?: ""
//            address?.address2.isNullOrBlank() && address?.address1.isNullOrBlank() -> ""
//            address?.address1.isNullOrBlank() -> address?.address2 ?: ""
//            address?.address2.isNullOrBlank() -> address?.address1 ?: ""
//            else -> "${address?.address1}, ${address?.address2}"
//
//        }
    }

    override fun toggleToolBar(hide: Boolean) {
        toggleToolBarVisibility(hide)
    }

    override fun updateToolBarText(heading: String) {
        setToolBarTitle(heading)
    }

    override fun requestUpdateAddress(address: Address) {
        launch {
            state.loading = true
            when (val response = repository.editAddressRequest(address)) {
                is RetroApiResponse.Success -> {
                    state.loading = false
                    onUpdateAddressSuccess.value = true

                }
                is RetroApiResponse.Error -> {
                    onUpdateAddressSuccess.value = false
                    state.error = response.error.message
                    state.loading = false
                }
            }
        }
    }

    override fun requestOrderCard(address: Address?) {
        address?.let {
            it.cardName = ""
            launch {
                state.loading = true
                when (val response = repository.orderCard(it)) {
                    is RetroApiResponse.Success -> {
                        orderCardSuccess.value = true
                        state.loading = false
                    }

                    is RetroApiResponse.Error -> {
                        state.loading = false
                        orderCardSuccess.value = false
                        state.toast = "${response.error.message}^${AlertType.DIALOG.name}"
                    }
                }
            }
        }
    }

    override fun setUpVerificationLayout() {
        when (SessionManager.eidStatus) {
            EIDStatus.EXPIRED -> populateExpiredDocumentData()
            EIDStatus.VALID -> populateVerifiedDocumentData()
            EIDStatus.NOT_SET -> populateRequiredDocumentData()
        }
    }

    private fun populateVerifiedDocumentData() {
        //context.resources.getDrawable(co.yap.yapcore.R.drawable.bg_funds, null)
        state.drawbleRight =
            context.resources.getDrawable(R.drawable.ic_achievement_completed_personal_details)
        state.verificationText = Translator.getString(
            context,
            Strings.screen_personal_details_display_text_emirates_id_details_update
        )
    }

    private fun populateExpiredDocumentData() {
        state.drawbleRight =
            context.resources.getDrawable(R.drawable.ic_doc_error)
        state.verificationText = Translator.getString(
            context,
            Strings.screen_personal_details_display_text_expired_emirates_id_details
        )
    }

    private fun populateRequiredDocumentData() {
        state.drawbleRight =
            context.resources.getDrawable(R.drawable.ic_doc_error)
        state.verificationText = Translator.getString(
            context,
            Strings.screen_personal_details_display_text_required_emirates_id_details
        )
    }

}