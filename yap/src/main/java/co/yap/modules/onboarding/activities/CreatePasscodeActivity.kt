package co.yap.modules.onboarding.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.passcode.IPassCode
import co.yap.modules.passcode.PassCodeViewModel
import co.yap.modules.webview.WebViewFragment
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.constants.Constants.URL_TERMS_CONDITION
import co.yap.yapcore.databinding.FragmentPassCodeBinding
import co.yap.yapcore.helpers.extentions.ExtraType
import co.yap.yapcore.helpers.extentions.getValue
import co.yap.yapcore.helpers.extentions.startFragment


class CreatePasscodeActivity : BaseBindingActivity<IPassCode.ViewModel>(),
    IPassCode.View {

    companion object {
        fun newIntent(context: Context, isSettingPin: Boolean): Intent {
            val intent = Intent(context, CreatePasscodeActivity::class.java)
            intent.putExtra("isSettingPin", isSettingPin)
            return intent
        }
    }

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_pass_code

    override val viewModel: IPassCode.ViewModel
        get() = ViewModelProviders.of(this).get(PassCodeViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.forgotTextVisibility = false
        viewModel.state.title = getString(Strings.screen_create_passcode_display_text_title)
        viewModel.state.buttonTitle =
            getString(Strings.screen_create_passcode_button_create_passcode)

        val isSettingPin = intent.getValue(
            "isSettingPin",
            ExtraType.BOOLEAN.name
        ) as? Boolean
        getBinding().clTermsAndConditions.visibility =
            if (isSettingPin == true) View.VISIBLE else View.INVISIBLE

        getBinding().dialer.hideFingerprintView()
        viewModel.clickEvent.observe(this, Observer {
            when (it) {
                R.id.tvTermsAndConditions -> {
                    startFragment<WebViewFragment>(
                        fragmentName = WebViewFragment::class.java.name, bundle = bundleOf(
                            co.yap.yapcore.constants.Constants.PAGE_URL to URL_TERMS_CONDITION
                        ), showToolBar = false
                    )
                }
                R.id.btnAction -> {
                    if (viewModel.isValidPassCode())
                        setIntentResults()
                }
            }
        })
    }

    private fun setIntentResults() {
        val intent = Intent()
        intent.putExtra("PASSCODE", viewModel.state.passCode)
        setResult(Constants.REQUEST_CODE_CREATE_PASSCODE, intent)
        finish()
    }

    override fun onDestroy() {
        viewModel.clickEvent.removeObservers(this)
        super.onDestroy()
    }

    fun getBinding(): FragmentPassCodeBinding {
        return viewDataBinding as FragmentPassCodeBinding
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                super.onBackPressed()
            }
        }
    }
}
