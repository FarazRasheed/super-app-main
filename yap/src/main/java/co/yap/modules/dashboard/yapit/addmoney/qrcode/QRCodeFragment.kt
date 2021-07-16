package co.yap.modules.dashboard.yapit.addmoney.qrcode

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.yapcore.helpers.ImageBinding
import co.yap.yapcore.helpers.extentions.generateQrCode
import co.yap.yapcore.helpers.extentions.shareImage
import co.yap.yapcore.helpers.extentions.storeBitmap
import co.yap.yapcore.helpers.permissions.PermissionHelper
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.fragment_qr_code.*

class QRCodeFragment : DialogFragment(), IQRCode.View {
    lateinit var viewDataBinding: ViewDataBinding
    fun getBindingVariable(): Int = BR.viewModel
    fun getLayoutId(): Int = R.layout.fragment_qr_code
    var permissionHelper: PermissionHelper? = null

    override val viewModel: IQRCode.ViewModel
        get() = ViewModelProviders.of(this).get(QRCodeViewModel::class.java)

    override fun showLoader(isVisible: Boolean) {
    }

    override fun showToast(msg: String) {
    }

    override fun showInternetSnack(isVisible: Boolean) {
    }

    override fun isPermissionGranted(permission: String): Boolean {
        return false
    }

    override fun requestPermissions() {
    }

    override fun getString(resourceKey: String): String {
        return ""
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(getBindingVariable(), viewModel)
        viewDataBinding.lifecycleOwner = this
        viewModel.clickEvent.observe(this, clickEventObserver)
        viewDataBinding.executePendingBindings()
        updateUI()
    }

    private fun updateUI() {
        ImageBinding.loadAvatar(
            ivProfilePic,
            viewModel.state.profilePictureUrl,
            viewModel.state.fullName,
            android.R.color.transparent,
            R.dimen.text_size_h2
        )
        SessionManager.user?.let { accountInfo ->
            viewModel.state.qrBitmap =
                context?.generateQrCode(accountInfo.encryptedAccountUUID ?: "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.QRCodeTheme)
        viewModel.onCreate()
    }

    private val clickEventObserver = Observer<Int> {
        when (it) {
            R.id.tvSaveToGallery -> {
                checkPermission()
            }
            R.id.tvShareMyCode -> {
                context?.shareImage(qrContainer)
            }
            R.id.ivBack -> {
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clickEvent.removeObservers(this)
    }

    private fun checkPermission() {
        permissionHelper = PermissionHelper(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 100
        )
        permissionHelper?.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                context?.storeBitmap(qrContainer) {
                    Toast.makeText(
                        context,
                        getString(R.string.common_saved_image_to_gallery),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {
                Toast.makeText(
                    context,
                    getString(R.string.common_permission_rejected_error),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onPermissionDenied() {

            }

            override fun onPermissionDeniedBySystem() {

            }
        })
    }
}
