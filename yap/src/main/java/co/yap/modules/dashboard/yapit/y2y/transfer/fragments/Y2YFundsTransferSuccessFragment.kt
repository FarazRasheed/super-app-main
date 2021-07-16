package co.yap.modules.dashboard.yapit.y2y.transfer.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import co.yap.BR
import co.yap.R
import co.yap.databinding.FragmentY2yFundsTransferSuccessBinding
import co.yap.modules.dashboard.yapit.y2y.main.fragments.Y2YBaseFragment
import co.yap.modules.dashboard.yapit.y2y.transfer.interfaces.IY2YFundsTransferSuccess
import co.yap.modules.dashboard.yapit.y2y.transfer.viewmodels.Y2YFundsTransferSuccessViewModel
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.extentions.toFormattedCurrency
import co.yap.yapcore.managers.SessionManager

class Y2YFundsTransferSuccessFragment : Y2YBaseFragment<IY2YFundsTransferSuccess.ViewModel>(),
    IY2YFundsTransferSuccess.View {
    private val args: Y2YFundsTransferSuccessFragmentArgs by navArgs()
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_y2y_funds_transfer_success

    override val viewModel: IY2YFundsTransferSuccess.ViewModel
        get() = ViewModelProviders.of(this).get(Y2YFundsTransferSuccessViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.updateCardBalance {}
        viewModel.clickEvent.observe(this, Observer {
            activity?.finish()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()

    }

    private fun setUpData() {
        viewModel.state.title = args.title
        viewModel.state.transferredAmount =
            args.amount.toFormattedCurrency(showCurrency = true, currency = args.currencyType)
        viewModel.state.imageUrl = args.imagePath

        getBinding().lyUserImage.tvNameInitials.background = Utils.getContactBackground(
            getBinding().lyUserImage.tvNameInitials.context,
            args.position
        )


        getBinding().lyUserImage.tvNameInitials.setTextColor(
            Utils.getContactColors(
                getBinding().lyUserImage.tvNameInitials.context, args.position
            )
        )
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()

    }

    override fun getBinding(): FragmentY2yFundsTransferSuccessBinding {
        return viewDataBinding as FragmentY2yFundsTransferSuccessBinding
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}