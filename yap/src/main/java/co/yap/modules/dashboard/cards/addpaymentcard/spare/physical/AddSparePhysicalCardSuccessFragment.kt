package co.yap.modules.dashboard.cards.addpaymentcard.spare.physical

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.yap.R
import co.yap.modules.dashboard.cards.addpaymentcard.activities.AddPaymentCardActivity
import co.yap.modules.dashboard.cards.reportcard.activities.ReportLostOrStolenCardActivity.Companion.reportCardSuccess
import co.yap.translation.Strings.screen_spare_card_landing_display_text_physical_card
import co.yap.translation.Translator
import co.yap.yapcore.defaults.DefaultFragment
import kotlinx.android.synthetic.main.fragment_add_physical_spare_card_success.*
import kotlinx.android.synthetic.main.layout_add_spare_card_header.view.*
import kotlinx.android.synthetic.main.layout_add_spare_physical_card_success.*


class AddSparePhysicalCardSuccessFragment : DefaultFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(
            R.layout.fragment_add_physical_spare_card_success,
            container, false
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        include.labelCardType.setText(
            Translator.getString(
                context!!,
                screen_spare_card_landing_display_text_physical_card
            )
        )

        btnDoneAddingSparePhysicalCard.setOnClickListener {
            // Spare physical card added event
            reportCardSuccess=true
            setupActionsIntent()
            activity?.finish()
        }
    }

    override fun onBackPressed(): Boolean {
        /*setupActionsIntent()
        activity!!.finish()*/
        return true
    }

    private fun setupActionsIntent() {
        val returnIntent = Intent()
        returnIntent.putExtra("cardAdded", true)
        activity?.setResult(Activity.RESULT_OK, returnIntent)
    }
}