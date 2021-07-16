package co.yap.modules.dashboard.more.help.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import co.yap.R
import co.yap.modules.dashboard.more.help.interfaces.IHelpSupport
import co.yap.modules.dashboard.more.help.states.HelpSupportState
import co.yap.modules.dashboard.more.main.viewmodels.MoreBaseViewModel
import co.yap.networking.authentication.AuthApi
import co.yap.networking.authentication.AuthRepository
import co.yap.networking.interfaces.IRepositoryHolder
import co.yap.networking.messages.MessagesRepository
import co.yap.networking.models.RetroApiResponse
import co.yap.yapcore.SingleClickEvent

class HelpSupportViewModel(application: Application) :
    MoreBaseViewModel<IHelpSupport.State>(application), IHelpSupport.ViewModel,
    IRepositoryHolder<MessagesRepository> {

    override val repository: MessagesRepository = MessagesRepository
    override val clickEvent: SingleClickEvent = SingleClickEvent()
    override val urlUpdated: MutableLiveData<String> = MutableLiveData()
    override val authRepository: AuthApi = AuthRepository
    override val state: HelpSupportState = HelpSupportState()

    init {
        state.contactPhone.set("")
        state.toolbarTitle = getString(R.string.screen_help_support_display_text_title)
    }

    override fun handlePressOnView(id: Int) {
        clickEvent.setValue(id)
    }

    override fun getHelpDeskPhone() {
        launch {
            state.loading = true
            when (val response =
                repository.getHelpDeskContact()) {
                is RetroApiResponse.Error -> {
                    state.loading = false

                }
                is RetroApiResponse.Success -> {
                    state.loading = false
                    response.data.data?.let { state.contactPhone.set(it) }
                }
            }
        }
    }

    override fun getFaqsUrl() {
        launch {
            state.loading = true
            when (val response =
                repository.getFaqsUrl()) {
                is RetroApiResponse.Error -> {
                    state.loading = false
                }
                is RetroApiResponse.Success -> {
                    state.loading = false
                    response.data.data?.let { urlUpdated.value = it }
                }
            }
        }
    }
}