package co.yap.modules.dashboard.cards.reportcard.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.reportcard.activities.ReportLostOrStolenCardActivity.Companion.reportCardSuccess
import co.yap.modules.dashboard.cards.reportcard.interfaces.IRepostOrStolenCard
import co.yap.modules.dashboard.cards.reportcard.viewmodels.ReportLostOrStolenCardViewModels
import co.yap.modules.others.helper.Constants
import co.yap.translation.Strings.screen_report_card_display_button_block_alert_cancel
import co.yap.translation.Strings.screen_report_card_display_button_block_alert_confirm
import co.yap.translation.Strings.screen_report_card_display_text_block_alert_message
import co.yap.translation.Strings.screen_report_card_display_text_block_alert_title
import co.yap.translation.Strings.screen_spare_card_landing_display_text_physical_card
import co.yap.translation.Strings.screen_spare_card_landing_display_text_virtual_card
import co.yap.translation.Translator
import kotlinx.android.synthetic.main.fragment_lost_or_stolen_card.*

class ReportLostOrStolenCardFragment :
    ReportOrLOstCardChildFragment<IRepostOrStolenCard.ViewModel>(), IRepostOrStolenCard.View {
    val REASON_DAMAGE: Int = 2
    val REASON_LOST_STOLEN: Int = 4

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_lost_or_stolen_card

    override val viewModel: ReportLostOrStolenCardViewModels
        get() = ViewModelProviders.of(this).get(ReportLostOrStolenCardViewModels::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.cardType = viewModel.parentViewModel?.card?.cardType ?: ""
        viewModel.state.maskedCardNumber = viewModel.parentViewModel?.card?.maskedCardNo ?: ""

        if (Constants.CARD_TYPE_DEBIT == viewModel.parentViewModel?.card?.cardType) {
            viewModel.state.cardType = Constants.TEXT_PRIMARY_CARD
        } else {
            if (true == viewModel.parentViewModel?.card?.physical) {
                viewModel.state.cardType = Translator.getString(
                    requireContext(),
                    screen_spare_card_landing_display_text_physical_card
                )

            } else {
                viewModel.state.cardType = Translator.getString(
                    requireContext(),
                    screen_spare_card_landing_display_text_virtual_card
                )
            }
        }
        llDamagedCard.setOnClickListener {
            viewModel.state.valid = true
            viewModel.HOT_LIST_REASON = REASON_DAMAGE

            llDamagedCard.isActivated = true
            llStolenCard.isActivated = false
        }

        llStolenCard.setOnClickListener {
            viewModel.state.valid = true
            viewModel.HOT_LIST_REASON = REASON_LOST_STOLEN

            llDamagedCard.isActivated = false
            llStolenCard.isActivated = true
        }

        viewModel.clickEvent.observe(this, Observer {
            when (it) {

//                R.id.llDamagedCard -> {
//                    llDamagedCard.isActivated = true
//                    llStolenCard.isActivated = false
//                }
//
//                R.id.llStolenCard -> {
//                    llDamagedCard.isActivated = false
//                    llStolenCard.isActivated = true
//
//                }

                R.id.btnBlockAndReport -> {
                    showDialog()
                }

                viewModel.CARD_REORDER_SUCCESS -> {

                    llDamagedCard.isActivated = false
                    llStolenCard.isActivated = false

                    if (viewModel.state.cardType == Translator.getString(
                            requireContext(),
                            screen_spare_card_landing_display_text_virtual_card
                        )
                    ) {
                        reportCardSuccess = true
                        setupActionsIntent()
                        activity?.finish()
                    } else {
                        val action =
                            ReportLostOrStolenCardFragmentDirections.actionReportLostOrStolenCardFragmentToBlockCardSuccessFragment(
                                viewModel.cardFee + " AED"
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        })
    }

    private fun showDialog() {

        val builder = AlertDialog.Builder(this.requireActivity())
        builder.setTitle(
            Translator.getString(
                requireContext(), screen_report_card_display_text_block_alert_title
            )
        )
        builder.setMessage(
            Translator.getString(
                requireContext(), screen_report_card_display_text_block_alert_message
            )
        )
        builder.setPositiveButton(
            Translator.getString(
                requireContext(), screen_report_card_display_button_block_alert_confirm
            )
        ) { dialog, which ->
            viewModel.parentViewModel?.card?.let {
                viewModel.requestConfirmBlockCard(it)
            }
        }

        builder.setNegativeButton(
            Translator.getString(
                context!!, screen_report_card_display_button_block_alert_cancel
            )
        ) { dialog, which ->
            //dismiss
        }


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()

    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    private fun setupActionsIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra("cardBlocked", true)
        activity?.setResult(Activity.RESULT_OK, returnIntent)
    }
}