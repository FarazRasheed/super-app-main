package co.yap.modules.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.yap.networking.customers.responsedtos.City
import co.yap.yapcore.R
import co.yap.yapcore.interfaces.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class CitiesListBottomSheet(
    private val mListener: OnItemClickListener,
    private val citiesList: ArrayList<City>
) : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val adapter = CitiesAdapter(citiesList.toMutableList())
        val view = inflater.inflate(R.layout.bottom_sheet_cities, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.allowFullItemClickListener = true
        adapter.onItemClickListener = myListener
        recyclerView.adapter = adapter
        return view
    }

    private val myListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            mListener.onItemClick(view, this@CitiesListBottomSheet, pos)
        }
    }

}