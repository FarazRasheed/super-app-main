package co.yap.sendmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.yap.networking.transactions.responsedtos.purposepayment.PurposeOfPayment
import co.yap.widgets.expandablelist.PopParentAdapter
import co.yap.yapcore.interfaces.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PopListBottomSheet(
    private val mListener: OnItemClickListener,
    private val purposeCategories: Map<String?, List<PurposeOfPayment>>?
) : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_pop, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val titleList = purposeCategories?.keys?.let { ArrayList(it) } ?: arrayListOf()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =
            PopParentAdapter(titleList, purposeCategories, recyclerView, mListener)
        return view
    }

}