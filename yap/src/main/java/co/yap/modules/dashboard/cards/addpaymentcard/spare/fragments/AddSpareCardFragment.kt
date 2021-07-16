package co.yap.modules.dashboard.cards.addpaymentcard.spare.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.addpaymentcard.activities.AddPaymentCardActivity
import co.yap.modules.dashboard.cards.addpaymentcard.activities.AddPaymentCardActivity.Companion.onBackPressCheck
import co.yap.modules.dashboard.cards.addpaymentcard.fragments.AddPaymentChildFragment
import co.yap.modules.dashboard.cards.addpaymentcard.spare.helpers.virtual.AddSpareVirtualCardViewHelper
import co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces.IAddSpareCard
import co.yap.modules.dashboard.cards.addpaymentcard.spare.viewmodels.AddSpareCardViewModel
import co.yap.modules.dashboard.cards.reordercard.activities.ReorderCardActivity
import co.yap.modules.dashboard.yapit.topup.cardslisting.TopUpBeneficiariesActivity
import co.yap.modules.location.activities.LocationSelectionActivity
import co.yap.networking.cards.responsedtos.Address
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.managers.SessionManager

class AddSpareCardFragment : AddPaymentChildFragment<IAddSpareCard.ViewModel>(),
    IAddSpareCard.View {

    private var cardAdded: Boolean = false
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_add_spare_card

    override val viewModel: IAddSpareCard.ViewModel
        get() = ViewModelProviders.of(this).get(AddSpareCardViewModel::class.java)

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.avaialableCardBalance = viewModel.availableBalance
        getUpArguments()
        navController = findNavController()
        setObservers()
    }

    private fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                viewModel.ADD_PHYSICAL_SPARE_SUCCESS_EVENT -> {
                    onAddPhysicalCard()
                }

                viewModel.ADD_VIRTUAL_SPARE_SUCCESS_EVENT -> {
                    onAddVirtualCard()
                }
                viewModel.REORDER_CARD_SUCCESS_EVENT -> {
                    onReorderCard()
                }

                R.id.btnConfirmPhysicalCardPurchase -> {
                    onPressConfirmPurchasePhysical()
                }

                R.id.btnConfirmVirtualCardPurchase -> {
                    onPressConfirmPurchaseVirtual()
                }

                R.id.btnDoneAddingSpareVirtualCard -> {
                    setupActionsIntent()
                    activity?.finish()
                }

                R.id.btnConfirm -> {
                    viewModel.state.toggleVisibility = true
                }

                R.id.tvChangeLocation -> {
                    startActivityForResult(
                        LocationSelectionActivity.newIntent(
                            context = requireContext(),
                            address = viewModel.address ?: Address(),
                            headingTitle = getString(Strings.screen_meeting_location_display_text_add_new_address_title),
                            subHeadingTitle = getString(Strings.screen_meeting_location_display_text_add_new_address_subtitle)
                        ), RequestCodes.REQUEST_FOR_LOCATION
                    )
                }

            }
        })
    }

    private fun onAddPhysicalCard() {
        updateTransactionOnHome()
        SessionManager.updateCardBalance(){}

        if (!viewModel.isFromBlockCardScreen) {
            if (activity is AddPaymentCardActivity) {
                (activity as AddPaymentCardActivity).hideToolbar()
            }
        }
        findNavController().navigate(R.id.action_addSpareCardFragment_to_addSparePhysicalCardSuccessFragment)
    }

    private fun onAddVirtualCard() {
        updateTransactionOnHome()
        SessionManager.updateCardBalance(){}
        if (!viewModel.isFromBlockCardScreen) {
            if (activity is AddPaymentCardActivity)
                (activity as AddPaymentCardActivity).hideToolbar()
        }
        cardAdded = true
        view?.let {
            this.activity?.let { activity ->
                AddSpareVirtualCardViewHelper(
                    activity,
                    navController,
                    it,
                    viewModel
                )
            }
        }
    }

    private fun onReorderCard() {
        updateTransactionOnHome()
        showToast("Reorder Card Successfully")
//        findNavController().navigate(R.id.action_addSpareCardFragment_to_successFragment)
    }

    private fun onPressConfirmPurchaseVirtual() {
        try {
            // todo temporary logic added to fix a bug, balance should be stored as double in view model
            val virtualCardFee =
                viewModel.state.virtualCardFee.replace("${SessionManager.getDefaultCurrency()} ", "").replace(",", "")
                    .toDouble()
            val availableCardBalance =
                viewModel.state.avaialableCardBalance.replace("${SessionManager.getDefaultCurrency()} ", "")
                    .replace(",", "")
                    .toDouble()
            if (virtualCardFee > availableCardBalance) {
                showDialog()
            } else {
                viewModel.requestAddSpareVirtualCard()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onPressConfirmPurchasePhysical() {
        // todo temporary logic added to fix a bug, balance should be stored as double in view model
        try {
            val physicalCardFee =
                viewModel.state.physicalCardFee.replace("${SessionManager.getDefaultCurrency()} ", "").replace(
                    ",",
                    ""
                ).toDouble()
            val availableCardBalance =
                viewModel.state.avaialableCardBalance.replace("${SessionManager.getDefaultCurrency()} ", "").replace(
                    ",",
                    ""
                ).toDouble()

            if (physicalCardFee > availableCardBalance) {
                showDialog()
            } else {
                if (activity is ReorderCardActivity) {
                    viewModel.requestReorderCard()
                } else {
                    viewModel.requestAddSparePhysicalCard()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getUpArguments() {

        viewModel.cardType =
            arguments?.let { AddSpareCardFragmentArgs.fromBundle(it).cardType } as String

        viewModel.state.cardType = viewModel.cardType

//        arguments?.let { AddressSelectionFragmentArgs.fromBundle(it).isFromPhysicalCardsScreen }

        viewModel.isFromBlockCardScreen =
            arguments?.let { AddSpareCardFragmentArgs.fromBundle(it).isFromBlockCard } as Boolean
        viewModel.requestInitialData()

    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
        onBackPressCheck = true

    }

    override fun onBackPressed(): Boolean {
        return if (cardAdded) {
            return false
        } else {
            super.onBackPressed()
        }
    }

    private fun setupActionsIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra("cardAdded", true)
        returnIntent.putExtra("paymentCard", viewModel.paymentCard)
        activity?.setResult(Activity.RESULT_OK, returnIntent)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(this.requireActivity())
        builder.setMessage(
            Translator.getString(
                requireContext(), Strings.
                    screen_add_spare_card_display_text_alert_title
            )
        )
        builder.setPositiveButton(
            Translator.getString(
                requireContext(), Strings.screen_add_spare_card_display_button_block_alert_top_up
            )
        ) { dialog, which ->
            launchActivity<TopUpBeneficiariesActivity>()
//            startActivity(TopUpLandingActivity.getIntent(requireContext()))
        }

        builder.setNegativeButton(
            Translator.getString(
                requireContext(), Strings.screen_add_spare_card_display_button_block_alert_skip
            )
        ) { dialog, which ->
            //dismiss
        }


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCodes.REQUEST_FOR_LOCATION -> {
                    val isUpdated = data?.getBooleanExtra(Constants.ADDRESS_SUCCESS, false)
                    isUpdated?.let { it ->
                        if (it) {
                            val address: Address? =
                                data.getParcelableExtra(Constants.ADDRESS)
                            address?.let {
                                viewModel.address = it
                                setupAddressCard()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAddressCard() {
        viewModel.state.physicalCardAddressTitle = viewModel.address?.address1 ?: ""
        viewModel.state.physicalCardAddressSubTitle = viewModel.address?.address2 ?: ""
        viewModel.state.enableConfirmLocation = true
    }

    private fun updateTransactionOnHome() {
        // Send Broadcast for updating transactions list in `Home Fragment`
        val intent = Intent(Constants.BROADCAST_UPDATE_TRANSACTION)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }
}