package co.yap.sendmoney.addbeneficiary.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.sendmoney.addbeneficiary.viewmodels.TransferTypeViewModel
import co.yap.sendmoney.addbeneficiary.interfaces.ITransferType
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.fragments.SendMoneyBaseFragment

class TransferTypeFragment : SendMoneyBaseFragment<ITransferType.ViewModel>(),
    ITransferType.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_transfer_type

    override val viewModel: ITransferType.ViewModel
        get() = ViewModelProviders.of(this).get(TransferTypeViewModel::class.java)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.llBankTransferType -> {
                    findNavController().navigate(R.id.action_transferTypeFragment_to_addBeneficiaryFragment)
//                    findNavController().navigate(R.id.action_transferTypeFragment_to_addBeneficiaryForDomesticTransferFragment)
                }

                R.id.llCashPickUpTransferType -> {
                    //findNavController().navigate(R.id.action_transferTypeFragment_to_addBeneficiaryForCashFlowFragment)
                    findNavController().navigate(R.id.action_transferTypeFragment_to_addBeneficiaryFragment)
                }
            }
        })
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed(): Boolean {
        return super.onBackPressed()
    }
}