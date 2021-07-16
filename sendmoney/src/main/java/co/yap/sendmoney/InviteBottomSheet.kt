package co.yap.sendmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_invite_friend.view.*

class InviteBottomSheet(
    private val mListener: OnItemClickListener,
    private val T: Any
) : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_invite_friend, container, false)

        view.tvChooseEmail.setOnClickListener { mListener.onClick(view.tvChooseEmail.id, T) }
        view.tvChooseSMS.setOnClickListener { mListener.onClick(view.tvChooseSMS.id, T) }
        view.tvChooseWhatsapp.setOnClickListener {
            mListener.onClick(
                view.tvChooseWhatsapp.id,
                T
            )
        }
        return view
    }

    interface OnItemClickListener {
        fun onClick(viewId: Int, T: Any)
    }
}