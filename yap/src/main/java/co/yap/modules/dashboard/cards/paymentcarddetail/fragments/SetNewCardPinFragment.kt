package co.yap.modules.dashboard.cards.paymentcarddetail.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.yap.R
import co.yap.modules.setcardpin.pinflow.IPin
import co.yap.modules.setcardpin.pinflow.PINViewModel
import co.yap.yapcore.BR
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.databinding.FragmentPinBinding

open class SetNewCardPinFragment : BaseBindingFragment<IPin.ViewModel>(), IPin.View {
    private val args: SetNewCardPinFragmentArgs by navArgs()
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_pin

    var oldPinCode: String? = null
    override val viewModel: PINViewModel
        get() = ViewModelProviders.of(this).get(PINViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNewCardPinFragmentdata()
        getBindings().dialer.hideFingerprintView()
        getBindings().dialer.upDatedDialerPad(viewModel.state.pincode)
        getBindings().dialer.updateDialerLength(4)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnAction -> {
                    oldPinCode = args.oldPinCode
                    val action =
                        SetNewCardPinFragmentDirections.actionSetNewCardPinFragmentToConfirmNewCardPinFragment(
                            args.flowType,
                            oldPinCode.toString(),
                            viewModel.state.pincode
                        )
                    findNavController().navigate(action)
                }
            }
        })
    }

    override fun loadData() {

    }

    fun getBindings(): FragmentPinBinding {
        return viewDataBinding as FragmentPinBinding
    }

    override fun onDestroyView() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroyView()
    }
}