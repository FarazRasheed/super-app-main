package co.yap.modules.dashboard.cards.paymentcarddetail.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.yap.R
import co.yap.modules.others.helper.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_spare_card.view.*

class SpareCardBottomSheet(private val physical : Boolean, private val mListener: CardClickListener) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_spare_card, container, false)

        if(!physical){
            view.tvChangePin.visibility = View.GONE
            view.tvReportCard.visibility = View.GONE
        }
        view.tvAddCardName.setOnClickListener { mListener.onClick(Constants.EVENT_ADD_CARD_NAME)}
        view.tvChangePin.setOnClickListener { mListener.onClick(Constants.EVENT_CHANGE_PIN)}
        view.tvViewStatements.setOnClickListener { mListener.onClick(Constants.EVENT_VIEW_STATEMENTS)}
        view.tvReportCard.setOnClickListener { mListener.onClick(Constants.EVENT_REPORT_CARD)}
        view.tvRemoveCard.setOnClickListener { mListener.onClick(Constants.EVENT_REMOVE_CARD)}
        return view
    }
}