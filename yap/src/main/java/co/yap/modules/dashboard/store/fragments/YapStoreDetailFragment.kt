package co.yap.modules.dashboard.store.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.store.adaptor.YapStoreDetailAdaptor
import co.yap.modules.dashboard.store.interfaces.IYapStoreDetail
import co.yap.modules.dashboard.store.viewmodels.YapStoreDetailViewModel
import co.yap.networking.store.responsedtos.Store
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_yap_store.*

class YapStoreDetailFragment : BaseBindingFragment<IYapStoreDetail.ViewModel>(),
    IYapStoreDetail.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_yap_store_detail

    override val viewModel: IYapStoreDetail.ViewModel
        get() = ViewModelProviders.of(this).get(YapStoreDetailViewModel::class.java)

    override var testValue: String = "test value"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clickEvent.observe(this, observer)
        val storeId = arguments?.getString("storeId", "")

        val data: Store? = viewModel.yapStoreData.find { (it.id == storeId!!.toInt()) }
        if (data != null) {
            viewModel.state.title = data.name.toString()
            viewModel.state.subTitle = data.desc.toString()
            viewModel.state.image = data.image?: 0
            viewModel.state.storeHeading =
                "Allocate specific budget to your child, track and fully."
            viewModel.state.storeDetail =
                "Allocate specific budget to your child, track and fully control your child spending by setting limits to the card. Save updatedDate with an innovative real-updatedDate money request system. Allocate specific budget to your child, track and fully control your child spending by setting limits to the card. Save updatedDate with an innovative real-updatedDate money request."
            viewModel.state.storeIcon = R.drawable.ic_young_smile
        }
        setupRecycleView()
    }

    private fun setupRecycleView() {
        val storeAdaptor = YapStoreDetailAdaptor(viewModel.yapStoreData)
        recycler_stores.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recycler_stores.adapter = storeAdaptor
        storeAdaptor.setItemListener(listener)
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            showToast("List item $pos clicked")
        }
    }

    private val observer = Observer<Int> {
        when (it) {
            R.id.imgCross -> {
                findNavController().navigateUp()
                showToast("Cross Button Clicked")
            }
            R.id.imgCheckout -> {
                showToast("Checkout Button Clicked")
            }
            R.id.btnActivate -> {
                showToast("Activate Button Clicked")
            }
            R.id.btnActivateMe -> {
                showToast("Activate Button Clicked")
            }
        }
    }
}