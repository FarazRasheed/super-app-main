package co.yap.modules.dashboard.cards.reordercard.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.R
import co.yap.modules.dashboard.cards.reordercard.interfaces.IRenewCard
import co.yap.modules.dashboard.cards.reordercard.viewmodels.RenewCardViewModel
import co.yap.modules.dashboard.yapit.topup.cardslisting.TopUpBeneficiariesActivity
import co.yap.modules.location.activities.LocationSelectionActivity
import co.yap.networking.cards.responsedtos.Address
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BR
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager

class ReorderCardFragment : ReorderCardBaseFragment<IRenewCard.ViewModel>(), IRenewCard.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_reorder_card

    override val viewModel: IRenewCard.ViewModel
        get() = ViewModelProviders.of(this).get(RenewCardViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.reorderCardSuccess.observe(this, Observer {
            if (it) {
                findNavController().navigate(R.id.action_reorderCardFragment_to_reorderCardSuccessFragment)
            }
        })
    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.btnConfirm -> {
                viewModel.state.isAddressConfirm.set(true)
                viewModel.address.address1 = viewModel.state.cardAddressTitle.get()
                viewModel.address.address2 = viewModel.state.cardAddressSubTitle.get()
            }

            R.id.btnConfirmPurchase -> {
                SessionManager.cardBalance.value?.availableBalance?.toDoubleOrNull()
                    ?.let { balance ->
                        viewModel.fee.toDoubleOrNull()?.let { feeAmount ->
                            if (feeAmount <= balance)
                                viewModel.requestReorderCard()
                            else
                                showDialog()
                        }
                    }
            }

            R.id.tvChangeLocation -> {
                val heading = Translator.getString(
                    requireContext(),
                    R.string.screen_meeting_location_display_text_add_new_address_title
                )
                val subHeading = Translator.getString(
                    requireContext(),
                    R.string.screen_meeting_location_display_text_add_new_address_subtitle
                )

                startActivityForResult(
                    LocationSelectionActivity.newIntent(
                        requireContext(),
                        viewModel.address,
                        heading,
                        subHeading
                    ), RequestCodes.REQUEST_FOR_LOCATION
                )
            }
        }
    }

    private fun showDialog() {
        context?.let { it ->
            Utils.confirmationDialog(it, null,
                Translator.getString(
                    requireContext(), Strings.screen_add_spare_card_display_text_alert_title
                )
                , Translator.getString(
                    it,
                    R.string.screen_add_spare_card_display_button_block_alert_top_up
                ), Translator.getString(
                    it,
                    R.string.screen_add_spare_card_display_button_block_alert_skip
                ),
                object : OnItemClickListener {
                    override fun onItemClick(view: View, data: Any, pos: Int) {
                        if (data is Boolean) {
                            if (data) {
                                startActivityForResult(
                                    TopUpBeneficiariesActivity.newIntent(
                                        requireContext(),
                                        getString(Strings.screen_cards_button_reorder_card)
                                    ),
                                    RequestCodes.REQUEST_SHOW_BENEFICIARY
                                )
                                //activity?.let { it.finish() }
                            } else {
                                activity?.let { it.finish() }
                            }
                        }
                    }
                })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
        viewModel.reorderCardSuccess.removeObservers(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodes.REQUEST_FOR_LOCATION -> {
                    val address: Address? =
                        data?.getParcelableExtra(Constants.ADDRESS)
                    address?.let {
                        viewModel.address = it
                        setLocationCardStates()
                    }
                }
                RequestCodes.REQUEST_SHOW_BENEFICIARY -> {
                    activity?.recreate()
                }
            }
        }
    }

    private fun setLocationCardStates() {
        viewModel.state.cardAddressTitle.set(viewModel.address.address1)
        viewModel.state.cardAddressSubTitle.set(viewModel.address.address2)
    }

}