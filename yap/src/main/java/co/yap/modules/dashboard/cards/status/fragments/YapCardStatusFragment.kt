package co.yap.modules.dashboard.cards.status.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.status.interfaces.IYapCardStatus
import co.yap.modules.dashboard.cards.status.viewmodels.YapCardStatusViewModel
import co.yap.modules.setcardpin.activities.SetCardPinWelcomeActivity
import co.yap.networking.cards.responsedtos.Card
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.enums.CardDeliveryStatus
import co.yap.yapcore.enums.CardType
import co.yap.yapcore.enums.PartnerBankStatus
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.widget_step_indicator_layout.*


class YapCardStatusFragment : BaseBindingFragment<IYapCardStatus.ViewModel>(), IYapCardStatus.View {

    companion object {
        const val data = "payLoad"

        @JvmStatic
        fun newInstance(payLoad: Parcelable?) = YapCardStatusFragment().apply {
            arguments = Bundle().apply {
                putParcelable("dataList", payLoad)
            }
        }
    }

    var card: Card? = null
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_card_status

    override val viewModel: IYapCardStatus.ViewModel
        get() = ViewModelProviders.of(this).get(YapCardStatusViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card = arguments?.getParcelable("dataList")
        setObservers()
        updateBindings()
    }

    private fun updateBindings() {
        if (card != null) {
            viewModel.state.title.set(if (card?.cardType == "DEBIT") "Primary card" else "Spare physical card")
            viewModel.state.cardType.set(if (card?.cardType == "DEBIT") "Primary card" else "Spare physical card")
            viewModel.state.message.set(if (card?.cardType == "DEBIT") "Your YAP card is on its way!" else "Your Spare physical card is on its way")


            when (card?.deliveryStatus?.let { CardDeliveryStatus.valueOf(it) }) {
                CardDeliveryStatus.ORDERED, CardDeliveryStatus.FAILED -> {
                    tbBtnOneOrdered.setImageResource(R.drawable.ic_tick)
                    tvOrdered.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.colorPrimary
                        )
                    )
                }
                CardDeliveryStatus.BOOKED -> {
                    tbBtnOneOrdered.setImageResource(R.drawable.ic_tick)
                    tvOrdered.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.colorPrimary
                        )
                    )

                    tbProgressBarBuilding.progress = 100

                    tbBtnBuilding.setImageResource(R.drawable.ic_tick)
                    tvBuilding.text = "Built"
                    tvBuilding.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                }

                CardDeliveryStatus.SHIPPING -> {
                    viewModel.state.message.set(if (card?.cardType == CardType.DEBIT.type && CardDeliveryStatus.SHIPPED.name == card?.deliveryStatus) "Your Primary card is shipped" else "")
                    tbBtnOneOrdered.setImageResource(R.drawable.ic_tick)
                    tvOrdered.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.colorPrimary
                        )
                    )

                    tbProgressBarBuilding.progress = 100
                    tbBtnBuilding.setImageResource(R.drawable.ic_tick)
                    tvBuilding.text = "Built"
                    tvBuilding.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )

                    viewModel.state.shippingProgress = 100
                    tvShipping.text =
                        if (CardDeliveryStatus.SHIPPED.name == card?.deliveryStatus) "Shipped" else "Shipping"
                    tvShipping.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    viewModel.state.valid =
                        card?.cardType == CardType.DEBIT.type && CardDeliveryStatus.SHIPPED.name == card?.deliveryStatus && PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus
                }
                CardDeliveryStatus.SHIPPED -> {
                    viewModel.state.message.set(if (card?.cardType == CardType.DEBIT.type && CardDeliveryStatus.SHIPPED.name == card?.deliveryStatus) "Your Primary card is shipped" else "")
                    tbBtnOneOrdered.setImageResource(R.drawable.ic_tick)
                    tvOrdered.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.colorPrimary
                        )
                    )

                    tbProgressBarBuilding.progress = 100
                    tbBtnBuilding.setImageResource(R.drawable.ic_tick)
                    tvBuilding.text = "Built"
                    tvBuilding.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )

                    viewModel.state.shippingProgress = 100
                    tbBtnShipping.setImageResource(R.drawable.ic_tick)
                    tvShipping.text =
                        if (CardDeliveryStatus.SHIPPED.name == card?.deliveryStatus) "Shipped" else "Shipping"
                    tvShipping.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    viewModel.state.valid =
                        card?.cardType == CardType.DEBIT.type && CardDeliveryStatus.SHIPPED.name == card?.deliveryStatus && PartnerBankStatus.ACTIVATED.status == SessionManager.user?.partnerBankStatus
                }
            }
        }
    }

    private fun setObservers() {
        viewModel.clickEvent.observe(this, observer)
    }

    private val observer = Observer<Int> {
        when (it) {
            R.id.btnActivateCard -> {
                card?.let { card ->
                    startActivityForResult(
                        SetCardPinWelcomeActivity.newIntent(
                            requireContext(),
                            card
                        ), Constants.EVENT_CREATE_CARD_PIN
                    )
                } ?: showToast("Debit card not found.")
            }
            R.id.tbBtnBack -> {
                val returnIntent = Intent()
                returnIntent.putExtra(Constants.isPinCreated, false)
                activity?.setResult(Activity.RESULT_CANCELED, returnIntent)
                activity?.finish()
            }
        }
    }

    override fun onDestroyView() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.EVENT_CREATE_CARD_PIN -> {
                if (resultCode == Activity.RESULT_OK) {// search this to look into conflict YAP_ISSUE_KNOWN_01
                    val isPinCreated: Boolean? =
                        data?.getBooleanExtra(Constants.isPinCreated, false)
                    val serialNumber: String? =
                        data?.getStringExtra(Constants.CARD_SERIAL_NUMBER)
                    val isTopupSkip: Boolean? =
                        data?.getBooleanExtra(Constants.IS_TOPUP_SKIP, false)
                    if (isPinCreated == true) {
                        val returnIntent = Intent()
                        returnIntent.putExtra(Constants.CARD_SERIAL_NUMBER, serialNumber)
                        returnIntent.putExtra(Constants.isPinCreated, isPinCreated)
                        returnIntent.putExtra(Constants.IS_TOPUP_SKIP, isTopupSkip)
                        activity?.setResult(Activity.RESULT_OK, returnIntent)
                        activity?.finish()
                    }
                }
            }
        }
    }
}