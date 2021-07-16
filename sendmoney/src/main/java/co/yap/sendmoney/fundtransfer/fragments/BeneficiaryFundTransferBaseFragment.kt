package co.yap.sendmoney.fundtransfer.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import co.yap.sendmoney.fundtransfer.viewmodels.BeneficiaryFundTransferBaseViewModel
import co.yap.sendmoney.fundtransfer.viewmodels.BeneficiaryFundTransferViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.IBase

abstract class BeneficiaryFundTransferBaseFragment<V : IBase.ViewModel<*>> :
    BaseBindingFragment<V>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel is BeneficiaryFundTransferBaseViewModel<*> && activity != null) {
            (viewModel as BeneficiaryFundTransferBaseViewModel<*>).parentViewModel =
                ViewModelProviders.of(requireActivity())
                    .get(BeneficiaryFundTransferViewModel::class.java)
        }
    }

    override fun onBackPressed(): Boolean {
        return super.onBackPressed()
    }
}