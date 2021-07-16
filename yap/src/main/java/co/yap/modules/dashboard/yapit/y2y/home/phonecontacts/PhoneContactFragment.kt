package co.yap.modules.dashboard.yapit.y2y.home.phonecontacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.databinding.FragmentPhoneContactsBinding
import co.yap.modules.dashboard.yapit.y2y.home.fragments.YapToYapFragment
import co.yap.modules.dashboard.yapit.y2y.home.fragments.YapToYapFragmentDirections
import co.yap.modules.dashboard.yapit.y2y.home.yapcontacts.YapContactsAdaptor
import co.yap.modules.dashboard.yapit.y2y.main.fragments.Y2YBaseFragment
import co.yap.networking.customers.requestdtos.Contact
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BR
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.helpers.PagingState
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.Utils.getAdjustURL
import co.yap.yapcore.helpers.Utils.getBody
import co.yap.yapcore.interfaces.OnItemClickListener


class PhoneContactFragment : Y2YBaseFragment<IPhoneContact.ViewModel>(),
    InviteBottomSheet.OnItemClickListener {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_phone_contacts
    override val viewModel: PhoneContactViewModel
        get() = ViewModelProviders.of(this).get(PhoneContactViewModel::class.java)

    lateinit var adaptor: YapContactsAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
        initComponents()
        setObservers()
        viewModel.getY2YBeneficiaries()
    }

    private fun initComponents() {
        adaptor =
            YapContactsAdaptor(mutableListOf())
        getBinding().recycler.adapter = adaptor
        adaptor.setItemListener(listener)
    }

    private fun initState() {
        viewModel.getState().observe(this, Observer { state ->
            if (viewModel.listIsEmpty()) {
                getBinding().recycler.visibility = View.GONE
                getBinding().tvContactListDescription.visibility = View.GONE
                getBinding().txtError.visibility =
                    if (state == PagingState.DONE || state == PagingState.ERROR) View.VISIBLE else View.GONE
                getBinding().progressBar.visibility =
                    if (state == PagingState.LOADING) View.VISIBLE else View.GONE
                if (state == PagingState.DONE || state == PagingState.ERROR) { // error type handling
                    viewModel.parentViewModel?.yapContactLiveData?.postValue(mutableListOf())
                }
            } else {
                getBinding().txtError.visibility = View.GONE
                getBinding().progressBar.visibility = View.GONE
                getBinding().recycler.visibility = View.VISIBLE
                getBinding().tvContactListDescription.visibility = View.VISIBLE
                viewModel.parentViewModel?.yapContactLiveData?.postValue(viewModel.phoneContactLiveData.value?.filter { it.yapUser!! })
            }
        })
    }

    private fun setObservers() {
        viewModel.phoneContactLiveData.observe(this, Observer {
            adaptor.setList(it)
            getBinding().tvContactListDescription.visibility =
                if (it.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            getBinding().txtError.text =
                if (viewModel.parentViewModel?.isSearching?.value!!) "No result" else Translator.getString(
                    requireContext(),
                    Strings.screen_y2y_display_text_no_contacts
                )
        })
        viewModel.parentViewModel?.searchQuery?.observe(this, Observer {
            adaptor.filter.filter(it)
        })

        viewModel.parentViewModel?.isSearching?.value?.let {
            if (it)
                adaptor.filterCount.observe(this, Observer { count ->
                    getBinding().tvContactListDescription.visibility =
                        if (count == 0) View.GONE else View.VISIBLE
                    getBinding().txtError.visibility =
                        if (count == 0 && viewModel.getState().value != PagingState.LOADING) View.VISIBLE else View.GONE
                    getBinding().txtError.text =
                        if (viewModel.parentViewModel?.isSearching?.value!!) "No result" else Translator.getString(
                            requireContext(),
                            Strings.screen_y2y_display_text_no_contacts
                        )
                })
        }
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (view.id) {
                R.id.userPackageType -> {

                }
                R.id.tvInvite -> {
                    sendInvite((data as Contact))
                }
                R.id.lyContact -> {
                    if (data is Contact && data.yapUser == true && data.accountDetailList != null && data.accountDetailList?.isNotEmpty() == true) {
                        if (parentFragment is YapToYapFragment) {
                            navigate(
                                YapToYapFragmentDirections.actionYapToYapHomeToY2YTransferFragment(
                                    data.beneficiaryPictureUrl ?: "",
                                    data.accountDetailList?.get(0)?.accountUuid ?: "",
                                    data.title ?: "",
                                    pos
                                ), screenType = FeatureSet.Y2Y_TRANSFER
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sendInvite(contact: Contact) {
        shareInfo(contact)
    }

    private fun shareInfo(contact: Contact) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getBody(requireContext(), contact))
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    }

    override fun onClick(viewId: Int, data: Any) {

    }
    private fun getBinding(): FragmentPhoneContactsBinding {
        return (viewDataBinding as FragmentPhoneContactsBinding)
    }
}