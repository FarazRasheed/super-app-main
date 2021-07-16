package co.yap.modules.dashboard.more.profile.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.main.activities.MoreActivity
import co.yap.modules.dashboard.more.profile.intefaces.ISuccess
import co.yap.modules.dashboard.more.profile.viewmodels.SuccessViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.fragment_success.*

class SuccessFragment : BaseBindingFragment<ISuccess.ViewModel>(),
    ISuccess.View {
    val args: SuccessFragmentArgs by navArgs()
    var successType: String? = null
    var photoPlacedId: String = " "

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_success

    override val viewModel: ISuccess.ViewModel
        get() = ViewModelProviders.of(this).get(SuccessViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.buttonClickEvent.observe(this, Observer {
            findNavController().popBackStack(R.id.personalDetailsFragment, true)
            findNavController().navigate(R.id.personalDetailsFragment)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is MoreActivity)
            (context as MoreActivity).goneToolbar()
        loadData()
    }

    private fun loadData() {
        successType = args.successType
        photoPlacedId = args.placesPhotoId

        val separatedPrimary =
            args.staticString.split(args.destinationValue)
        val primaryStr = SpannableStringBuilder(args.staticString + args.destinationValue)

        primaryStr.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)),
            separatedPrimary[0].length,
            primaryStr.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val addressStr = getString(R.string.screen_address_success_display_text_sub_heading_update)

        if (primaryStr.contains(addressStr)) {
            cvLocationCard.visibility = VISIBLE
            viewModel.state.topSubHeading = addressStr

            viewModel.state.address1 = SessionManager.userAddress?.address1 ?: ""
            viewModel.state.address2 = SessionManager.userAddress?.address2 ?: ""

            viewModel.placesApiCall(photoPlacedId) {
                placeImage?.setPadding(0, 0, 0, 0)
                placeImage?.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        } else {
            tvSuccessSubHeading.text = primaryStr
        }
    }

    override fun onDestroy() {
        viewModel.buttonClickEvent.removeObservers(this)
        super.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        return true
    }
}