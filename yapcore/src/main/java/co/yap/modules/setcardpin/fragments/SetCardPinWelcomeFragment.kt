package co.yap.modules.setcardpin.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.yap.modules.setcardpin.activities.SetPinChildFragment
import co.yap.modules.setcardpin.interfaces.ISetCardPinWelcome
import co.yap.modules.setcardpin.viewmodels.SetCardPinWelcomeViewModel
import co.yap.yapcore.AdjustEvents.Companion.trackAdjustPlatformEvent
import co.yap.yapcore.BR
import co.yap.yapcore.R
import co.yap.yapcore.adjust.AdjustEvents

class SetCardPinWelcomeFragment : SetPinChildFragment<ISetCardPinWelcome.ViewModel>(),
    ISetCardPinWelcome.View {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_set_card_pin_welcome

    override val viewModel: SetCardPinWelcomeViewModel
        get() = ViewModelProviders.of(this).get(SetCardPinWelcomeViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.parentViewModel?.skipWelcome == true) {
            skipWelcomeScreen()
        }
    }

    private fun skipWelcomeScreen() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.setCardPinWelcomeFragment, true)
            .build()

        findNavController().navigate(
            R.id.action_setCardPinWelcomeFragment_to_setCardPinFragment,
            null,
            navOptions
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnCreatePin -> {
                    trackAdjustPlatformEvent(AdjustEvents.SET_PIN_START.type)
                    findNavController().navigate(R.id.action_setCardPinWelcomeFragment_to_setCardPinFragment)

                }
                R.id.tvCreatePinLater -> activity?.finish()
            }
        })
    }

    override fun onDestroyView() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroyView()
    }
}