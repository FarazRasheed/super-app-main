package co.yap.modules.dashboard.store.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.store.adaptor.YapStoreAdaptor
import co.yap.modules.dashboard.store.interfaces.IYapStore
import co.yap.modules.dashboard.store.viewmodels.YapStoreViewModel
import co.yap.networking.store.responsedtos.Store
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_yap_store.*

class YapStoreFragment : BaseBindingFragment<IYapStore.ViewModel>(), IYapStore.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_yap_store

    override val viewModel: IYapStore.ViewModel
        get() = ViewModelProviders.of(this).get(YapStoreViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStoreList()
        initComponents()
        setObservers()
    }

    private fun initComponents() {
        recycler_stores.adapter = YapStoreAdaptor(mutableListOf())
        (recycler_stores.adapter as YapStoreAdaptor).allowFullItemClickListener = true
        (recycler_stores.adapter as YapStoreAdaptor).setItemListener(listener)
    }

    private fun setObservers() {
        viewModel.storesLiveData.observe(this, Observer {
            (recycler_stores.adapter as YapStoreAdaptor).setList(it)
        })
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is Store) {
//                if (data.name == "YAP Household") {
//                    startActivityForResult(
//                        HouseHoldLandingActivity.newIntent(requireContext()),
//                        RequestCodes.REQUEST_ADD_HOUSE_HOLD
//                    )
//                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.REQUEST_ADD_HOUSE_HOLD) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    val finishScreen =
                        data.getValue(
                            RequestCodes.REQUEST_CODE_FINISH,
                            ExtraType.BOOLEAN.name
                        ) as? Boolean
                    finishScreen?.let { it ->
                        if (it) {
                            //finish()  // Transaction fragment
                        } else {
                            // other things?
                        }
                    }
                }
            }
        }
    }


    private fun getRecycleViewAdaptor(): YapStoreAdaptor? {
        return if (recycler_stores.adapter is YapStoreAdaptor) {
            (recycler_stores.adapter as YapStoreAdaptor)
        } else {
            null
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivRightIcon -> {
                Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
            }
        }
    }
}