package co.yap.modules.dashboard.yapit.y2y.home.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.BR
import co.yap.R
import co.yap.databinding.ActivityYapToYapDashboardBinding
import co.yap.modules.dashboard.yapit.y2y.main.interfaces.IY2Y
import co.yap.modules.dashboard.yapit.y2y.main.viewmodels.Y2YViewModel
import co.yap.yapcore.BaseBindingActivity
import co.yap.yapcore.IFragmentHolder
import co.yap.yapcore.defaults.DefaultNavigator
import co.yap.yapcore.defaults.INavigator
import co.yap.yapcore.helpers.*
import co.yap.yapcore.interfaces.BackPressImpl
import co.yap.yapcore.interfaces.IBaseNavigator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_yap_to_yap_dashboard.*

class YapToYapDashboardActivity : BaseBindingActivity<IY2Y.ViewModel>(), INavigator,
    IFragmentHolder {

    companion object {
        const val searching = "searching"
        const val data = "payLoad"
    }

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_yap_to_yap_dashboard
    override val viewModel: IY2Y.ViewModel
        get() = ViewModelProviders.of(this).get(Y2YViewModel::class.java)

    override val navigator: IBaseNavigator
        get() = DefaultNavigator(
            this@YapToYapDashboardActivity,
            R.id.main_nav_host_fragment
        )

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isSearching.value = intent.getBooleanExtra(searching, false)
        viewModel.errorEvent.observe(this, errorEvent)
        main.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    val errorEvent = Observer<String> {
        if (!it.isNullOrEmpty())
            showErrorSnackBar(it)
        else
            hideErrorSnackBar()
    }

    override fun onToolBarClick(id: Int) {
        when (id) {
            R.id.ivLeftIcon -> {
                onBackPressed()
            }
            R.id.ivRightIcon -> {
                Utils.shareText(this, getBody())
            }
        }
    }

    private fun showErrorSnackBar(errorMessage: String) {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(errorMessage)
            }
        } ?: clSnackBar.showSnackBar(
            msg = errorMessage,
            viewBgColor = R.color.errorLightBackground,
            colorOfMessage = R.color.error, duration = Snackbar.LENGTH_INDEFINITE, marginTop = 0
        )
    }

    private fun hideErrorSnackBar() {
        cancelAllSnackBar()
    }

    private fun getBody(): String {
        return Utils.getGeneralInvitationBody(this)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        if (!BackPressImpl(fragment).onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.errorEvent.removeObservers(this)
    }

    fun getBindings(): ActivityYapToYapDashboardBinding {
        return viewDataBinding as ActivityYapToYapDashboardBinding
    }
}