package co.yap.modules.dashboard.more.profile.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.cards.paymentcarddetail.fragments.CardClickListener
import co.yap.modules.dashboard.more.changepasscode.activities.ChangePasscodeActivity
import co.yap.modules.dashboard.more.main.activities.MoreActivity
import co.yap.modules.dashboard.more.main.fragments.MoreBaseFragment
import co.yap.modules.dashboard.more.profile.intefaces.IProfile
import co.yap.modules.dashboard.more.profile.viewmodels.ProfileSettingsViewModel
import co.yap.modules.others.helper.Constants
import co.yap.modules.webview.WebViewFragment
import co.yap.translation.Strings
import co.yap.yapcore.constants.Constants.KEY_IS_FINGERPRINT_PERMISSION_SHOWN
import co.yap.yapcore.constants.Constants.KEY_TOUCH_ID_ENABLED
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.enums.FeatureSet
import co.yap.yapcore.helpers.SharedPreferenceManager
import co.yap.yapcore.helpers.Utils
import co.yap.yapcore.helpers.biometric.BiometricUtil
import co.yap.yapcore.helpers.extentions.hasBitmap
import co.yap.yapcore.helpers.extentions.launchActivity
import co.yap.yapcore.helpers.extentions.startFragment
import co.yap.yapcore.helpers.permissions.PermissionHelper
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.layout_profile_picture.*
import kotlinx.android.synthetic.main.layout_profile_settings.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

class ProfileSettingsFragment : MoreBaseFragment<IProfile.ViewModel>(), IProfile.View,
    CardClickListener {

    private lateinit var updatePhotoBottomSheet: UpdatePhotoBottomSheet
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_profile
    private val takePhoto = 1
    private val pickPhoto = 2
    internal var permissionHelper: PermissionHelper? = null

    override val viewModel: IProfile.ViewModel
        get() = ViewModelProviders.of(this).get(ProfileSettingsViewModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is MoreActivity) {
            (context as MoreActivity).visibleToolbar()
        }
        viewModel.state.buildVersionDetail = versionName
        val sharedPreferenceManager =
            SharedPreferenceManager(requireContext())

        if (BiometricUtil.hasBioMetricFeature(requireContext())) {
            val isTouchIdEnabled: Boolean =
                sharedPreferenceManager.getValueBoolien(
                    KEY_TOUCH_ID_ENABLED,
                    false
                )
            swTouchId.isChecked = isTouchIdEnabled
            llSignInWithTouch.visibility = View.VISIBLE

            swTouchId.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    sharedPreferenceManager.save(
                        KEY_IS_FINGERPRINT_PERMISSION_SHOWN,
                        true
                    )
                    sharedPreferenceManager.save(KEY_TOUCH_ID_ENABLED, true)
                } else {
                    sharedPreferenceManager.save(
                        KEY_TOUCH_ID_ENABLED,
                        false
                    )
                }
            }
        } else {
            llSignInWithTouch.visibility = View.GONE
        }
    }

    override fun onClick(eventType: Int) {
        updatePhotoBottomSheet.dismiss()

        when (eventType) {
            Constants.EVENT_ADD_PHOTO -> {
                checkPermission(takePhoto)
            }
            Constants.EVENT_CHOOSE_PHOTO -> {
                checkPermission(pickPhoto)
            }
            Constants.EVENT_REMOVE_PHOTO -> {
                viewModel.requestRemoveProfilePicture {
                    if (it)
                        ivProfilePic.setImageDrawable(null)
                }
            }
        }
    }

    private fun checkPermission(type: Int) {
        permissionHelper = PermissionHelper(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 100
        )

        permissionHelper?.request(object : PermissionHelper.PermissionCallback {
            override fun onPermissionGranted() {
                if (type == takePhoto) {
                    EasyImage.openCamera(this@ProfileSettingsFragment, takePhoto)
                } else {
                    EasyImage.openGallery(this@ProfileSettingsFragment, pickPhoto)
                }

            }

            override fun onIndividualPermissionGranted(grantedPermission: Array<String>) {
                if (type == takePhoto) {
                    if (grantedPermission.contains(Manifest.permission.CAMERA))
                        EasyImage.openCamera(this@ProfileSettingsFragment, takePhoto)
                } else {
                    if (grantedPermission.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        EasyImage.openGallery(this@ProfileSettingsFragment, pickPhoto)
                }
            }

            override fun onPermissionDenied() {
                showToast("Can't proceed without permissions")
            }

            override fun onPermissionDeniedBySystem() {
                permissionHelper?.openAppDetailsActivity()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, activity,
            object : DefaultCallback() {
                override fun onImagePicked(
                    imageFile: File?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    onPhotosReturned(imageFile, type, source)
                }

                override fun onImagePickerError(
                    e: Exception?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    viewModel.state.toast = "Invalid file found^${AlertType.DIALOG.name}"
                }
            })
    }

    private fun onPhotosReturned(path: File?, position: Int, source: EasyImage.ImageSource?) {
        path?.let {
            val ext = path.extension
            if (!ext.isBlank()) {
                when (ext) {
                    "png", "jpg", "jpeg" -> {
                        viewModel.requestUploadProfilePicture(it)
                        viewModel.state.imageUri = it.toUri()
                        ivProfilePic.setImageURI(it.toUri())
                    }
                    else -> {
                        viewModel.state.toast = "Invalid file found^${AlertType.DIALOG.name}"
                    }
                }
            } else {
                viewModel.state.toast = "Invalid file found^${AlertType.DIALOG.name}"
            }
        }
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    private fun logoutAlert() {
        AlertDialog.Builder(this.activity!!)
            .setTitle(getString(R.string.screen_profile_settings_logout_display_text_alert_title))
            .setMessage(getString(R.string.screen_profile_settings_logout_display_text_alert_message))
            .setPositiveButton(getString(R.string.screen_profile_settings_logout_display_text_alert_logout),
                DialogInterface.OnClickListener { dialog, which ->
                    viewModel.logout()
                })

            .setNegativeButton(
                getString(R.string.screen_profile_settings_logout_display_text_alert_cancel),
                null
            )
            .show()
    }

    private fun doLogout() {
        SessionManager.doLogout(requireContext())
        activity?.finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        viewModel.clickEvent.removeObservers(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.clickEvent.observe(this, Observer {
            when (it) {

                R.id.tvPersonalDetailView -> {
                    val action =
                        ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToPersonalDetailsFragment()
                    findNavController().navigate(action)
                }

                R.id.tvPrivacyView -> {
                    Utils.showComingSoon(requireContext())
                }

                R.id.tvNotificationsView -> {
                    Utils.showComingSoon(requireContext())
                }

                R.id.tvChangePasscode -> {
                    launchActivity<ChangePasscodeActivity>(type = FeatureSet.CHANGE_PASSCODE)
                }
                R.id.tvTermsAndConditionView -> {
                    startFragment(
                        fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                            co.yap.yapcore.constants.Constants.PAGE_URL to co.yap.yapcore.constants.Constants.URL_TERMS_CONDITION,
                            co.yap.yapcore.constants.Constants.TOOLBAR_TITLE to getString(
                                Strings.screen_profile_settings_display_terms_and_conditions
                            )
                        ), showToolBar = false
                    )
                }

                R.id.tvFeesAndPricingPlansView -> {
                    startFragment(
                        fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                            co.yap.yapcore.constants.Constants.PAGE_URL to co.yap.yapcore.constants.Constants.URL_FEES_AND_PRICING_PLAN
                        ), showToolBar = false
                    )
                }
                R.id.tvFollowOnInstagram -> {
                    Utils.openInstagram(requireContext())
                }

                R.id.tvFollowOnTwitter -> {
                    Utils.openTwitter(requireContext())
                }

                R.id.tvLikeUsOnFaceBook -> {
                    Utils.getOpenFacebookIntent(requireContext())
                        ?.let { startActivity(it) }
                }

                R.id.ivProfilePic -> {
                }

                R.id.tvLogOut -> {
                    logoutAlert()
                }

                R.id.rlAddNewProfilePic -> {
                    this.fragmentManager?.let {
                        updatePhotoBottomSheet = UpdatePhotoBottomSheet(this, showRemovePhoto())
                        updatePhotoBottomSheet.show(it, "")
                    }
                }

                viewModel.PROFILE_PICTURE_UPLOADED -> {
                }

                viewModel.EVENT_LOGOUT_SUCCESS -> {
                    doLogout()
                }
            }
        })
    }

    private fun showRemovePhoto(): Boolean {
        return viewModel.state.profilePictureUrl.isNotEmpty() && ivProfilePic.hasBitmap()
    }

}