package co.yap.modules.dashboard.more.profile.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.modules.dashboard.more.profile.intefaces.IUnverifiedChangeEmailSuccess
import co.yap.modules.dashboard.more.profile.viewmodels.UnverifiedChangeEmailSuccessViewModel
import co.yap.modules.dashboard.unverifiedemail.UnVerifiedEmailActivity
import co.yap.translation.Strings
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.managers.SessionManager
import kotlinx.android.synthetic.main.fragment_unverified_change_email_success.*

class UnverifiedChangeEmailSuccessFragment :
    BaseBindingFragment<IUnverifiedChangeEmailSuccess.ViewModel>(),
    IUnverifiedChangeEmailSuccess.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_unverified_change_email_success

    override val viewModel: IUnverifiedChangeEmailSuccess.ViewModel
        get() = ViewModelProviders.of(this).get(UnverifiedChangeEmailSuccessViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mailButtonClickEvent.observe(this, Observer {
            when (it) {
                R.id.btnOpenMailApp -> {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                    startActivity(Intent.createChooser(intent, "Choose"))
                }
                R.id.tvGoToDashboard -> {
                    activity?.finish()
                }
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is UnVerifiedEmailActivity)
        (context as UnVerifiedEmailActivity).hideToolbar()
        val email=SessionManager.user?.currentCustomer?.email

        val fcs = ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))

        val separatedPrimary =
            getString(Strings.screen_unverified_success_display_text_sub_heading).split(email!!)
        val primaryStr = SpannableStringBuilder(getString(Strings.screen_unverified_success_display_text_sub_heading) + email)

        primaryStr.setSpan(
            fcs,
            separatedPrimary[0].length,
            primaryStr.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvUnverifySuccessSubHeading.text = primaryStr
    }

    override fun onBackPressed(): Boolean {
        return true
    }

}