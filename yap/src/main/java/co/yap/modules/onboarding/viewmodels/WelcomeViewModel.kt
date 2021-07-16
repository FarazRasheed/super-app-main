package co.yap.modules.onboarding.viewmodels

import android.app.Application
import co.yap.R
import co.yap.modules.onboarding.enums.AccountType
import co.yap.modules.onboarding.interfaces.IWelcome
import co.yap.modules.onboarding.models.WelcomeContent
import co.yap.modules.onboarding.states.WelcomeState
import co.yap.translation.Strings
import co.yap.yapcore.BaseViewModel
import co.yap.yapcore.SingleLiveEvent

class WelcomeViewModel(application: Application) : BaseViewModel<IWelcome.State>(application),
    IWelcome.ViewModel {

    override var onGetStartedPressEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    override lateinit var accountType: AccountType
    override val state: IWelcome.State get() = WelcomeState()

    override fun handlePressOnGetStarted() {
        onGetStartedPressEvent.value = true
    }

    override fun getPages(): ArrayList<WelcomeContent> =
        if (accountType == AccountType.B2C_ACCOUNT) generateB2CPages() else generateB2BPages()

    private fun generateB2BPages(): ArrayList<WelcomeContent> {
        val content1 = WelcomeContent(
            getString(Strings.screen_welcome_b2b_display_text_page1_title),
            getString(Strings.screen_welcome_b2b_display_text_page1_details),
            R.drawable.ic_real_time_banking
        )
        val content2 = WelcomeContent(
            getString(Strings.screen_welcome_b2b_display_text_page2_title),
            getString(Strings.screen_welcome_b2b_display_text_page2_details),
            R.drawable.ic_real_time_perks
        )
        val content3 = WelcomeContent(
            getString(Strings.screen_welcome_b2b_display_text_page3_title),
            getString(Strings.screen_welcome_b2b_display_text_page3_details),
            R.drawable.ic_real_time_benefits
        )
        return arrayListOf(content1, content2, content3)
    }

    private fun generateB2CPages(): ArrayList<WelcomeContent> {
        val content1 = WelcomeContent(
            getString(Strings.screen_welcome_b2c_display_text_page1_title),
            getString(Strings.screen_welcome_b2c_display_text_page1_details),
            R.drawable.ic_real_time_banking
        )
        val content2 = WelcomeContent(
            getString(Strings.screen_welcome_b2c_display_text_page2_title),
            getString(Strings.screen_welcome_b2c_display_text_page2_details),
            R.drawable.ic_real_time_perks
        )
        val content3 = WelcomeContent(
            getString(Strings.screen_welcome_b2c_display_text_page3_title),
            getString(Strings.screen_welcome_b2c_display_text_page3_details),
            R.drawable.ic_real_time_benefits
        )
        return arrayListOf(content1, content2, content3)
    }
}