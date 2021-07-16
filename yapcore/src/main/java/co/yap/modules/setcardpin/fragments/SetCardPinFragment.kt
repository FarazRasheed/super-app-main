package co.yap.modules.setcardpin.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.modules.setcardpin.activities.SetPinChildFragment
import co.yap.modules.setcardpin.pinflow.IPin
import co.yap.modules.setcardpin.pinflow.PINViewModel
import co.yap.yapcore.BR
import co.yap.yapcore.R
import co.yap.yapcore.databinding.FragmentPinBinding

class SetCardPinFragment : SetPinChildFragment<IPin.ViewModel>(), IPin.View {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_pin

    override val viewModel: IPin.ViewModel
        get() = ViewModelProviders.of(this).get(PINViewModel::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBindings().dialer.hideFingerprintView()
        getBindings().dialer.upDatedDialerPad(viewModel.state.pincode)
        getBindings().dialer.updateDialerLength(4)
        viewModel.setCardPinFragmentData()
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.btnAction -> {
                    val action =
                        SetCardPinFragmentDirections.actionSetCardPinFragmentToConfirmCardPinFragment(
                            viewModel.state.pincode
                        )
                    findNavController().navigate(action)
                }
            }
        })
        viewModel.errorEvent.observe(this, Observer {

        })
    }

    override fun loadData() {

    }

    private fun getBindings(): FragmentPinBinding {
        return viewDataBinding as FragmentPinBinding
    }

    override fun onDestroyView() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroyView()
    }
}