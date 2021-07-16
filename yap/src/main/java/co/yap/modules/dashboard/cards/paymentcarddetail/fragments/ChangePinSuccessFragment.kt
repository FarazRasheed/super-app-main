package co.yap.modules.dashboard.cards.paymentcarddetail.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.forgotcardpin.activities.ForgotCardPinActivity
import co.yap.modules.dashboard.cards.paymentcarddetail.interfaces.IChangeCardPinSuccess
import co.yap.modules.dashboard.cards.paymentcarddetail.viewmodels.ChangeCardPinSuccessViewModel
import co.yap.yapcore.BaseBindingFragment

class ChangePinSuccessFragment : BaseBindingFragment<IChangeCardPinSuccess.ViewModel>(),
    IChangeCardPinSuccess.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_change_pin_success

    override val viewModel: IChangeCardPinSuccess.ViewModel
        get() = ViewModelProviders.of(this).get(ChangeCardPinSuccessViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity is ForgotCardPinActivity){
            (activity as ForgotCardPinActivity).viewModel.state.toolBarVisibility=false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clickEvent.observe(this, Observer {
            activity?.finish()
        })
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}