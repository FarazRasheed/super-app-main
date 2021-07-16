package co.yap.modules.dashboard.yapit.y2y.home.yapcontacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.R
import co.yap.databinding.FragmentYapContactsBinding
import co.yap.modules.dashboard.yapit.y2y.home.fragments.YapToYapFragment
import co.yap.modules.dashboard.yapit.y2y.home.fragments.YapToYapFragmentDirections
import co.yap.modules.dashboard.yapit.y2y.main.fragments.Y2YBaseFragment
import co.yap.networking.customers.requestdtos.Contact
import co.yap.networking.customers.responsedtos.sendmoney.Beneficiary
import co.yap.translation.Strings
import co.yap.translation.Translator
import co.yap.yapcore.BR
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.PagingState
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.interfaces.OnItemClickListener

class YapContactsFragment : Y2YBaseFragment<IYapContact.ViewModel>() {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_yap_contacts

    override val viewModel: YapContactViewModel
        get() = ViewModelProviders.of(this).get(YapContactViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
        initComponents()
        setObservers()
    }

    private fun initComponents() {

        getBinding().recycler.adapter = YapContactsAdaptor(mutableListOf())
        (getBinding().recycler.adapter as YapContactsAdaptor).setItemListener(listener)
    }

    private fun initState() {
        viewModel.getState().observe(this, Observer { state ->
            if ((getBinding().recycler.adapter as YapContactsAdaptor).getDataList()
                    .isNullOrEmpty()
            ) {
                getBinding().recycler.visibility = View.GONE
                getBinding().txtError.visibility =
                    if (state == PagingState.DONE || state == PagingState.ERROR) View.VISIBLE else View.GONE
                getBinding().btnInvite.visibility =
                    if (state == PagingState.DONE || state == PagingState.ERROR) if (viewModel.parentViewModel?.isSearching?.value!!) View.GONE else View.VISIBLE else View.GONE
                getBinding().progressBar.visibility =
                    if (state == PagingState.LOADING) View.VISIBLE else View.GONE

            } else {
                getBinding().txtError.visibility = View.GONE
                getBinding().btnInvite.visibility = View.GONE
                getBinding().progressBar.visibility = View.GONE
                getBinding().recycler.visibility = View.VISIBLE
            }
        })
        viewModel.pagingState.value = PagingState.LOADING
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        viewModel.clickEvent.observe(this, observer)
        viewModel.parentViewModel?.yapContactLiveData?.observe(this, Observer {
            (getBinding().recycler.adapter as YapContactsAdaptor).setList(it)
            getBinding().txtError.visibility = View.GONE
            getBinding().tvContactListDescription.visibility =
                if (it.isEmpty()) View.GONE else View.VISIBLE

            viewModel.pagingState.value = PagingState.DONE
            getBinding().tvContactListDescription.text =
                if (it.size == 1) "${it.size} YAP contact" else "${it.size} YAP contacts"

            getBinding().txtError.text =
                if (viewModel.parentViewModel?.isSearching?.value!!) "No result" else Translator.getString(
                    requireContext(),
                    Strings.screen_y2y_display_text_no_yap_contacts
                )
        })

        viewModel.parentViewModel?.searchQuery?.observe(this, Observer {
            (getBinding().recycler.adapter as YapContactsAdaptor).filter.filter(it)
        })

        viewModel.parentViewModel?.isSearching?.value?.let {
            if (it)
                (getBinding().recycler.adapter as YapContactsAdaptor).filterCount.observe(
                    this,
                    Observer {
                        getBinding().tvContactListDescription.visibility =
                            if (it == 0) View.GONE else View.VISIBLE
                        getBinding().txtError.visibility =
                            if (it == 0 && viewModel.getState().value != PagingState.LOADING) View.VISIBLE else View.GONE
                        getBinding().txtError.text =
                            if (viewModel.parentViewModel?.isSearching?.value!!) "No result" else Translator.getString(
                                requireContext(),
                                Strings.screen_y2y_display_text_no_yap_contacts
                            )
                        getBinding().tvContactListDescription.text =
                            if (it == 1) "$it YAP contact" else "$it YAP contacts"
                    })
        }
    }

    val listener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (view.id) {
                R.id.lyContact -> {
                    if (data is Contact && data.yapUser == true && data.accountDetailList != null && !data.accountDetailList.isNullOrEmpty()) {
                        if (parentFragment is YapToYapFragment) {
                            navigate(
                                YapToYapFragmentDirections.actionYapToYapHomeToY2YTransferFragment(
                                    data.beneficiaryPictureUrl ?: "",
                                    data.accountDetailList?.get(0)?.accountUuid ?: "",
                                    data.title ?: "", pos,data.beneficiaryCreationDate?:""
                                ), screenType = FeatureSet.Y2Y_TRANSFER
                            )
                        }
                    }
                }
            }
        }
    }


    private val observer = Observer<Int> {
        when (it) {
            R.id.btnInvite -> {
                Utils.shareText(requireContext(), Utils.getGeneralInvitationBody(requireContext()))
            }
        }
    }

    private fun getBinding(): FragmentYapContactsBinding {
        return (viewDataBinding as FragmentYapContactsBinding)
    }
}