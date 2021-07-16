package co.yap.modules.dashboard.cards.addpaymentcard.spare.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.addpaymentcard.spare.interfaces.ICardBenefit
import co.yap.modules.dashboard.cards.addpaymentcard.spare.viewmodels.CardBenfitsDetailViewModel
import co.yap.modules.dashboard.cards.addpaymentcard.viewmodels.AddPaymentCardViewModel
import co.yap.modules.dashboard.main.fragments.YapDashboardChildFragment


class CardsBenefitsFragment : YapDashboardChildFragment<ICardBenefit.ViewModel>(),
    ICardBenefit.View {


    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_card_benefit

    override val viewModel: ICardBenefit.ViewModel
        get() = ViewModelProviders.of(this).get(CardBenfitsDetailViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            ViewModelProviders.of(it).get(AddPaymentCardViewModel::class.java)
                .state.tootlBarTitle = "Spare card benefits"
        }

        viewModel.state.benefitsModel =
            arguments?.let { CardsBenefitsFragmentArgs.fromBundle(it).benefitsModel }

        viewModel.clickEvent.observe(this, Observer {

        })
    }
}