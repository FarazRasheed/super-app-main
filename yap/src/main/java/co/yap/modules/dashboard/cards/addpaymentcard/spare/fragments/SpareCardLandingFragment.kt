package co.yap.modules.dashboard.cards.addpaymentcard.spare.fragments


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.addpaymentcard.fragments.AddPaymentChildFragment
import co.yap.modules.dashboard.cards.addpaymentcard.interfaces.ISpareCards
import co.yap.modules.dashboard.cards.addpaymentcard.models.BenefitsModel
import co.yap.modules.dashboard.cards.addpaymentcard.spare.SpareCardsLandingAdapter
import co.yap.modules.dashboard.cards.addpaymentcard.spare.viewmodels.SpareCardLandingViewModel
import co.yap.modules.dashboard.cards.addpaymentcard.viewmodels.AddPaymentCardViewModel
import co.yap.yapcore.constants.Constants.KEY_AVAILABLE_BALANCE
import co.yap.yapcore.helpers.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_spare_card_landing.*


class SpareCardLandingFragment : AddPaymentChildFragment<ISpareCards.ViewModel>(), ISpareCards.View,
    SpareCardsLandingAdapter.OnItemClickedListener {

    override fun onItemClick(benefitsModel: BenefitsModel) {

    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_spare_card_landing

    override val viewModel: SpareCardLandingViewModel
        get() = ViewModelProviders.of(this).get(SpareCardLandingViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getVirtualCardFee()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addBenefitRecyclerView()
        context?.let { SharedPreferenceManager(it).removeValue(KEY_AVAILABLE_BALANCE) }

        activity?.let {
            ViewModelProviders.of(it).get(AddPaymentCardViewModel::class.java)
                .state.tootlBarTitle = "Add a virtual spare card"
        }
        setObservers()
    }

    private fun gotoAddSpareVirtualCardConfirmScreen() {
        val action =
            SpareCardLandingFragmentDirections.actionSpareCardLandingFragmentToAddSpareCardFragment(
                getString(R.string.screen_spare_card_landing_display_text_virtual_card),
                "",
                "",
                "",
                "",
                false
            )
        findNavController().navigate(action)
    }

    private fun addBenefitRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        rvBenefits.layoutManager = layoutManager
        rvBenefits.isNestedScrollingEnabled = false
        rvBenefits.adapter =
            SpareCardsLandingAdapter(
                viewModel.loadJSONDummyList(),
                null
            )
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.addSpareCard -> {
                    gotoAddSpareVirtualCardConfirmScreen()
                }
                R.id.llAddVirtualCard -> {
                    gotoAddSpareVirtualCardConfirmScreen()
                }

                R.id.llAddPhysicalCard -> {

                    val action =
                        SpareCardLandingFragmentDirections.actionSpareCardLandingFragmentToAddSpareCardFragment(
                            getString(R.string.screen_spare_card_landing_display_text_physical_card),
                            "",
                            "",
                            "",
                            "",
                            false
                        )
                    findNavController().navigate(action)
                }

            }
        })
        viewModel.errorEvent.observe(this, Observer {
            requireActivity().finish()
        })
        viewModel.isFeeReceived.observe(this, Observer {
            if (it) viewModel.updateFees("")
        })
        viewModel.updatedFee.observe(this, Observer {
            viewModel.state.virtualCardFee = it
            viewModel.parentViewModel?.virtualCardFee = it
        })
    }

    override fun removeObservers() {
        viewModel.clickEvent.removeObservers(this)
        viewModel.errorEvent.removeObservers(this)
    }

    override fun onPause() {
        removeObservers()
        super.onPause()
    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }
}