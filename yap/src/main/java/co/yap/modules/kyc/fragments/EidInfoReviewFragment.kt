package co.yap.modules.kyc.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import co.yap.BR
import co.yap.R
import co.yap.modules.kyc.activities.DocumentsResponse
import co.yap.modules.kyc.enums.KYCAction
import co.yap.modules.kyc.viewmodels.EidInfoReviewViewModel
import co.yap.modules.onboarding.interfaces.IEidInfoReview
import co.yap.translation.Strings
import co.yap.widgets.Status
import co.yap.yapcore.enums.AlertType
import co.yap.yapcore.firebase.FirebaseEvents
import co.yap.yapcore.firebase.FirebaseTagManagerModel
import co.yap.yapcore.firebase.firebaseTagManagerEvent
import co.yap.yapcore.helpers.Utils.hideKeyboard
import co.yap.yapcore.helpers.showAlertDialogAndExitApp
import co.yap.yapcore.managers.SessionManager
import com.digitify.identityscanner.docscanner.activities.IdentityScannerActivity
import com.digitify.identityscanner.docscanner.enums.DocumentType
import kotlinx.android.synthetic.main.activity_eid_info_review.*
import java.io.File


class EidInfoReviewFragment : KYCChildFragment<IEidInfoReview.ViewModel>(), IEidInfoReview.View {

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.activity_eid_info_review

    override val viewModel: EidInfoReviewViewModel
        get() = ViewModelProviders.of(this).get(EidInfoReviewViewModel::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel.parentViewModel?.skipFirstScreen?.value == true) {
            openCardScanner()
            tbBtnBack.setOnClickListener {
                viewModel.parentViewModel?.finishKyc?.value = DocumentsResponse(false)
            }
        }
        addObservers()
    }

    private fun addObservers() {
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.ivEditFirstName, R.id.tvFirstName -> {
                    ivEditFirstName.isEnabled = false
                    ivEditMiddleName.isEnabled = true
                    ivEditLastName.isEnabled = true
                    manageFocus(tvFirstName, ivEditFirstName)
                }

                R.id.ivEditMiddleName, R.id.tvMiddleName -> {
                    ivEditMiddleName.isEnabled = false
                    ivEditFirstName.isEnabled = true
                    ivEditLastName.isEnabled = true
                    manageFocus(tvMiddleName, ivEditMiddleName)
                }

                R.id.ivEditLastName, R.id.tvLastName -> {
                    ivEditLastName.isEnabled = false
                    ivEditMiddleName.isEnabled = true
                    ivEditFirstName.isEnabled = true
                    manageFocus(tvLastName, ivEditLastName)
                }

                viewModel.eventErrorInvalidEid -> showInvalidEidAlert()
                viewModel.eventErrorExpiredEid -> showExpiredEidAlert()
                viewModel.eventErrorUnderAge -> showUnderAgeAlert()
                viewModel.eventErrorFromUsa -> showUSACitizenAlert()
                viewModel.eventRescan -> openCardScanner()
                R.id.tvNoThanks -> {
                    hideKeyboard(tvNoThanks)
                    openCardScanner()
                }
                viewModel.eventAlreadyUsedEid -> {
                    viewModel.parentViewModel?.finishKyc?.value =
                        DocumentsResponse(false, KYCAction.ACTION_EID_FAILED.name)
                }

                viewModel.eventNextWithError -> {
                    viewModel.performUploadDocumentsRequest(true) {
                        if (it.equals("success", true)) {
                            val action =
                                EidInfoReviewFragmentDirections.actionEidInfoReviewFragmentToInformationErrorFragment(
                                    viewModel.errorTitle, viewModel.errorBody
                                )
                            findNavController().navigate(action)
                        } else {
                            viewModel.state.toast = "${it}^${AlertType.DIALOG.name}"
                        }
                    }

                }
                viewModel.eventFinish -> {
                    viewModel.parentViewModel?.finishKyc?.value =
                        DocumentsResponse(false, KYCAction.ACTION_EID_FAILED.name)
                }
                viewModel.eventNext -> {
                    requireActivity().firebaseTagManagerEvent(FirebaseTagManagerModel(action = FirebaseEvents.CONFIRM_ID.event))
                    SessionManager.getAccountInfo()
                    SessionManager.onAccountInfoSuccess.observe(this, Observer { isSuccess ->
                        if (isSuccess) {
                            viewModel.parentViewModel?.finishKyc?.value =
                                DocumentsResponse(true)
                        } else {
                            showToast("Accounts info failed")
                            viewModel.parentViewModel?.finishKyc?.value =
                                DocumentsResponse(true)
                        }

                    })
                }
                viewModel.eventEidUpdate -> {
                    SessionManager.getAccountInfo()
                    SessionManager.onAccountInfoSuccess.observe(this, Observer { isSuccess ->
                        if (isSuccess) {
                            viewModel.parentViewModel?.finishKyc?.value =
                                DocumentsResponse(false, KYCAction.ACTION_EID_UPDATE.name)
                        } else {
                            showToast("Accounts info failed")
                            viewModel.parentViewModel?.finishKyc?.value =
                                DocumentsResponse(false, KYCAction.ACTION_EID_UPDATE.name)
                        }

                    })
                }
                viewModel.eventCitizenNumberIssue, viewModel.eventEidExpiryDateIssue -> invalidCitizenNumber(
                    "Sorry, that didn’t work. Please try again"
                )
            }
        })
        viewModel.eidStateLiveData.observe(this, Observer {
            if (it.status == Status.ERROR) {
                invalidCitizenNumber(it.message ?: "Sorry, that didn’t work. Please try again")
            }
        })
    }

    private fun invalidCitizenNumber(title: String) {
        activity?.let {
            it.showAlertDialogAndExitApp(
                message = title,
                callback = {
                    openCardScanner()
                },
                closeActivity = false
            )
            viewModel.parentViewModel?.paths?.forEach { filePath ->
                File(filePath).deleteRecursively()
            }
        }
    }

    private fun manageFocus(
        editText: EditText,
        ivEditName: ImageView
    ) {
        if (!editText.isFocused) {
            editText.isFocusable = true

            editText.isFocusableInTouchMode = true
            editText.isActivated = true
            editText.setSelection(editText.length())
            editText.requestFocus()
            editText.performClick()
            (editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        editText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                ivEditName.isEnabled = true
                editText.isFocusable = false
                editText.isFocusableInTouchMode = false
            }
        }

        editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.action === KeyEvent.ACTION_DOWN || keyEvent.action === KeyEvent.KEYCODE_ENTER
            ) {
                ivEditName.isEnabled = true
                editText.isFocusable = false
                editText.isFocusableInTouchMode = false
            }
            false
        })


    }

    override fun onDestroyView() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroyView()
    }

    override fun showUnderAgeAlert() {
        showEIDAlert(
            message = getString(Strings.screen_b2c_eid_info_review_display_text_error_under_age),
            posBtn = getString(Strings.common_button_yes),
            negBtn = getString(Strings.screen_b2c_eid_info_review_button_not_under_age),
            response = { positiveClick ->
                if (positiveClick) {
                    val action =
                        EidInfoReviewFragmentDirections.actionEidInfoReviewFragmentToInformationErrorFragment(
                            viewModel.errorTitle, viewModel.errorBody
                        )
                    findNavController().navigate(action)
                } else {
                    openCardScanner()
                }
            }
        )
    }

    override fun showExpiredEidAlert() {
        showEIDAlert(
            message = getString(Strings.screen_b2c_eid_info_review_display_text_error_expired_eid),
            posBtn = getString(Strings.common_button_yes),
            negBtn = getString(Strings.screen_b2c_eid_info_review_button_valid_emirates_id),
            response = { positiveClick ->
                if (positiveClick) {
                    val action =
                        EidInfoReviewFragmentDirections.actionEidInfoReviewFragmentToInformationErrorFragment(
                            viewModel.errorTitle, viewModel.errorBody
                        )
                    findNavController().navigate(action)
                } else {
                    openCardScanner()
                }
            }
        )
    }

    override fun showInvalidEidAlert() {
        showEIDAlert(
            message = getString(Strings.idenetity_scanner_sdk_screen_review_info_display_text_error_not_readable),
            posBtn = getString(R.string.ok),
            response = { positiveClick ->
                if (positiveClick) {
                    val action =
                        EidInfoReviewFragmentDirections.actionEidInfoReviewFragmentToInformationErrorFragment(
                            viewModel.errorTitle, viewModel.errorBody
                        )
                    findNavController().navigate(action)
                } else {
                    openCardScanner()
                }
            }
        )
    }

    private fun showEIDAlert(
        message: String,
        posBtn: String,
        negBtn: String? = null,
        response: (Boolean) -> Unit
    ) {
        AlertDialog.Builder(requireContext()).apply {
            setCancelable(false)
            setMessage(message)
            setPositiveButton(posBtn) { _, _ ->
                response.invoke(true)
            }
            if (negBtn != null)
                setNegativeButton(negBtn) { _, _ ->
                    response.invoke(false)
                }
        }.create().show()
    }

    override fun showUSACitizenAlert() {
        showEIDAlert(
            message = getString(Strings.screen_b2c_eid_info_review_display_text_error_from_usa).format(
                viewModel.sanctionedCountry
            ),
            posBtn = getString(Strings.common_button_yes),
            negBtn = getString(Strings.screen_b2c_eid_info_review_button_not_from_usa).format(
                viewModel.sanctionedCountry
            ),
            response = { positiveClick ->
                if (positiveClick) {
                    viewModel.performUploadDocumentsRequest(true) { message ->
                        if (message.equals("success", true)) {
                            val action =
                                EidInfoReviewFragmentDirections.actionEidInfoReviewFragmentToInformationErrorFragment(
                                    viewModel.errorTitle, viewModel.errorBody
                                )
                            findNavController().navigate(action)
                        } else {
                            viewModel.state.toast = "${message}^${AlertType.DIALOG.name}"
                        }
                    }
                } else {
                    openCardScanner()
                }
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null && viewModel.parentViewModel?.skipFirstScreen?.value == true) {

        }
        if (requestCode == IdentityScannerActivity.SCAN_EID_CAM && resultCode == Activity.RESULT_OK) {
            data?.let {
                viewModel.onEIDScanningComplete(it.getParcelableExtra(IdentityScannerActivity.SCAN_RESULT))
            }
        } else {
            viewModel.parentViewModel?.finishKyc?.value = DocumentsResponse(false)
        }
    }

    override fun openCardScanner() {
        viewModel.invalidateFields()
        startActivityForResult(
            IdentityScannerActivity.getLaunchIntent(
                requireContext(),
                DocumentType.EID,
                IdentityScannerActivity.SCAN_FROM_CAMERA
            ),
            IdentityScannerActivity.SCAN_EID_CAM
        )
    }

    override fun onDestroy() {
        viewModel.parentViewModel?.paths?.forEach { filePath ->
            File(filePath).deleteRecursively()
        }
        super.onDestroy()
    }
}