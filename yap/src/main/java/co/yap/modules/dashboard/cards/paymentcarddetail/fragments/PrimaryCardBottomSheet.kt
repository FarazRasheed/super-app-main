package co.yap.modules.dashboard.cards.paymentcarddetail.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.yap.R
import co.yap.modules.others.helper.Constants
import co.yap.yapcore.enums.CardStatus
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_primary_card.view.*

class PrimaryCardBottomSheet(private val cardStatus: String, private val mListener: CardClickListener) :
    BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_primary_card, container, false)

        if(cardStatus == CardStatus.EXPIRED.name){
            view.tvChangePin.visibility = View.GONE
            view.tvForgotCardPin.visibility = View.GONE
        }
        view.tvAddCardName.setOnClickListener { mListener.onClick(Constants.EVENT_ADD_CARD_NAME)}
        view.tvChangePin.setOnClickListener { mListener.onClick(Constants.EVENT_CHANGE_PIN)}
        view.tvForgotCardPin.setOnClickListener { mListener.onClick(Constants.EVENT_FORGOT_CARD_PIN)}

        view.tvViewStatements.setOnClickListener { mListener.onClick(Constants.EVENT_VIEW_STATEMENTS)}
        view.tvReportCard.setOnClickListener { mListener.onClick(Constants.EVENT_REPORT_CARD)}
        return view
    }
}