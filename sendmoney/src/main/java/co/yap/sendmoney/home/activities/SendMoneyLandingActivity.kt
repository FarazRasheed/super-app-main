package co.yap.sendmoney.home.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.activities.SendMoneyHomeActivity
import co.yap.sendmoney.databinding.ActivitySendMoneyLandingBinding
import co.yap.sendmoney.editbeneficiary.activity.EditBeneficiaryActivity
import co.yap.sendmoney.fundtransfer.activities.BeneficiaryFundTransferActivity
import co.yap.sendmoney.home.adapters.AllBeneficiariesAdapter
import co.yap.sendmoney.home.adapters.RecentTransferAdaptor
import co.yap.sendmoney.home.interfaces.ISendMoneyHome
import co.yap.sendmoney.home.viewmodels.SendMoneyHomeScreenViewModel
import co.yap.translation.Translator
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.SingleClickEvent
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.Constants.EXTRA
import co.yap.yapcore.constants.Constants.OVERVIEW_BENEFICIARY
import co.yap.yapcore.constants.RequestCodes
import co.yap.yapcore.constants.RequestCodes.REQUEST_TRANSFER_MONEY
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.getBeneficiaryTransferType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.helpers.extentions.showBlockedFeatureAlert
import co.yap.yapcore.interfaces.OnItemClickListener
import co.yap.yapcore.managers.SessionManager
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import kotlinx.android.synthetic.main.item_beneficiaries.*
import kotlinx.android.synthetic.main.layout_beneficiaries.*

class SendMoneyLandingActivity : BaseBindingActivity<ISendMoneyHome.ViewModel>(),
    ISendMoneyHome.View {

    private var positionToDelete = 0
    private lateinit var onTouchListener: RecyclerTouchListener
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_send_money_landing

    override val viewModel: SendMoneyHomeScreenViewModel
        get() = ViewModelProviders.of(this).get(SendMoneyHomeScreenViewModel::class.java)

    companion object {
        const val searching = "searching"
        private var performedDeleteOperation: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
        viewModel.isSearching.value = intent.getBooleanExtra(searching, false)
        viewModel.isSearching.value?.let {
            viewModel.state.isSearching.set(it)
        }
        setObservers()
    }

    private fun initComponents() {
        getBinding().layoutBeneficiaries.rvAllBeneficiaries.adapter =
            AllBeneficiariesAdapter(mutableListOf())
        initSwipeListener()
    }

    private fun setObservers() {
        viewModel.clickEvent.observe(this, clickListener)
        viewModel.onDeleteSuccess.observe(this, Observer {
            getAdaptor().removeItemAt(positionToDelete)
            performedDeleteOperation = true
            if (positionToDelete == 0)
                viewModel.requestAllBeneficiaries()
        })
        //Beneficiaries list observer
        viewModel.allBeneficiariesLiveData.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                // show and hide views for no beneficiary
                viewModel.state.isNoBeneficiary.set(true)
                viewModel.state.hasBeneficiary.set(false)
            } else {
                viewModel.state.isNoBeneficiary.set(false)
                viewModel.state.hasBeneficiary.set(true)
                getAdaptor().setList(it)
            }
            setSearchView(viewModel.isSearching.value ?: false)
        })
        //Beneficiaries list Search Query observer
        viewModel.searchQuery.observe(this, Observer {
            getAdaptor().filter.filter(it)
        })

        //Recent Beneficiaries list observer
        viewModel.recentTransferData.observe(this, Observer {
            if (it.isNullOrEmpty()) return@Observer
            val adapter = RecentTransferAdaptor(
                it.toMutableList(),
                null
            )
            adapter.onItemClickListener = recentItemClickListener
            viewModel.adapter.set(adapter)

        })
        //Searching Beneficiaries list Results Count observer
        viewModel.isSearching.value?.let { isSearching ->
            if (isSearching) {
                getAdaptor().filterCount.observe(this, Observer {
                    getBinding().layoutBeneficiaries.txtError.visibility =
                        if (it == 0) View.VISIBLE else View.GONE
                })
            }
        }
    }

    private fun setSearchView(show: Boolean) {
        if (!show) {
            getSearchView().isIconified = true
            getBinding().run {
                getSearchView().setIconifiedByDefault(false)
            }
        } else {
            getSearchView().isIconified = false
            getBinding().run { getSearchView().setIconifiedByDefault(false) }
            getSearchView().setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.searchQuery.value = query
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.searchQuery.value = newText
                    return true
                }
            })

        }
    }

    private val recentItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (data is Beneficiary)
                startMoneyTransfer(data, pos)
        }
    }


    private fun initSwipeListener() {
        onTouchListener = RecyclerTouchListener(this, rvAllBeneficiaries)
            .setClickable(
                object : RecyclerTouchListener.OnRowClickListener {
                    override fun onRowClicked(position: Int) {
                        viewModel.clickEvent.setPayload(
                            SingleClickEvent.AdaptorPayLoadHolder(
                                foregroundContainer,
                                getAdaptor().getDataForPosition(position),
                                position
                            )
                        )
                        viewModel.clickEvent.setValue(foregroundContainer.id)
                    }

                    override fun onIndependentViewClicked(
                        independentViewID: Int,
                        position: Int
                    ) {
                    }
                }).setSwipeOptionViews(R.id.btnEdit, R.id.btnDelete)
            .setSwipeable(
                R.id.foregroundContainer, R.id.swipe
            )
            { viewID, position ->
                viewModel.clickEvent.setPayload(
                    SingleClickEvent.AdaptorPayLoadHolder(
                        findViewById(viewID),
                        getAdaptor().getDataForPosition(position),
                        position
                    )
                )
                viewModel.clickEvent.setValue(viewID)
            }
    }

    private fun startMoneyTransfer(beneficiary: Beneficiary?, position: Int) {
        Utils.hideKeyboard(getSearchView())
        launchActivity<BeneficiaryFundTransferActivity>(
            requestCode = REQUEST_TRANSFER_MONEY,
            type = beneficiary.getBeneficiaryTransferType()
        ) {
            putExtra(Constants.BENEFICIARY, beneficiary)
            putExtra(Constants.POSITION, position)
            putExtra(Constants.IS_NEW_BENEFICIARY, false)
        }
    }

    private fun openEditBeneficiary(beneficiary: Beneficiary?) {
        Utils.hideKeyboard(getSearchView())
        beneficiary?.let {
            val bundle = Bundle()
            bundle.putBoolean(OVERVIEW_BENEFICIARY, false)
            bundle.putString(Constants.IS_IBAN_NEEDED, "loadFromServer")
            bundle.putParcelable(Beneficiary::class.java.name, beneficiary)
            launchActivity<EditBeneficiaryActivity>(
                requestCode = RequestCodes.REQUEST_NOTIFY_BENEFICIARY_LIST,
                type = FeatureSet.EDIT_SEND_MONEY_BENEFICIARY
            ) {
                putExtra(EXTRA, bundle)
            }
        }
    }

    private fun confirmDeleteBeneficiary(beneficiary: Beneficiary) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(
                Translator.getString(
                    this,
                    R.string.screen_send_money_display_text_delete
                )
            )
            .setMessage(
                Translator.getString(
                    this,
                    R.string.screen_send_money_display_text_delete_message
                )
            )
            .setPositiveButton(
                Translator.getString(
                    this,
                    R.string.common_button_yes
                )
            ) { dialog, which ->
                viewModel.requestDeleteBeneficiary(beneficiary.id ?: 0)
            }

            .setNegativeButton(
                Translator.getString(
                    this,
                    R.string.common_button_cancel
                ),
                null
            )
            .show()
    }

    override fun onPause() {
        rvAllBeneficiaries.removeOnItemTouchListener(onTouchListener)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        rvAllBeneficiaries.addOnItemTouchListener(onTouchListener)
        viewModel.state.isSearching.notifyChange()
        if (performedDeleteOperation) {
            performedDeleteOperation = false
            viewModel.requestAllBeneficiaries()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
        viewModel.onDeleteSuccess.removeObservers(this)
    }

    private val clickListener = Observer<Int> {
        when (it) {
            R.id.addContactsButton -> {
                launchActivity<SendMoneyHomeActivity>(
                    requestCode = RequestCodes.REQUEST_NOTIFY_BENEFICIARY_LIST,
                    type = FeatureSet.ADD_SEND_MONEY_BENEFICIARY
                )
            }
            R.id.layoutSearchView -> {
                viewModel.isSearching.value?.let { isSearching ->
                    if (isSearching) {
                        setSearchView(isSearching)
                    } else {
                        launchActivity<SendMoneyLandingActivity>(
                            type = FeatureSet.SEND_MONEY,
                            requestCode = REQUEST_TRANSFER_MONEY
                        ) {
                            putExtra(searching, true)
                        }
                    }
                }
            }
            R.id.foregroundContainer -> {
                viewModel.clickEvent.getPayload()?.let { payload ->
                    if (payload.itemData is Beneficiary) {
                        startMoneyTransfer(payload.itemData as Beneficiary, payload.position)
                    }
                }
                viewModel.clickEvent.setPayload(null)

            }
            R.id.btnEdit -> {
                viewModel.clickEvent.getPayload()?.let { payload ->
                    if (payload.itemData is Beneficiary) {
                        openEditBeneficiary(payload.itemData as Beneficiary)
                    }
                }
                viewModel.clickEvent.setPayload(null)
            }
            R.id.btnDelete -> {
                viewModel.clickEvent.getPayload()?.let { payload ->
                    if (payload.itemData is Beneficiary) {
                        if (SessionManager.user?.otpBlocked == true) {
                            showBlockedFeatureAlert(this, FeatureSet.DELETE_SEND_MONEY_BENEFICIARY)
                        } else {
                            positionToDelete = payload.position
                            confirmDeleteBeneficiary(payload.itemData as Beneficiary)
                        }
                    }
                }
                viewModel.clickEvent.setPayload(null)
            }
            R.id.tvCancel, R.id.tbBtnBack -> finish()
        }
    }

    private fun getAdaptor(): AllBeneficiariesAdapter {
        return getBinding().layoutBeneficiaries.rvAllBeneficiaries.adapter as AllBeneficiariesAdapter
    }

    private fun getSearchView(): SearchView {
        return getBinding().layoutBeneficiaries.layoutSearchView.svBeneficiary
    }

    private fun getBinding(): ActivitySendMoneyLandingBinding {
        return (viewDataBinding as ActivitySendMoneyLandingBinding)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    RequestCodes.REQUEST_NOTIFY_BENEFICIARY_LIST -> {
                        if (data.getBooleanExtra(Constants.BENEFICIARY_CHANGE, false)) {
                            val isMoneyTransfer =
                                data.getValue(Constants.IS_TRANSFER_MONEY, "BOOLEAN") as? Boolean
                            val isDismissFlow =
                                data.getValue(
                                    Constants.TERMINATE_ADD_BENEFICIARY,
                                    "BOOLEAN"
                                ) as? Boolean
                            val beneficiary =
                                data.getValue(
                                    Beneficiary::class.java.name,
                                    "PARCEABLE"
                                ) as? Beneficiary
                            when {
                                isMoneyTransfer == true -> {
                                    beneficiary?.let {
                                        startMoneyTransfer(it, 0)
                                        viewModel.requestAllBeneficiaries()
                                    }
                                }
                                isDismissFlow == true -> {
                                }
                                else -> viewModel.requestAllBeneficiaries()
                            }
                        } else if (data.getBooleanExtra(Constants.MONEY_TRANSFERED, false)) {
                            finish()
                        }
                    }
                    REQUEST_TRANSFER_MONEY -> {
                        if (resultCode == Activity.RESULT_OK && data.getBooleanExtra(
                                Constants.MONEY_TRANSFERED,
                                false
                            )
                        ) {
                            viewModel.isSearching.value?.let {
                                if (it) {
                                    val intent = Intent()
                                    intent.putExtra(Constants.MONEY_TRANSFERED, true)
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                } else {
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> finish()
            R.id.ivRightIcon -> {
                launchActivity<SendMoneyHomeActivity>(
                    requestCode = RequestCodes.REQUEST_NOTIFY_BENEFICIARY_LIST,
                    type = FeatureSet.ADD_SEND_MONEY_BENEFICIARY
                )
            }
        }
    }
}